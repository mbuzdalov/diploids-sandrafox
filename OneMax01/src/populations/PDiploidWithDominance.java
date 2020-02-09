package populations;

import geneticalgorithms.DominanceType;
import geneticalgorithms.GAException;
import individuals.IDiploidWithDominance;
import individuals.Individual;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PDiploidWithDominance extends Population {

    public PDiploidWithDominance(int count, int size, DominanceType dt) {
        super();
        population = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            population.add(new IDiploidWithDominance(size, dt));
        }
    }

    public PDiploidWithDominance(List<Individual> individuals) throws GAException {
        if (!individuals.stream().allMatch(i -> i instanceof IDiploidWithDominance))
            throw new GAException("For monoid population all individuals must be monoid");
        population = new ArrayList<>(individuals);
    }
}
