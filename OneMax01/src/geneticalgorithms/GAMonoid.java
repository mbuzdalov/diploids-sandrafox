package geneticalgorithms;

import individuals.IMonoid;
import individuals.Individual;
import parentselectors.TypeSelectionParents;
import populations.PMonoid;
import survivalselectors.SSMonoid;
import survivalselectors.TypeSelectionSurvival;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class GAMonoid extends GeneticAlgorithm {

    public GAMonoid(int size, int length, int countOfPoint, double probabilityCrossover,
                    TypeSelectionParents typeSelectionParents, TypeSelectionSurvival typeSelectionSurvival, AlgorithmType type) {
        super(countOfPoint, probabilityCrossover, typeSelectionParents, typeSelectionSurvival, length, type);
        population = new PMonoid(size, length);
        individualWithMaximalFitness = population.maximalFitness();
        maximalFitness = individualWithMaximalFitness.calcFitness();
        sSelector = new SSMonoid();
    }

    public List<Integer> evalPopulation() {
        return population.calculateFitness();
    }

    public int getMaximalFitness() {
        return maximalFitness;
    }

    @Override
    protected void greedyMGA() throws GAException {
        List<Individual> parents = population.getMaximal(2);
        byte[] c = uniformCrossover(parents.stream().map(wrap(i -> i.getGenom(0))).collect(Collectors.toList()));
        byte[] z = SBM(c);
        List<Individual> p = population.getPopulation();
        Individual i = new IMonoid(z, parents.get(0).getChanged(0));
        if (!p.contains(i) && i.calcFitness() >= p.get(p.size() - 1).calcFitness()) {
            p.set(p.size() - 1, i);
        }
        population = new PMonoid(p);
        updateMaximalFitness();
    }

    @Override
    protected void randomLocalSearch() throws GAException {
        int size = population.getSize();
        List<IMonoid> children = new ArrayList<>();
        population.deleteConstant();
        for (Individual ind : pSelector.select(population, 1, typeSelectionParents)) {
            IMonoid i = new IMonoid(ind.getGenom(0), ind.getChanged(0));
            int index = ThreadLocalRandom.current().nextInt(length);
            while (!i.inverseGene(index)) {
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

    @Override
    protected void standardBitMutation() throws GAException {
        List<IMonoid> children = new ArrayList<>();
        for (Individual ind : pSelector.select(population, 1, typeSelectionParents)) {
            byte[] b = ind.getGenom(0);
            byte[] child = SBM(b);
            IMonoid i = new IMonoid(child, ind.getChanged(0));
            if (i.calcFitness() > ind.calcFitness()) {
                children.add(i);
            }
        }
        population = sSelector.select(population, children, typeSelectionSurvival, population.getSize());
        population.incrementAges();
        evalPopulation();
        updateMaximalFitness();
    }
}
