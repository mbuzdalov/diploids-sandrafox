package geneticalgorithms;

import individuals.IDiploidWithTable;
import individuals.Individual;
import parentselectors.TypeSelectionParents;
import populations.PDiploidWithAverage;
import populations.PDiploidWithTable;
import survivalselectors.SSDiploidWithAverage;
import survivalselectors.SSDiploidWithTable;
import survivalselectors.TypeSelectionSurvival;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GADiploidWithTable extends GeneticAlgorithm {

    public GADiploidWithTable(int size, int length, int countOfPoint, double probabilityCrossover,
                              TypeSelectionParents typeSelectionParents, TypeSelectionSurvival typeSelectionSurvival,
                              AlgorithmType type, int[] vector, double probabiltySBM) {
        super(countOfPoint, probabilityCrossover, typeSelectionParents, typeSelectionSurvival, length, type);
        population = new PDiploidWithTable(size, length, vector);
        individualWithMaximalFitness = population.maximalFitness();
        maximalFitness = individualWithMaximalFitness.calcFitness();
        sSelector = new SSDiploidWithTable();
        standardProbability = probabiltySBM;
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
        Byte[][] newGenoms0 = uniformCrossoverTwo(List.of(p1.getGenom(0), p1.getGenom(1)));
        Byte[][] newGenoms1 = uniformCrossoverTwo(List.of(p2.getGenom(0), p2.getGenom(1)));
        int firstGamete = ThreadLocalRandom.current().nextInt(4), secondGamete = ThreadLocalRandom.current().nextInt(4);
        Byte[] gamete0, gamete1;
        Individual i = new IDiploidWithTable(
                SBM(p1.moreLikely(new Byte[][]{p1.getGenom1(), p1.getGenom2(), newGenoms0[0], newGenoms0[1]})),
                SBM(p2.moreLikely(new Byte[][]{p2.getGenom1(), p2.getGenom2(), newGenoms1[0], newGenoms1[1]})),
                p1.getChanged(0),
                p2.getChanged(secondGamete % 2), p1.getVector());
        List<Individual> p = new ArrayList(population.getPopulation());
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
        Byte[][] newGenoms0 = uniformCrossoverTwo(List.of(parents.get(0).getGenom(0), parents.get(0).getGenom(1)));
        Byte[][] newGenoms1 = uniformCrossoverTwo(List.of(parents.get(1).getGenom(0), parents.get(1).getGenom(1)));
        int firstGamete = ThreadLocalRandom.current().nextInt(4), secondGamete = ThreadLocalRandom.current().nextInt(4);
        Byte[] gamete0, gamete1;
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
        Individual i = new IDiploidWithTable(SBM(gamete0), SBM(gamete1), parents.get(0).getChanged(firstGamete % 2),
                parents.get(1).getChanged(secondGamete % 2), ((IDiploidWithTable) parents.get(0)).getVector());
        List<Individual> p = new ArrayList(population.getPopulation());
        if (!p.contains(i) && i.calcFitness() > p.get(p.size() - 1).calcFitness()) {
            p.set(p.size() - 1, i);
        }
        population = new PDiploidWithTable(p);
        evalPopulation();
        updateMaximalFitness();
    }

    private Byte[][] uniformCrossoverTwo(List<Byte[]> parents) throws GAException {
        if (parents.size() != 2) {
            throw new GAException("Sorry, expected two parents");
        }
        int length = parents.get(0).length;
        Byte[][] c = new Byte[2][length];
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
        for (int j = 0; j < inds.size(); j++) {
            IDiploidWithTable i = new IDiploidWithTable(SBM(inds.get(j).getGenom(0)), SBM(inds.get(j).getGenom(1)),
                    inds.get(j).getChanged(0), inds.get(j).getChanged(1), ((IDiploidWithTable) inds.get(j)).getVector());
            if (i.calcFitness() > inds.get(j).calcFitness()) {
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