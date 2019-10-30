package geneticalgorithmmonoid;

import java.util.ArrayList;
import java.util.List;

public class SurvivalSelector {
    public Population select(Population old, List<Individual> children, int type, int size) throws GAException {
        switch (type) {
            case 0:
                return ageBased(old, children, size);
            case 1:
                return fitnessBased(old, children, size);
            default:
                throw new GAException("Sorry, " + type + " is incorrect type of survival selection");
        }
    }

    private Population ageBased(Population old, List<Individual> children, int size) {
        List<Individual> newPopulation = new ArrayList<>(children);
        newPopulation.addAll(old.getNewest(size - children.size()));
        return new Population(newPopulation);
    }

    private Population fitnessBased(Population old, List<Individual> children, int size) {
        List<Individual> newPopulation = new ArrayList<>(children);
        newPopulation.addAll(old.getMaximal(size - children.size()));
        return new Population(newPopulation);
    }
}
