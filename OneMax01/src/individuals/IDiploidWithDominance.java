package individuals;

import geneticalgorithms.DominanceType;
import geneticalgorithms.GAException;

import java.util.Arrays;

public class IDiploidWithDominance implements Individual {
    private final byte[][] genoms;
    private int fitness = -1;
    private int age;
    private final boolean[][] changeds;
    private final int[] countNotChangeds;
    private final DominanceType dt;

    public IDiploidWithDominance(int size, DominanceType dt) {
        if (dt == DominanceType.DELTA) size *= 2;
        genoms = new byte[2][size];
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

    public IDiploidWithDominance(byte[] value1, byte[] value2, boolean[] changed1, boolean[] changed2, DominanceType dt) {
        genoms = new byte[][] { value1.clone(), value2.clone() };
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
            byte[] genom = dominance(dt);
            fitness = 0;
            for (Byte aByte : genom) {
                fitness += aByte;
            }
        }
        return fitness;
    }

    @Override
    public boolean canChanged() {
        return canChanged(0) || canChanged(1);
    }

    @Override
    public byte[] getGenom(int number) throws GAException {
        if (!(number == 0 || number == 1)) throw new GAException("Diploid individual has only two genoms");
        // TODO: isn't there three genomes? (dominator, zeroth and first)
        return genoms[number];
    }

    public byte[] dominance(DominanceType dominanceType) {
        switch (dominanceType) {
            case OR: {
                byte[] genom = new byte[genoms[1].length];
                for (int i = 0; i < genom.length; i++) {
                    genom[i] = genoms[0][i] == 1 ? 1 : genoms[1][i];
                }
                return genom;
            }
            case AND: {
                byte[] genom = new byte[genoms[1].length];
                for (int i = 0; i < genom.length; i++) {
                    genom[i] = genoms[0][i] == 0 ? 0 : genoms[1][i];
                }
                return genom;
            }
            case DELTA: {
                byte[] genom = new byte[genoms[1].length / 2];
                for (int i = 0; i < genom.length; i++) {
                    if (genoms[0][2 * i] == 0) {
                        if (genoms[1][2 * i] == 0 && genoms[1][2 * i + 1] == 1) {
                            genom[i] = 1;
                        } else {
                            if (genoms[0][2 * i] == 0) {
                                genom[i] = 0;
                            } else {
                                if (genoms[1][2 * i] == genoms[1][2 * i + 1]) {
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
                                if (genoms[1][2 * i] != genoms[1][2 * i + 1]) {
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
        }
        return null;
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

    public boolean[] getChanged(int number) throws GAException {
        if (!(number == 0 || number == 1)) throw new GAException("Diploid individual has only two genoms");
        return changeds[number];
    }

    public boolean equals(Object o) {
        if (o.getClass() == IDiploidWithDominance.class) {
            IDiploidWithDominance i = (IDiploidWithDominance) o;
            return Arrays.deepEquals(this.genoms, i.genoms);
        }
        return false;
    }
}
