package geneticalgorithmdiploid;

import geneticalgorithmmonoid.GAException;

import java.util.ArrayList;
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
    private DominanceType dt;

    public GeneticAlgoritm(int size, int length, int countOfPoint, double probabilityCrossover, int typeSelectionParents, int typeSelectionSurvival, DominanceType dt) {
        population = new Population(size, length, dt);
        maximalFitnessIndividual = population.maximalFitness();
        maximalFitness = maximalFitnessIndividual.calcFitness();
        this.countOfPoint = countOfPoint;
        this.probabilityCrossover = probabilityCrossover;
        this.typeSelectionParents = typeSelectionParents;
        this.typeSelectionSurvival = typeSelectionSurvival;
        standardProbability = 1 / (double)length;
        this.length = length;
        this.dt = dt;
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

    public void greedyMGA() throws GAException {
        List<Individual> parents = population.getMaximal(2);
        Byte[] c1 = uniformCrossover(parents.stream().map(Individual::getGenom1).collect(Collectors.toList()));
        Byte[] z1 = SBM(c1);
        Byte[] c2 = uniformCrossover(parents.stream().map(Individual::getGenom2).collect(Collectors.toList()));
        Byte[] z2 = SBM(c2);
        List<Individual> p = new ArrayList<>(population.getPopulation());
        Individual i = new Individual(z1, z2, parents.get(0).getChanged(0), parents.get(0).getChanged(1), dt);
        if (!p.contains(i) && i.calcFitness() > p.get(p.size() - 1).calcFitness()) {
            p.set(p.size() - 1, i);
        }
        population = new Population(p);
        updateMaximalFitness();
    }

    public void randomLocalSearch() throws GAException {
        int size = population.getSize();
        List<Individual> children = new ArrayList<>();
        population.deleteConstant();
        for (Individual ind : pSelector.select(population, 1, typeSelectionParents)) {
            Individual i = new Individual(ind.getGenom1(),
                    ind.getGenom2(),
                    ind.getChanged(0),
                    ind.getChanged(1),
                    dt);
            int index = ThreadLocalRandom.current().nextInt(length);
            while (!i.inverseGene(index, 0)) {
                index = ThreadLocalRandom.current().nextInt(length);
            }
            index = ThreadLocalRandom.current().nextInt(length);
            while (!i.inverseGene(index, 1)) {
                index = ThreadLocalRandom.current().nextInt(length);
            }
            if (i.calcFitness() >= ind.calcFitness()) {
                children.add(i);
            }
        }
        population = sSelector.select(population, children, typeSelectionSurvival, size);
        population.incrementAges();
        evalPopulation();
        updateMaximalFitness();
    }

    public void standardBitMutation() throws GAException {
        List<Individual> children = new ArrayList<>();
        for (Individual ind : pSelector.select(population, population.getSize()/10, typeSelectionParents)) {
            Byte[] b1 = ind.getGenom1();
            Byte[] b2 = ind.getGenom2();
            Byte[] child1 = SBM(b1);
            Byte[] child2 = SBM(b2);
            Individual i = new Individual(child1, child2, ind.getChanged(0), ind.getChanged(1), dt);
            if (i.calcFitness() > ind.calcFitness()) {
                children.add(i);
            }
        }
        population = sSelector.select(population, children, typeSelectionSurvival, population.getSize());
        population.incrementAges();
        evalPopulation();
        updateMaximalFitness();
    }

    private Byte[] SBM(Byte[] b) {
        Byte[] child = new Byte[b.length];
        int l = 0;
        for (int i = 0; i < b.length; i++) {
            if (Math.random() < standardProbability) {
                child[i] = (byte)(1 - b[i]);
                l++;
            } else {
                child[i] = b[i];
            }
        }
        if (l == 0) {
            int index = ThreadLocalRandom.current().nextInt(b.length);
            child[index] = (byte) (1 - b[index]);
        }
        return child;
    }

    private Byte[] uniformCrossover(List<Byte[]> parents) throws GAException {
        if (parents.size() != 2) {
            throw new GAException("Sorry, expected two parents");
        }
        int length = parents.get(0).length;
        Byte[] c0 = new Byte[length];
        for (int i = 0; i < length; i++) {
            if (probabilityCrossover < Math.random()) {
                c0[i] = parents.get(1)[i];
            } else {
                c0[i] = parents.get(0)[i];
            }
        }
        return c0;
    }
}
