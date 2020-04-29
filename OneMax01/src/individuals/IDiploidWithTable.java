package individuals;

import geneticalgorithms.GAException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class IDiploidWithTable implements Individual {
    private Byte[][] genoms;
    private int fitness = -1;
    private int age;
    private int size;
    private int[] vector;

    public IDiploidWithTable(int size, int[] vector) {
        genoms = new Byte[2][];
        genoms[0] = new Byte[size];
        genoms[1] = new Byte[size];
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
        }
        fitness = -1;
        this.size = size;
        this.vector = vector;
    }

    public void incrementAge() {
        age++;
    }

    public int getAge() {
        return age;
    }

    public IDiploidWithTable(Byte[] value1, Byte[] value2, int[] vector) {
        genoms = new Byte[2][];
        size = value1.length;
        genoms[0] = new Byte[value1.length];
        genoms[1] = new Byte[value2.length];
        System.arraycopy(value1, 0, genoms[0], 0, value1.length);
        System.arraycopy(value2, 0, genoms[1], 0, value2.length);
        this.vector = vector;
        calcFitness();
    }

    public int[] getVector() {
        return vector;
    }

    @Override
    public int calcFitness() {
        if (fitness == -1) {
            fitness = 0;
            for (int i = 0; i < size; i++) {
                fitness += FitnessTable.TABLE[genoms[0][i] + genoms[1][i]][vector[i]];
            }
        }
        return fitness;
    }

    @Override
    public boolean canChanged() {
        throw new UnsupportedOperationException("MB: I don't get what this should mean, so I drop it");
    }

    @Override
    public Byte[] getGenom(int number) throws GAException {
        if (!(number == 0 || number == 1)) throw new GAException("Diploid individual has only two genoms");
        return genoms[number];
    }

    public Byte[] getGenom1() {
        return genoms[0];
    }

    public Byte[] getGenom2() {
        return genoms[1];
    }

    public boolean[] getChanged(int number) {
        throw new UnsupportedOperationException("MB: I don't get what this should mean, so I drop it");
    }

    public boolean equals(Object o) {
        if (o.getClass() == IDiploidWithTable.class) {
            IDiploidWithTable i = (IDiploidWithTable) o;
            return Arrays.deepEquals(this.genoms, i.genoms) && Arrays.equals(this.vector, i.vector);
        }
        return false;
    }

    public Byte[] moreLikely(Byte[][] gs) {
        int[] fs = {0, 0, 0, 0};
        int max = 0;
        for (int i = 0; i < size; i++) {
            if (vector[i] != 1) {
                for (int j = 0; j < 4; j++) {
                    fs[j] += FitnessTable.TABLE[gs[j][i] + gs[j][i]][vector[i]];
                    if (fs[j] > max) max = fs[j];
                }
            }
        }
        List<Integer> ans = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (fs[i] == max) ans.add(i);
        }
        return gs[ans.get(ThreadLocalRandom.current().nextInt(ans.size()))];
    }
}
