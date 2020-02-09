package survivalselectors;

import geneticalgorithms.GAException;
import individuals.IDiploidWithDominance;
import individuals.Individual;
import populations.PDiploidWithDominance;
import populations.Population;

import java.util.ArrayList;
import java.util.List;

public class SSDiploidWithDominance extends SurvivalSelector{

    @Override
    protected Population returnNewPopulation(List<Individual> newPopulation) throws GAException {
        return new PDiploidWithDominance(newPopulation);
    }
}
