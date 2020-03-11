package survivalselectors;

import geneticalgorithms.GAException;
import individuals.Individual;
import populations.PDiploidWithTable;
import populations.Population;

import java.util.List;

public class SSDiploidWithTable extends SurvivalSelector {
    @Override
    protected Population returnNewPopulation(List<Individual> newPopulation) throws GAException {
        return new PDiploidWithTable(newPopulation);
    }
}
