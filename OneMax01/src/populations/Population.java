package populations;

import individuals.IDiploidWithDominance;
import individuals.Individual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Population {
    protected List<Individual> population;

    public int getSize() {
        return population.size();
    }

    public Individual getIndividual(int number) {
        return population.get(number);
    }

    public List<Integer> calculateFitness() {
        List<Integer> fitness = new ArrayList<>();
        for (Individual i : population) {
            fitness.add(i.calcFitness());
        }
        return fitness;
    }

    public void deleteConstant() {
        List<Individual> ind = new ArrayList<>();
        for (Individual i : population) {
            if (i.canChanged()){
                ind.add(i);
            }
        }
        population = ind;
    }

    public Individual maximalFitness() {
        return population.stream().max(Comparator.comparing(Individual::calcFitness)).orElse(null);
    }

    public List<Individual> getPopulation() {
        return population;
    }

    public void incrementAges() {
        population.forEach(Individual::incrementAge);
    }

    public List<Individual> getNewest(int count) {
        List<Individual> sortedPopulation = population.stream().sorted(Comparator.comparing(Individual::getAge).
                thenComparing(Individual::calcFitness, Comparator.reverseOrder())).collect(Collectors.toList());
        List<Individual> newest = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            newest.add(sortedPopulation.get(i));
        }
        return newest;
    }

    public List<Individual> getMaximal(int count) {
        List<Individual> sortedPopulation = population.stream().sorted(Comparator.comparing(Individual::calcFitness)).
                collect(Collectors.toList());
        List<Individual> newest = new ArrayList<>();
        int length = sortedPopulation.size();
        for (int i = 1; i <= count; i++) {
            newest.add(sortedPopulation.get(length - i));
        }
        return newest;
    }
}
