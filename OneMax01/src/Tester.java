public class Tester {
    public static void main(String[] args) {
        /*try {
            //only mutation
            /*for (int i = 1; i <= 5; i++) {
                System.out.println("Trying: " + i);
                geneticalgorithms.GAMonoid ga = new geneticalgorithms.GAMonoid(100, 50, -1, 0, 3, 1);
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
                geneticalgorithms.GAMonoid ga = new geneticalgorithms.GAMonoid(100, 50, 0, 0.9, 3, 1);
                ga.evalPopulation();
                int generations = 1;
                while (!ga.isTerminated(50)) {
                    //System.out.println(ga.getPMonoid().maximalFitness());
                    ga.crossover(1);
                    ga.evalPopulation();
                    generations++;
                }
                System.out.println("Generations: " + generations);
            }*/
            //mutation and crossover
            /*for (int i = 1; i <= 5; i++) {
                System.out.println("Trying: " + i);
                GAMonoid ga = new GAMonoid(100, 50, 24, 0.5, 3, 1);
                ga.evalPopulation();
                int generations = 1;
                while (!ga.isTerminated(50)) {
                    //System.out.println(ga.getMaximalFitness);
                    ga.crossoverAndMutation(1);
                    ga.evalPopulation();
                    generations++;
                }
                System.out.println("Generations: " + generations);
            }
        } catch () {
            System.out.println(0);
        }*/
    }
}
