package geneticalgorithms;

import individuals.Individual;
import parentselectors.TypeSelectionParents;
import individuals.IDiploidWithDominance;
import populations.PDiploidWithDominance;
import survivalselectors.SSDiploidWithDominance;
import survivalselectors.TypeSelectionSurvival;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class GADiploidWithDominance extends GeneticAlgorithm {
    private DominanceType dt;

    public GADiploidWithDominance(int size, int length, int countOfPoint, double probabilityCrossover,
                                  TypeSelectionParents typeSelectionParents, TypeSelectionSurvival typeSelectionSurvival,
                                  DominanceType dt, AlgorithmType type) {
        super(countOfPoint, probabilityCrossover, typeSelectionParents, typeSelectionSurvival, length, type);
        population = new PDiploidWithDominance(size, length, dt);
        individualWithMaximalFitness = population.maximalFitness();
        maximalFitness = individualWithMaximalFitness.calcFitness();
        sSelector = new SSDiploidWithDominance();
        this.dt = dt;
    }

    @Override
    protected void greedyMGA() throws GAException {
        List<Individual> parents = population.getMaximal(2);
        Byte[] c1 = uniformCrossover(parents.stream().map(wrap(i -> i.getGenom(0))).collect(Collectors.toList()));
        Byte[] z1 = SBM(c1);
        Byte[] c2 = uniformCrossover(parents.stream().map(wrap(i -> i.getGenom(1))).collect(Collectors.toList()));
        Byte[] z2 = SBM(c2);
        List<Individual> p = new ArrayList<>(population.getPopulation());
        IDiploidWithDominance i = new IDiploidWithDominance(z1, z2, parents.get(0).getChanged(0), parents.get(0).getChanged(1), dt);
        if (!p.contains(i) && i.calcFitness() > p.get(p.size() - 1).calcFitness()) {
            p.set(p.size() - 1, i);
        }
        population = new PDiploidWithDominance(p);
        updateMaximalFitness();
    }

    @Override
    protected void randomLocalSearch() throws GAException {
        int size = population.getSize();
        List<IDiploidWithDominance> children = new ArrayList<>();
        population.deleteConstant();
        for (Individual ind : pSelector.select(population, 1, typeSelectionParents)) {
            IDiploidWithDominance i = new IDiploidWithDominance(ind.getGenom(0),
                    ind.getGenom(0),
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

    @Override
    protected void standardBitMutation() throws GAException {
        List<IDiploidWithDominance> children = new ArrayList<>();
        for (Individual ind : pSelector.select(population, population.getSize()/10, typeSelectionParents)) {
            Byte[] b1 = ind.getGenom(0);
            Byte[] b2 = ind.getGenom(1);
            Byte[] child1 = SBM(b1);
            Byte[] child2 = SBM(b2);
            IDiploidWithDominance i = new IDiploidWithDominance(child1, child2, ind.getChanged(0), ind.getChanged(1), dt);
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
