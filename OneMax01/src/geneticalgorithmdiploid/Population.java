package geneticalgorithmdiploid;

import geneticalgorithmdiploid.Individual;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Population {
    private List<geneticalgorithmdiploid.Individual> population;

    public Population(int count, int size, DominanceType dt) {
        population = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            population.add(new geneticalgorithmdiploid.Individual(size, dt));
        }
    }

    public int getSize() {
        return population.size();
    }

    public geneticalgorithmdiploid.Individual getIndividual(int number) {
        return population.get(number);
    }

    /*public geneticalgorithmdiploid.Population(List<Byte[]> values){
        population = values.stream().map(v -> new geneticalgorithmdiploid.Individual(v)).collect(Collectors.toList());
    }*/

    public Population(List<geneticalgorithmdiploid.Individual> individuals) {
        population = new ArrayList<>(individuals);
    }

    public List<Integer> calculateFitness() {
        List<Integer> fitness = new ArrayList<>();
        for (geneticalgorithmdiploid.Individual i : population) {
            fitness.add(i.calcFitness());
        }
        return fitness;
    }

    public void deleteConstant() {
        List<geneticalgorithmdiploid.Individual> ind = new ArrayList<>();
        for (geneticalgorithmdiploid.Individual i : population) {
            if (i.canChanged(1) || i.canChanged(2)){
                ind.add(i);
            }
        }
        population = new ArrayList<>(ind);
    }

    public geneticalgorithmdiploid.Individual maximalFitness() {
        return population.stream().max(Comparator.comparing(geneticalgorithmdiploid.Individual::calcFitness)).orElse(null);
    }

    public List<geneticalgorithmdiploid.Individual> getPopulation() {
        return population.stream().sorted(Comparator.comparing(geneticalgorithmdiploid.Individual::calcFitness)).collect(Collectors.toList());
    }

    public void incrementAges() {
        for (geneticalgorithmdiploid.Individual i : population) {
            i.incrementAge();
        }
    }

    public List<geneticalgorithmdiploid.Individual> getNewest(int count) {
        List<geneticalgorithmdiploid.Individual> sortedPopulation = population.stream().sorted(Comparator.comparing(geneticalgorithmdiploid.Individual::getAge).thenComparing(geneticalgorithmdiploid.Individual::calcFitness, Comparator.reverseOrder())).collect(Collectors.toList());
        List<geneticalgorithmdiploid.Individual> newest = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            newest.add(sortedPopulation.get(i));
        }
        return newest;
    }

    public List<geneticalgorithmdiploid.Individual> getMaximal(int count) {
        List<geneticalgorithmdiploid.Individual> sortedPopulation = population.stream().sorted(Comparator.comparing(geneticalgorithmdiploid.Individual::calcFitness)).collect(Collectors.toList());
        List<Individual> newest = new ArrayList<>();
        int length = sortedPopulation.size();
        for (int i = 1; i <= count; i++) {
            newest.add(sortedPopulation.get(length - i));
        }
        return newest;
    }
}
