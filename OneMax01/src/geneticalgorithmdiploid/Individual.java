package geneticalgorithmdiploid;

public class Individual {
    private Byte[][] genoms;
    private int fitness = -1;
    private int age;
    private boolean[][] changeds;
    private int[] countNotChangeds;
    private DominanceType dt;

    public Individual(int size, DominanceType dt) {
        if (dt == DominanceType.DELTA) size *= 2;
        genoms = new Byte[2][];
        genoms[0] = new Byte[size];
        genoms[1] = new Byte[size];
        changeds = new boolean[2][];
        changeds[0] = new boolean[size];
        changeds[1] = new boolean[size];
        for (int i = 0; i < size; i++) {
            if (Math.random() > 0.5) {
                genoms[0][i] = 1;
            } else {
                genoms[0][i] = 0;
            }
            if (Math.random() > 0.5) {
                genoms[1][i] = 1;
            } else {
                genoms[1][i] = 0;
            }
            changeds[0][i] = false;
            changeds[1][i] = false;
        }
        fitness = -1;
        countNotChangeds = new int[2];
        countNotChangeds[0] = size;
        countNotChangeds[1] = size;
        this.dt = dt;
    }

    public void incrementAge() {
        age++;
    }

    public int getAge() {
        return age;
    }

    public Individual(Byte[] value1, Byte[] value2, boolean[] changed1,boolean[] changed2, DominanceType dt) {
        genoms = new Byte[2][];
        genoms[0] = new Byte[value1.length];
        genoms[1] = new Byte[value2.length];
        System.arraycopy(value1, 0, genoms[0], 0, value1.length);
        System.arraycopy(value2, 0, genoms[1], 0, value2.length);
        changeds = new boolean[2][];
        this.changeds[0] = new boolean[changed1.length];
        this.changeds[1] = new boolean[changed2.length];
        countNotChangeds = new int[2];
        countNotChangeds[0] = changed1.length;
        countNotChangeds[1] = changed2.length;
        for (int i = 0; i < changed1.length; i++) {
            if (changed1[i]) {
                countNotChangeds[0]--;
            }
            if (changed2[i]) {
                countNotChangeds[1]--;
            }
            this.changeds[0][i] = changed1[i];
            this.changeds[1][i] = changed2[i];
        }
        this.dt = dt;
        calcFitness();
    }

    public int calcFitness() {

        if (fitness == -1) {
            Byte[] genom = dominance(dt);
            fitness = 0;
            for (int i = 0; i < genom.length; i++) {
                fitness += genom[i];
            }
        }
        return fitness;
    }

    public Byte[] dominance(DominanceType dominanceType) {
        switch (dominanceType) {
            case OR:
                Byte[] genom = new Byte[genoms[1].length];
                for (int i = 0; i < genom.length; i++) {
                    genom[i] = genoms[0][i] == 1 ? 1 : genoms[1][i];
                }
                return genom;
            case AND:
                genom = new Byte[genoms[1].length];
                for (int i = 0; i < genom.length; i++) {
                    genom[i] = genoms[0][i] == 0 ? 0 : genoms[1][i];
                }
                return genom;
            case DELTA:
                genom = new Byte[genoms[1].length / 2];
                for (int i = 0; i < genom.length; i++) {
                    if (genoms[0][2 * i] == 0) {
                        if (genoms[1][2 * i] == 0 && genoms[1][2 * i + 1] == 1) {
                            genom[i] = 1;
                        } else {
                            if (genoms[0][2 * i] == 0) {
                                genom[i] = 0;
                            } else {
                                if (genoms[1][2 * i].equals(genoms[1][2 * i + 1])) {
                                    genom[i] = 1;
                                } else {
                                    genom[i] = 0;
                                }
                            }
                        }
                    } else {
                        if (genoms[1][2 * i] == 1 && genoms[1][2 * i + 1] == 1) {
                            genom[i] = 1;
                        } else {
                            if (genoms[0][2 * i] == 0) {
                                genom[i] = 0;
                            } else {
                                if (!genoms[1][2 * i].equals(genoms[1][2 * i + 1])) {
                                    genom[i] = 1;
                                } else {
                                    genom[i] = 0;
                                }
                            }
                        }
                    }
                }
                return genom;
        }
        return null;
    }

    public Byte[] getGenom(DominanceType dt) {
        this.dt = dt;
        return dominance(dt);
    }

    public void setDominance(DominanceType dt) {
        this.dt = dt;
    }

    public Byte[] getGenom1() {
        return genoms[0];
    }

    public Byte[] getGenom2() {
        return genoms[1];
    }

    public boolean inverseGene(int position, int genom) {
        if (changeds[genom][position]) {
            return false;
        }
        changeds[genom][position] = true;
        countNotChangeds[genom]--;
        genoms[genom][position] = (byte)(1 - genoms[genom][position]);
        if (genoms[genom][position] == 0) {
            fitness--;
        } else {
            fitness++;
        }
        return true;
    }

    public boolean canChanged(int genom) {
        return countNotChangeds[genom] > 0;
    }

    public boolean[] getChanged(int genom) {
        return changeds[genom];
    }

    public boolean equals(Object o) {
        if (o.getClass() == Individual.class) {
            Individual i = (Individual) o;
            return this.genoms.equals(i.genoms);
        }
        return false;
    }
}
