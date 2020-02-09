package parentselectors;

import geneticalgorithms.GAException;
import individuals.Individual;
import populations.Population;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ParentSelector {
    public List<Individual> select(Population population, int count, TypeSelectionParents type) throws GAException {
        switch (type) {
            case RWS:
                return select0(population, count);
            case SUS:
                return select1(population, count);
            case TS:
                return select2(population, count);
            case RS:
                return select3(population, count);
            default:
                throw new GAException("Sorry, " + type + " incorrect type");

        }
    }

    private List<Individual> select01(Population population, int count, boolean notRepeat) {
        List<Integer> rank = new ArrayList<>();
        Map<Integer, Individual> genoms = new HashMap<>();
        int sum = 0;
        int number = 0;
        for (Individual i : population.getPopulation()) {
            sum += i.calcFitness();
            rank.add(sum);
            genoms.put(number, i);
            number++;
        }
        List<Integer> prevIndexes = new ArrayList<>();
        List<Individual> parents = new ArrayList<>();
        for (int k = 0; k < count; k++) {
            int index = Collections.binarySearch(rank, ThreadLocalRandom.current().nextInt(0, sum + 1));
            if (index < 0) {
                index += 1;
                index *= -1;
            }
            while (notRepeat && prevIndexes.contains(index)) {
                index = Collections.binarySearch(rank, ThreadLocalRandom.current().nextInt(0, sum + 1));
                if (index < 0) {
                    index += 1;
                    index *= -1;
                }
            }
            prevIndexes.add(index);
            parents.add(genoms.get(index));
        }
        return parents;
    }

    private List<Individual> select0(Population population, int count) {
        return select01(population, count, false);
    }

    private List<Individual> select1(Population population, int count) {
        return select01(population, count, true);
    }

    private List<Individual> select2(Population population, int count) {
        List<Individual> parents = new ArrayList<>();
        Byte[] a = {0};
        for (int k = 0; k < count; k++) {
            List<Individual> temp = new ArrayList<>();
            for (int j = 0; j < population.getSize() / 10; j++) {
                temp.add(population.getIndividual(ThreadLocalRandom.current().nextInt(population.getSize())));
            }
            parents.add(temp.stream().max(Comparator.comparing(Individual::calcFitness)).orElse(null));
        }
        return parents;
    }

    private List<Individual> select3(Population population, int count) {
        List<Integer> rank = new ArrayList<>();
        Map<Integer, Individual> genoms = new HashMap<>();
        int sum = 0;
        int number = 0;
        int length = 0;
        int prevFitness = -1;
        for (Individual i : population.getPopulation()) {
            if (i.calcFitness() != prevFitness) {
                length++;
            }
            sum += length;
            rank.add(sum);
            genoms.put(number, i);
            number++;
        }
        List<Individual> parents = new ArrayList<>();
        for (int k = 0; k < count; k++) {
            int index = Collections.binarySearch(rank, ThreadLocalRandom.current().nextInt(0, sum + 1));
            if (index < 0) {
                index += 1;
                index *= -1;
            }
            parents.add(genoms.get(index));
        }
        return parents;
    }
}
