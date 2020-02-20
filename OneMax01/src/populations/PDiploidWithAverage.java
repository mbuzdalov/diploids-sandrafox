package populations;

import geneticalgorithms.GAException;
import individuals.IDiploidWithAverage;
import individuals.IDiploidWithDominance;
import individuals.Individual;

import java.util.ArrayList;
import java.util.List;

public class PDiploidWithAverage extends Population {
    public PDiploidWithAverage(int count, int size) {
        super();
        population = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            population.add(new IDiploidWithAverage(size));
        }
    }

    public PDiploidWithAverage(List<Individual> individuals) throws GAException {
        if (!individuals.stream().allMatch(i -> i instanceof IDiploidWithAverage))
            throw new GAException("For monoid population all individuals must be diploid");
        population = new ArrayList<>(individuals);
    }

    public boolean isTerminated(int maxValue) {
        return population.stream().anyMatch(i -> ((IDiploidWithAverage) i).isTerminated(maxValue));
    }
}
