package geneticalgorithms;

import individuals.Individual;
import parentselectors.ParentSelector;
import parentselectors.TypeSelectionParents;
import populations.Population;
import survivalselectors.SurvivalSelector;
import survivalselectors.TypeSelectionSurvival;

import java.util.List;
import java.util.function.Function;

public abstract class GeneticAlgorithm {
    protected int countOfPoint;
    protected double probabilityCrossover;
    protected Population population;
    protected ParentSelector pSelector = new ParentSelector();
    protected SurvivalSelector sSelector;
    protected TypeSelectionParents typeSelectionParents;
    protected TypeSelectionSurvival typeSelectionSurvival;
    protected int maximalFitness;
    protected Individual individualWithMaximalFitness;
    protected double standardProbability;
    protected int length;
    protected AlgorithmType type;

    public GeneticAlgorithm(int countOfPoint, double probabilityCrossover, TypeSelectionParents typeSelectionParents,
                            TypeSelectionSurvival typeSelectionSurvival, int length, AlgorithmType type) {
        this.countOfPoint = countOfPoint;
        this.probabilityCrossover = probabilityCrossover;
        this.typeSelectionParents = typeSelectionParents;
        this.typeSelectionSurvival = typeSelectionSurvival;
        this.standardProbability = 1 / (double) length;
        this.length = length;
        this.type = type;
    }

    public boolean isTerminated(int maxValue) {
        return maxValue == maximalFitness;
    }

    public List<Integer> evalPopulation() {
        return population.calculateFitness();
    }

    public int getMaximalFitness() {
        return maximalFitness;
    }

    protected void updateMaximalFitness() {
        Individual individual = population.maximalFitness();
        if (individual.calcFitness() > maximalFitness) {
            maximalFitness = individual.calcFitness();
            individualWithMaximalFitness = individual;
        }
    }

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
        }
    }

    protected abstract void greedyMGA() throws GAException;

    protected abstract void standardBitMutation() throws GAException;

    protected abstract void randomLocalSearch() throws GAException;

    public static <T,R> Function<T,R> wrap(CheckedFunction<T,R> checkedFunction) {
        return t -> {
            try {
                return checkedFunction.apply(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    protected byte[] SBM(byte[] b) {
        byte[] child = new byte[b.length];
        int l = 0;
        for (int i = 0; i < b.length; i++) {
            if (Math.random() < standardProbability) {
                child[i] = (byte) (1 - b[i]);
                l++;
            } else {
                child[i] = b[i];
            }
        }
//        if (l == 0) {
//            int index = ThreadLocalRandom.current().nextInt(b.length);
//            child[index] = (byte) (1 - b[index]);
//        }
        return child;
    }

    protected byte[] uniformCrossover(List<byte[]> parents) throws GAException {
        if (parents.size() != 2) {
            throw new GAException("Sorry, expected two parents");
        }
        int length = parents.get(0).length;
        byte[] c0 = new byte[length];
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
