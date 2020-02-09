package survivalselectors;

import geneticalgorithms.GAException;
import individuals.Individual;
import populations.PMonoid;
import populations.Population;

import java.util.List;

public class SSMonoid extends SurvivalSelector {
    @Override
    protected Population returnNewPopulation(List<Individual> newPopulation) throws GAException {
        return new PMonoid(newPopulation);
    }
}
