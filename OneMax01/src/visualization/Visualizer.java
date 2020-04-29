package visualization;

import geneticalgorithms.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import parentselectors.TypeSelectionParents;
import survivalselectors.TypeSelectionSurvival;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;

public class Visualizer extends ApplicationFrame {

    public Visualizer( String applicationTitle, String chartTitle ) throws GAException {
        super(applicationTitle);
        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                chartTitle ,
                "Steps" ,
                "Fitness" ,
                createDataset() ,
                PlotOrientation.VERTICAL ,
                true , true , false);

        ChartPanel chartPanel = new ChartPanel( xylineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
        final XYPlot plot = xylineChart.getXYPlot( );

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
        renderer.setSeriesPaint( 0 , Color.RED );
        renderer.setSeriesPaint( 1 , Color.GREEN );
        renderer.setSeriesPaint( 2 , Color.YELLOW );
        renderer.setSeriesPaint(3, Color.BLUE);
        renderer.setSeriesStroke( 0 , new BasicStroke( 4.0f ) );
        renderer.setSeriesStroke( 1 , new BasicStroke( 3.0f ) );
        renderer.setSeriesStroke( 2 , new BasicStroke( 2.0f ) );
        renderer.setSeriesStroke(5, new BasicStroke(1.0f));
        plot.setRenderer( renderer );
        setContentPane( chartPanel );
    }

    public static XYDataset createDataset( ) throws GAException {
        /* monoid
        GAMonoid ga;
        int generations;
        final XYSeries mutation = new XYSeries( "RLS" );
        for (int i = 0; i < 5; i++) {
            ga = new GAMonoid(50, 50, -1, 0, 2, 1);
            ga.evalPopulation();
            generations = 1;
            while (!ga.isTerminated(50)) {
                //System.out.println(ga.getMaximalFitness());
                mutation.add(generations, ga.getMaximalFitness());
                ga.randomLocalSearch();
                generations++;
            }
            //System.out.println(generations);
            mutation.add(generations, 50);
        }

        final XYSeries mcrossover = new XYSeries( "greedy(m + 1)" );
        for (int i = 0; i < 5; i++) {
            ga = new GAMonoid(50, 50, -1, 0.5, 2, 1);
            ga.evalPopulation();
            generations = 1;
            while (!ga.isTerminated(50)) {
                //System.out.println(ga.getMaximalFitness());
                mcrossover.add(generations, ga.getMaximalFitness());
                ga.greedyMGA();
                generations++;
            }
            System.out.println(generations);
            mcrossover.add(generations, 50);
        }

        final XYSeriesCollection dataset = new XYSeriesCollection( );
        dataset.addSeries( mutation );
        dataset.addSeries(mcrossover);
        return dataset;

         */
        /*final XYSeries and = new XYSeries( "andSBM" );
        for (int i = 0; i < 5; i++) {
            ga = new geneticalgorithmdiploid.GAMonoid(50, 50, -1, 0, 2, 1, geneticalgorithms.DominanceType.AND);
            ga.evalPopulation();
            generations = 1;
            while (!ga.isTerminated(50)) {
                //System.out.println(ga.getMaximalFitness());
                and.add(generations, ga.getMaximalFitness());
                ga.randomLocalSearch();
                generations++;
            }
            //System.out.println(generations);
            and.add(generations, 50);
        }
         */
        final XYSeries ean = new XYSeries( "(1 + 1)1/N");
        final XYSeries ea2n = new XYSeries( "(1 + 1)1/2N" );
        final XYSeries gan = new XYSeries("(2 + 1)1/N");
        final XYSeries ga2n = new XYSeries("(2 + 1)1/2N");

        final int nRuns = 10;
        for (int N = 100; N <= 1000; N += 100) {
            final int n = N;
            addGenerationsToDataSet(() -> new GADiploidWithTable(1, n, -1, 0.5, TypeSelectionParents.SUS,
                    TypeSelectionSurvival.FITNESS, AlgorithmType.SBM, generateVector(n, 0.1), 1.0 / n),
                    ean, nRuns, 2 * n);
            //addInfoToDataSet(ga, ean, 2 * N);
            addGenerationsToDataSet(() -> new GADiploidWithTable(1, n, -1, 0.5, TypeSelectionParents.SUS,
                    TypeSelectionSurvival.FITNESS, AlgorithmType.SBM, generateVector(n, 0.1), 0.5 / n),
                    ea2n, nRuns, 2 * n);
            //addInfoToDataSet(ga, ea2n, 2 * N);
            addGenerationsToDataSet(() -> new GADiploidWithTable(2, n, -1, 0.5, TypeSelectionParents.SUS,
                    TypeSelectionSurvival.FITNESS, AlgorithmType.GREEDY, generateVector(n, 0.1), 1.0 / n),
                    gan, nRuns, 2 * n);
            addGenerationsToDataSet(() -> new GADiploidWithTable(2, n, -1, 0.5, TypeSelectionParents.SUS,
                    TypeSelectionSurvival.FITNESS, AlgorithmType.GREEDY, generateVector(n, 0.1), 0.5 / n),
                    ga2n, nRuns, 2 * n);
            //addInfoToDataSet(ga, gan, 2 * N);
//            ga = new GADiploidWithTable(2, N, -1, 0.5, TypeSelectionParents.SUS,
//                    TypeSelectionSurvival.FITNESS, AlgorithmType.GREEDYMOD, generateVector(N, 0.1),
//                    1 / (double)(N));
            //addGenerationsToDataSet(ga, ean, 2 * N);
//            ga = new GADiploidWithTable(2, N, -1, 0.5, TypeSelectionParents.SUS,
//                    TypeSelectionSurvival.FITNESS, AlgorithmType.GREEDYMOD, generateVector(N, 0.1),
//                    1 / (double)(2 * N));
            //addGenerationsToDataSet(ga, ea2n, 2 * N);
        }

        final XYSeriesCollection dataset = new XYSeriesCollection( );
        dataset.addSeries(ean);
        dataset.addSeries(ea2n);
        dataset.addSeries(gan);
        dataset.addSeries(ga2n);
        //dataset.addSeries(and);
        return dataset;
    }

    private static int[] generateVector(int length, double probabilityHe) {
        double probability0 = probabilityHe + (1 - probabilityHe) / 2;
        int[] vector = new int[length];
        for (int i = 0; i < length; ++i) {
            double p = Math.random();
            if (p <= probabilityHe) {
                vector[i] = 1;
            } else if (p <= probability0) {
                vector[i] = 0;
            } else {
                vector[i] = 2;
            }
        }
        return vector;
    }

    private static void addInfoToDataSet(GeneticAlgorithm ga, XYSeries s, int maxValue) throws GAException {
        ga.evalPopulation();
        int generations = 1;
        while (!ga.isTerminated(maxValue)) {
            //System.out.println(ga.getMaximalFitness());
            s.add(generations, ga.getMaximalFitness());
            ga.newGeneration();
            generations++;
        }
        System.out.println(generations);
        s.add(generations, ga.getMaximalFitness());
    }

    private static void addGenerationsToDataSet(Supplier<GeneticAlgorithm> gaSup, XYSeries s, int nRuns, int maxValue) throws GAException {
        double sumGenerations = 0;
        for (int i = 0; i < nRuns; ++i) {
            GeneticAlgorithm ga = gaSup.get();
            ga.evalPopulation();
            int generations = 1;
            while ((!ga.isTerminated(maxValue))) {
                ga.newGeneration();
                generations++;
            }
            System.out.println(generations);
            sumGenerations += generations;
        }
        s.add(maxValue, sumGenerations / nRuns);
    }

    public static void main( String[ ] args ) {
        try {
            JFreeChart xylineChart = ChartFactory.createXYLineChart(
                    "Genetic Algorithms progress",
                    "Problem size",
                    "Average generations",
                    Visualizer.createDataset(),
                    PlotOrientation.VERTICAL,
                    true, true, false);

            int width = 1000;   /* Width of the image */
            int height = 800;  /* Height of the image */
            File XYChart = new File( "VectorWith0.1He2.jpeg" );
            ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);
        } catch (GAException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
}