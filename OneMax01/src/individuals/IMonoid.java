package individuals;

import geneticalgorithms.GAException;

import java.util.Arrays;

public class IMonoid implements Individual {
    private final byte[] genom;
    private int fitness = -1;
    private final boolean[] changed;
    private int countNotChanged;

    public IMonoid(int size) {
        genom = new byte[size];
        changed = new boolean[size];
        for (int i = 0; i < size; i++) {
            if (Math.random() > 0.5) {
                genom[i] = 1;
            } else {
                genom[i] = 0;
            }
            changed[i] = false;
        }
        countNotChanged = size;
    }

    public IMonoid(byte[] value, boolean[] changed) {
        genom = value.clone();
        this.changed = new boolean[changed.length];
        countNotChanged = changed.length;
        for (int i = 0; i < changed.length; i++) {
            if (changed[i]) {
                countNotChanged--;
            }
            this.changed[i] = changed[i];
        }
        calcFitness();
    }

    @Override
    public void incrementAge() {

    }

    @Override
    public int getAge() {
        return 0;
    }

    public int calcFitness() {
        if (fitness == -1) {
            fitness = 0;
            for (byte b : genom) {
                fitness += b;
            }
        }
        return fitness;
    }

    public boolean inverseGene(int position) {
        if (changed[position]) {
            return false;
        }
        changed[position] = true;
        countNotChanged--;
        genom[position] = (byte)(1 - genom[position]);
        if (genom[position] == 0) {
            fitness--;
        } else {
            fitness++;
        }
        return true;
    }

    public boolean canChanged() {
        return countNotChanged > 0;
    }

    @Override
    public byte[] getGenom(int number) throws GAException {
        if (number != 0) throw new GAException("Monoid individual has only one genom");
        return genom;
    }

    public boolean[] getChanged(int number) throws GAException {
        if (number != 0) throw new GAException("Monoid individual has only one genom");
        return changed;
    }

    public boolean equals(Object o) {
        if (o.getClass() == IMonoid.class) {
            IMonoid i = (IMonoid) o;
            return Arrays.equals(this.genom, i.genom);
        }
        return false;
    }
}
