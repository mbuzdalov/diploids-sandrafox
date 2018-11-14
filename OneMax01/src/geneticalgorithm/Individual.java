package geneticalgorithm;

public class Individual {
    private Byte[] genom;
    private int fitness = -1;
    private int age;

    public Individual(int size) {
        genom = new Byte[size];
        for (int i = 0; i < size; i++) {
            if (Math.random() > 0.5) {
                genom[i] = 1;
            } else {
                genom[i] = 0;
            }
        }
        fitness = -1;
    }

    public void incrementAge() {
        age++;
    }

    public int getAge() {
        return age;
    }

    public Individual(Byte[] value) {
        genom = value;
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
}
