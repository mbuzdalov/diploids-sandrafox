package geneticalgorithm;

import java.util.ArrayList;
import java.util.List;

public class SurvivalSelector {
    public Population select(Population old, List<Individual> children, int type) throws GAException {
        switch (type) {
            case 0:
                return ageBased(old, children);
            case 1:
                return fitnessBased(old, children);
            default:
                throw new GAException("Sorry, " + type + " is incorrect type of survival selection");
        }
    }

    private Population ageBased(Population old, List<Individual> children) {
        List<Individual> newPopulation = new ArrayList<>(children);
        newPopulation.addAll(old.getNewest(old.getSize() - children.size()));
        return new Population(newPopulation);
    }

    private Population fitnessBased(Population old, List<Individual> children) {
        List<Individual> newPopulation = new ArrayList<>(children);
        newPopulation.addAll(old.getMaximal(old.getSize() - children.size()));
        return new Population(newPopulation);
    }
}
