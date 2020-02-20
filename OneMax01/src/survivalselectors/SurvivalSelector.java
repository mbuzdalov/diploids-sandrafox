package survivalselectors;

import geneticalgorithms.GAException;
import individuals.Individual;
import populations.Population;

import java.util.ArrayList;
import java.util.List;

public abstract class SurvivalSelector {
    public Population select(Population old, List<? extends Individual> children, TypeSelectionSurvival type, int size) throws GAException {
        switch (type) {
            case AGE:
                return ageBased(old, children, size);
            case FITNESS:
                return fitnessBased(old, children, size);
            default:
                throw new GAException("Sorry, " + type + " is incorrect type of survival selection");
        }
    }

    private Population ageBased(Population old, List<? extends Individual> children, int size) throws GAException {
        List<Individual> newPopulation = new ArrayList<>(children);
        newPopulation.addAll(old.getNewest(size - children.size()));
        return returnNewPopulation(newPopulation);
    }

    private Population fitnessBased(Population old, List<? extends Individual> children, int size) throws GAException {
        List<Individual> newPopulation = new ArrayList<>(children);
        newPopulation.addAll(old.getMaximal(size - children.size()));
        return returnNewPopulation(newPopulation);
    }

    protected abstract Population returnNewPopulation(List<Individual> newPopulation) throws GAException;
}
