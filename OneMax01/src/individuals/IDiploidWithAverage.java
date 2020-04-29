package individuals;

import geneticalgorithms.GAException;

import java.util.Arrays;

public class IDiploidWithAverage implements Individual {
    private final byte[][] genoms;
    private int fitness = -1;
    private int age;
    private final boolean[][] changeds;
    private final int[] countNotChangeds;
    private final int size;

    public IDiploidWithAverage(int size) {
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
        this.size = size;
    }

    public void incrementAge() {
        age++;
    }

    public int getAge() {
        return age;
    }

    public IDiploidWithAverage(byte[] value1, byte[] value2, boolean[] changed1, boolean[] changed2) {
        genoms = new byte[][] { value1.clone(), value2.clone() };
        size = value1.length;
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
        calcFitness();
    }

    public int calcFitness() {
        if (fitness == -1) {
            int fitness0 = 0, fitness1 = 0;
            for (int i = 0; i < size; i++) {
                fitness0 += genoms[0][i];
                fitness1 += genoms[1][i];
            }
            fitness = Math.max(fitness0, fitness1);
        }
        return fitness;
    }

    public boolean isTerminated(int maxValue) {
        int fitness0 = 0, fitness1 = 0;
        for (int i = 0; i < size; i++) {
            fitness0 += genoms[0][i];
            fitness1 += genoms[1][i];
        }
        return fitness0 == maxValue || fitness1 == maxValue;
    }

    @Override
    public boolean canChanged() {
        return canChanged(0) || canChanged(1);
    }

    @Override
    public byte[] getGenom(int number) throws GAException {
        if (!(number == 0 || number == 1)) throw new GAException("Diploid individual has only two genoms");
        return genoms[number];
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
        if (o.getClass() == IDiploidWithAverage.class) {
            IDiploidWithAverage i = (IDiploidWithAverage) o;
            return Arrays.deepEquals(this.genoms, i.genoms);
        }
        return false;
    }
}
