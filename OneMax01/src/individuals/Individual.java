package individuals;

import geneticalgorithms.GAException;

public interface Individual {

    void incrementAge();

    int getAge();

    int calcFitness();

    boolean canChanged();

    byte[] getGenom(int number) throws GAException;

    boolean[] getChanged(int number) throws GAException;
}
