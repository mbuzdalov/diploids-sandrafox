package populations;

import geneticalgorithms.GAException;
import individuals.IDiploidWithTable;
import individuals.Individual;

import java.util.ArrayList;
import java.util.List;

public class PDiploidWithTable extends Population {
    public PDiploidWithTable(int count, int size, int[] vector) {
        super();
        population = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            population.add(new IDiploidWithTable(size, vector));
        }
    }

    public PDiploidWithTable(List<Individual> individuals) throws GAException {
        if (!individuals.stream().allMatch(i -> i instanceof IDiploidWithTable))
            throw new GAException("For monoid population all individuals must be monoid");
        population = new ArrayList<>(individuals);
    }
}
