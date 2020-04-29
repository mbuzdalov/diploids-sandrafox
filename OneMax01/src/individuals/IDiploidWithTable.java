package individuals;

import geneticalgorithms.GAException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class IDiploidWithTable implements Individual {
    private final byte[][] genoms;
    private int fitness = -1;
    private int age;
    private final int size;
    private final int[] vector;

    public IDiploidWithTable(int size, int[] vector) {
        genoms = new byte[2][size];
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
        this.size = size;
        this.vector = vector;
    }

    public void incrementAge() {
        age++;
    }

    public int getAge() {
        return age;
    }

    public IDiploidWithTable(byte[] value1, byte[] value2, int[] vector) {
        genoms = new byte[][] { value1.clone(), value2.clone() };
        size = value1.length;
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
    public byte[] getGenom(int number) throws GAException {
        if (!(number == 0 || number == 1)) throw new GAException("Diploid individual has only two genoms");
        return genoms[number];
    }

    public boolean[] getChanged(int number) {
        throw new UnsupportedOperationException("MB: I don't get what this should mean, so I drop it");
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o.getClass() == getClass()) {
            IDiploidWithTable i = (IDiploidWithTable) o;
            return Arrays.deepEquals(this.genoms, i.genoms) && Arrays.equals(this.vector, i.vector);
        }
        return false;
    }

    public byte[] moreLikely(byte[][] gs) {
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
