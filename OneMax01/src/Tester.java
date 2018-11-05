import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.*;

public class Tester {
    public static void main(String[] args) {
        try {
            //only mutation
            /*for (int i = 1; i <= 5; i++) {
                System.out.println("Trying: " + i);
                GeneticAlgoritm ga = new GeneticAlgoritm(100, 50, -1, 0, 3, 1);
                ga.evalPopulation();
                int generations = 1;
                while (!ga.isTerminated(50)) {
                    ga.mutation();
                    ga.evalPopulation();
                    generations++;
                }
                System.out.println("Generations: " + generations);
            }*/
            //one point crossover
            /*for (int i = 1; i <= 5; i++) {
                System.out.println("Trying: " + i);
                GeneticAlgoritm ga = new GeneticAlgoritm(100, 50, 0, 0.9, 3, 1);
                ga.evalPopulation();
                int generations = 1;
                while (!ga.isTerminated(50)) {
                    //System.out.println(ga.getPopulation().maximalFitness());
                    ga.crossover(1);
                    ga.evalPopulation();
                    generations++;
                }
                System.out.println("Generations: " + generations);
            }*/
            //mutation and crossover
            for (int i = 1; i <= 5; i++) {
                System.out.println("Trying: " + i);
                GeneticAlgoritm ga = new GeneticAlgoritm(100, 50, 24, 0.5, 3, 1);
                ga.evalPopulation();
                int generations = 1;
                while (!ga.isTerminated(50)) {
                    //System.out.println(ga.getPopulation().maximalFitness());
                    ga.crossoverAndMutation(1);
                    ga.evalPopulation();
                    generations++;
                }
                System.out.println("Generations: " + generations);
            }
        } catch (GAException e) {
            System.out.println(e.getMessage());
        }
    }
}
