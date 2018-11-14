package geneticalgorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Population {
    private List<Individual> population;

    public Population(int count, int size) {
        population = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            population.add(new Individual(size));
        }
    }

    public int getSize() {
        return population.size();
    }

    public Individual getIndividual(int number) {
        return population.get(number);
    }

    /*public geneticalgorithm.Population(List<Byte[]> values){
        population = values.stream().map(v -> new geneticalgorithm.Individual(v)).collect(Collectors.toList());
    }*/

    public Population(List<Individual> individuals) {
        population = new ArrayList<>(individuals);
    }

    public List<Integer> calculateFitness() {
        List<Integer> fitness = new ArrayList<>();
        for (Individual i : population) {
            fitness.add(i.calcFitness());
        }
        return fitness;
    }

    public Individual maximalFitness() {
        return population.stream().max(Comparator.comparing(Individual::calcFitness)).orElse(null);
    }

    public List<Individual> getPopulation() {
        return population.stream().sorted(Comparator.comparing(Individual::calcFitness)).collect(Collectors.toList());
    }

    public void incrementAges() {
        for (Individual i : population) {
            i.incrementAge();
        }
    }

    public List<Individual> getNewest(int count) {
        List<Individual> sortedPopulation = population.stream().sorted(Comparator.comparing(Individual::getAge).thenComparing(Individual::calcFitness, Comparator.reverseOrder())).collect(Collectors.toList());
        List<Individual> newest = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            newest.add(sortedPopulation.get(i));
        }
        return newest;
    }

    public List<Individual> getMaximal(int count) {
        List<Individual> sortedPopulation = population.stream().sorted(Comparator.comparing(Individual::calcFitness)).collect(Collectors.toList());
        List<Individual> newest = new ArrayList<>();
        int length = sortedPopulation.size();
        for (int i = 1; i <= count; i++) {
            newest.add(sortedPopulation.get(length - i));
        }
        return newest;
    }
}
