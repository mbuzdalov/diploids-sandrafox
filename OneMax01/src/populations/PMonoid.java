package populations;

import geneticalgorithms.GAException;
import individuals.IDiploidWithDominance;
import individuals.IMonoid;
import individuals.Individual;

import java.util.ArrayList;
import java.util.List;

public class PMonoid extends Population {

    public PMonoid(int count, int size) {
        super();
        population = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            population.add(new IMonoid(size));
        }
    }

    public PMonoid(List<Individual> individuals) throws GAException {
        if (!individuals.stream().allMatch(i -> i instanceof IMonoid))
            throw new GAException("For monoid population all individuals must be monoid");
        population = new ArrayList<>(individuals);
    }
}
