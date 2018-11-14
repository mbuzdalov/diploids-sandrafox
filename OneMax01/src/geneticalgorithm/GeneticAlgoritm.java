package geneticalgorithm;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class GeneticAlgoritm {
    private int countOfPoint;
    private double probabilityCrossover;
    private Population population;
    private ParentSelector pSelector = new ParentSelector();
    private SurvivalSelector sSelector = new SurvivalSelector();
    private int typeSelectionParents, typeSelectionSurvival;
    private int maximalFitness;
    private Individual maximalFitnessIndividual;
    private double standardProbability;
    private int length;

    public GeneticAlgoritm(int size, int length, int countOfPoint, double probabilityCrossover, int typeSelectionParents, int typeSelectionSurvival) {
        population = new Population(size, length);
        maximalFitnessIndividual = population.maximalFitness();
        maximalFitness = maximalFitnessIndividual.calcFitness();
        this.countOfPoint = countOfPoint;
        this.probabilityCrossover = probabilityCrossover;
        this.typeSelectionParents = typeSelectionParents;
        this.typeSelectionSurvival = typeSelectionSurvival;
        standardProbability = 1 / length;
        this.length = length;
    }

    public boolean isTerminated(int maxValue) {
        return maxValue == maximalFitness;
    }

    public List<Integer> evalPopulation() {
        return population.calculateFitness();
    }

    public Population getPopulation() {
        return population;
    }

    public int getMaximalFitness() {
        return maximalFitness;
    }

    private void updateMaximalFitness() {
        Individual individual = population.maximalFitness();
        if (individual.calcFitness() > maximalFitness) {
            maximalFitness = individual.calcFitness();
            maximalFitnessIndividual = individual;
        }
    }

    public void randomLocalSearch() throws GAException {
        List<Individual> children = new ArrayList<>();
        for (Individual ind : pSelector.select(population.deleteConstant(), population.getSize()/10, typeSelectionParents)) {
            int index = ThreadLocalRandom.current().nextInt(length);
            while (!ind.inverseGene(index)) {
                index = ThreadLocalRandom.current().nextInt(length);
            }
            children.add(ind);
        }
        population = sSelector.select(population, children, typeSelectionSurvival);
        population.incrementAges();
        evalPopulation();
        updateMaximalFitness();
    }

    public void standardBitMutation() throws GAException {
        List<Individual> children = new ArrayList<>();
        for (Individual ind : pSelector.select(population, population.getSize()/10, typeSelectionParents)) {
            Byte[] b = ind.getGenom();
            Byte[] child = new Byte[b.length];
            for (int i = 0; i < b.length; i++) {
                if (Math.random() > standardProbability) {
                    child[i] = (byte)(1 - b[i]);
                } else {
                    child[i] = b[i];
                }
            }
            children.add(new Individual(child, ind.getChanged()));
        }
        population = sSelector.select(population, children, typeSelectionSurvival);
        population.incrementAges();
        evalPopulation();
        updateMaximalFitness();
    }

    public void mutation() throws GAException {
        List<Individual> children = new ArrayList<>();
        for (Individual ind : pSelector.select(population, population.getSize()/10, typeSelectionParents)) {
            children.add(new Individual(mutation(ind.getGenom()), ind.getChanged()));
        }
        population = sSelector.select(population, children, typeSelectionSurvival);
        population.incrementAges();
        evalPopulation();
        updateMaximalFitness();
    }

    public void crossover(int type) throws GAException {
        List<Individual> children = new ArrayList<>();
        List<Individual> parents = pSelector.select(population, population.getSize()/10, typeSelectionParents);
        boolean[] changed = parents.get(0).getChanged();
        for (int i = 0; i < parents.size() - 1; i+= 2) {
            switch (type) {
                case 0:
                    children.addAll(multiPointCrossover(parents.subList(i, i + 2).stream().map(ind -> ind.getGenom()).collect(Collectors.toList())).stream().map(b -> new Individual(b, changed)).collect(Collectors.toList()));
                    break;
                case 1:
                    children.addAll(uniformCrossover(parents.subList(i, i + 2).stream().map(ind -> ind.getGenom()).collect(Collectors.toList())).stream().map(b ->new Individual(b, changed)).collect(Collectors.toList()));
                    break;
                default:
                    throw new GAException("Sorry, " + type + " is incorrect type of crossoving");
            }
        }
        population = sSelector.select(population, children, typeSelectionSurvival);
        population.incrementAges();
        evalPopulation();
        updateMaximalFitness();
    }

    public void crossoverAndMutation(int type) throws GAException {
        List<Individual> children = new ArrayList<>();
        List<Individual> parents = pSelector.select(population, population.getSize()/10, typeSelectionParents);
        boolean[] changed = parents.get(0).getChanged();
        for (int i = 0; i < parents.size() - 1; i+= 2) {
            switch (type) {
                case 0:
                    children.addAll(multiPointCrossover(parents.subList(i, i + 2).stream().map(ind -> ind.getGenom()).collect(Collectors.toList())).stream().map(b ->new Individual(mutation(b), changed)).collect(Collectors.toList()));
                    break;
                case 1:
                    children.addAll(uniformCrossover(parents.subList(i, i + 2).stream().map(ind -> ind.getGenom()).collect(Collectors.toList())).stream().map(b ->new Individual(mutation(b), changed)).collect(Collectors.toList()));
                    break;
                default:
                    throw new GAException("Sorry, " + type + " is incorrect type of crossoving");
            }
        }
        population = sSelector.select(population, children, typeSelectionSurvival);
        population.incrementAges();
        evalPopulation();
        updateMaximalFitness();
    }

    private Byte[] mutation(Byte[] genom) {
        int length = genom.length;
        Byte[] newGenom = new Byte[length];
        System.arraycopy(genom, 0, newGenom, 0, length);
        int count = ThreadLocalRandom.current().nextInt(length + 1);
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            positions.add(i);
        }
        for (int i = 0; i < count; i++) {
            int index = ThreadLocalRandom.current().nextInt(length - i);
            int pos = positions.get(index);
            //newGenom[pos] = 1;
            newGenom[pos] = (byte)(1 - newGenom[pos]);
            positions.remove(index);
        }
        return newGenom;
    }

    public List<Byte[]> multiPointCrossover(List<Byte[]> parents) throws GAException {
        if (parents.size() != 2) {
            throw new GAException("Sorry, expected two parents");
        }
        List<Byte[]> children = new ArrayList<>();
        int length = parents.get(0).length;
        Byte[] c0 = new Byte[length], c1 = new Byte[length];
        int size = length/(countOfPoint + 1);
        int index = 1;
        for (int k = 0; k < countOfPoint; k++) {
            for (int i = k*size; i < (k + 1) * size; i++) {
                c0[i] = parents.get(index)[i];
                c1[i] = parents.get(1 - index)[i];
            }
            index = 1 - index;
        }
        for (int i = countOfPoint * size; i < length; i++) {
            c0[i] = parents.get(index)[i];
            c1[i] = parents.get(1 - index)[i];
        }
        children.add(c0);
        children.add(c1);
        return children;
    }

    public List<Byte[]> uniformCrossover(List<Byte[]> parents) throws GAException {
        if (parents.size() != 2) {
            throw new GAException("Sorry, expected two parents");
        }
        int length = parents.get(0).length;
        Byte[] c0 = new Byte[length], c1 = new Byte[length];
        for (int i = 0; i < length; i++) {
            if (probabilityCrossover < Math.random()) {
                c0[i] = parents.get(1)[i];
                c1[i] = parents.get(0)[i];
            } else {
                c0[i] = parents.get(0)[i];
                c1[i] = parents.get(1)[i];
            }
        }
        List<Byte[]> children = new ArrayList<>();
        children.add(c0);
        children.add(c1);
        return children;
    }
}