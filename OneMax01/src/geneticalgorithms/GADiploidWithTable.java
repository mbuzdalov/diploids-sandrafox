package geneticalgorithms;

import individuals.IDiploidWithTable;
import individuals.Individual;
import parentselectors.TypeSelectionParents;
import populations.PDiploidWithTable;
import survivalselectors.SSDiploidWithTable;
import survivalselectors.TypeSelectionSurvival;
import util.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GADiploidWithTable extends GeneticAlgorithm {

    public GADiploidWithTable(int size, int length, int countOfPoint, double probabilityCrossover,
                              TypeSelectionParents typeSelectionParents, TypeSelectionSurvival typeSelectionSurvival,
                              AlgorithmType type, int[] vector, double probabilitySBM) {
        super(countOfPoint, probabilityCrossover, typeSelectionParents, typeSelectionSurvival, length, type);
        population = new PDiploidWithTable(size, length, vector);
        individualWithMaximalFitness = population.maximalFitness();
        maximalFitness = individualWithMaximalFitness.calcFitness();
        sSelector = new SSDiploidWithTable();
        standardProbability = probabilitySBM;
    }

    @Override
    public void newGeneration() throws GAException {
        switch (type) {
            case RLS:
                randomLocalSearch();
                break;
            case SBM:
                standardBitMutation();
                break;
            case GREEDY:
                greedyMGA();
                break;
            case GREEDYMOD:
                greedyMGAMod();
                break;
        }
    }

    protected void greedyMGAMod() throws GAException {
        List<Individual> parents = population.getMaximal(2);
        IDiploidWithTable p1 = (IDiploidWithTable) parents.get(0), p2 = (IDiploidWithTable) parents.get(1);
        byte[][] newGenoms0 = uniformCrossoverTwo(Lists.of(p1.getGenom(0), p1.getGenom(1)));
        byte[][] newGenoms1 = uniformCrossoverTwo(Lists.of(p2.getGenom(0), p2.getGenom(1)));
        //int firstGamete = ThreadLocalRandom.current().nextInt(4);
        //int secondGamete = ThreadLocalRandom.current().nextInt(4);
        //Byte[] gamete0, gamete1;
        Individual i = new IDiploidWithTable(
                SBM(p1.moreLikely(new byte[][]{p1.getGenom(0), p1.getGenom(1), newGenoms0[0], newGenoms0[1]})),
                SBM(p2.moreLikely(new byte[][]{p2.getGenom(0), p2.getGenom(1), newGenoms1[0], newGenoms1[1]})),
                p1.getVector());
        List<Individual> p = new ArrayList<>(population.getPopulation());
        if (!p.contains(i) && i.calcFitness() > p.get(p.size() - 1).calcFitness()) {
            p.set(p.size() - 1, i);
        }
        population = new PDiploidWithTable(p);
        evalPopulation();
        updateMaximalFitness();
        //System.out.println(maximalFitness);
    }

    @Override
    protected void greedyMGA() throws GAException {
        List<Individual> parents = population.getMaximal(2);
        byte[][] newGenoms0 = uniformCrossoverTwo(Lists.of(parents.get(0).getGenom(0), parents.get(0).getGenom(1)));
        byte[][] newGenoms1 = uniformCrossoverTwo(Lists.of(parents.get(1).getGenom(0), parents.get(1).getGenom(1)));
        int firstGamete = ThreadLocalRandom.current().nextInt(4);
        int secondGamete = ThreadLocalRandom.current().nextInt(4);
        byte[] gamete0, gamete1;
        if (firstGamete < 2) {
            gamete0 = parents.get(0).getGenom(firstGamete);
        } else {
            gamete0 = newGenoms0[firstGamete - 2];
        }
        if (secondGamete < 2) {
            gamete1 = parents.get(1).getGenom(secondGamete);
        } else {
            gamete1 = newGenoms1[secondGamete - 2];
        }
        Individual i = new IDiploidWithTable(SBM(gamete0), SBM(gamete1), ((IDiploidWithTable) parents.get(0)).getVector());
        // Change: was (population.getPopulation()), but it is unsorted. This seems to clash with the logic below
        List<Individual> p = new ArrayList<>(parents);
        // >= since we want to escape from plateaus
        if (!p.contains(i) && i.calcFitness() >= p.get(p.size() - 1).calcFitness()) {
            p.set(p.size() - 1, i);
        }
        population = new PDiploidWithTable(p);
        evalPopulation();
        updateMaximalFitness();
    }

    private byte[][] uniformCrossoverTwo(List<byte[]> parents) throws GAException {
        if (parents.size() != 2) {
            throw new GAException("Sorry, expected two parents");
        }
        int length = parents.get(0).length;
        byte[][] c = new byte[2][length];
        for (int i = 0; i < length; i++) {
            if (probabilityCrossover < Math.random()) {
                c[0][i] = parents.get(1)[i];
                c[1][i] = parents.get(0)[i];
            } else {
                c[0][i] = parents.get(0)[i];
                c[1][i] = parents.get(1)[i];
            }
        }
        return c;
    }

    @Override
    protected void standardBitMutation() throws GAException {
        List<IDiploidWithTable> children = new ArrayList<>();
        List<Individual> inds = pSelector.select(population, 1, typeSelectionParents);
        for (Individual ind : inds) {
            IDiploidWithTable i = new IDiploidWithTable(SBM(ind.getGenom(0)), SBM(ind.getGenom(1)),
                    ((IDiploidWithTable) ind).getVector());
            if (i.calcFitness() >= ind.calcFitness()) {
                children.add(i);
            }
        }
        population = sSelector.select(population, children, typeSelectionSurvival, population.getSize());
        population.incrementAges();
        evalPopulation();
        updateMaximalFitness();
    }

    @Override
    protected void randomLocalSearch() throws GAException {
        throw new GAException("This method isn't support in this version of Genetic Algorithm");
    }
}
