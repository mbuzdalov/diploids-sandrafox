package geneticalgorithm;

public class Individual {
    private Byte[] genom;
    private int fitness = -1;
    private int age;
    private boolean[] changed;
    private int countNotChanged;

    public Individual(int size) {
        genom = new Byte[size];
        changed = new boolean[size];
        for (int i = 0; i < size; i++) {
            if (Math.random() > 0.5) {
                genom[i] = 1;
            } else {
                genom[i] = 0;
            }
            changed[i] = false;
        }
        fitness = -1;
        countNotChanged = size;
    }

    public void incrementAge() {
        age++;
    }

    public int getAge() {
        return age;
    }

    public Individual(Byte[] value, boolean[] changed) {
        genom = new Byte[value.length];
        System.arraycopy(value, 0, genom, 0, value.length);
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

    public int calcFitness() {
        if (fitness == -1) {
            fitness = 0;
            for (int i = 0; i < genom.length; i++) {
                fitness += genom[i];
            }
        }
        return fitness;
    }

    public Byte[] getGenom() {
        return genom;
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

    public boolean[] getChanged() {
        return changed;
    }
}
