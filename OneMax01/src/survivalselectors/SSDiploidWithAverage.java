package survivalselectors;

import geneticalgorithms.GAException;
import individuals.Individual;
import populations.PDiploidWithAverage;
import populations.PDiploidWithDominance;
import populations.Population;

import java.util.List;

public class SSDiploidWithAverage extends SurvivalSelector {
    @Override
    protected Population returnNewPopulation(List<Individual> newPopulation) throws GAException {
        return new PDiploidWithAverage(newPopulation);
    }
}
