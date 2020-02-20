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
        GeneticAlgorithm ga;
        int generations;
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
        final XYSeries or = new XYSeries( "GreedyMono");
        final XYSeries delta = new XYSeries( "MutationDiploid" );
        final XYSeries mono = new XYSeries("SBMMono");
        for (int m = 50; m <= 1000; m += 50) {
            for (int i = 0; i < 5; i++) {
                ga = new GAMonoid(2, m, -1, 0.5, TypeSelectionParents.SUS, TypeSelectionSurvival.FITNESS, AlgorithmType.GREEDY);
                addGenerationsToDataSet(ga, or, m);
                ga = new GADiploidCycleWithAverage(2, m, -1, 0.5, TypeSelectionParents.SUS, TypeSelectionSurvival.FITNESS, AlgorithmType.SBM);
                addGenerationsToDataSet(ga, delta, m);
                ga = new GAMonoid(1, m, -1, 0.5, TypeSelectionParents.SUS, TypeSelectionSurvival.FITNESS, AlgorithmType.SBM);
                addGenerationsToDataSet(ga, mono, m);
            }
        }
        /*final XYSeries mono = new XYSeries("RLS");
        for (int i = 0; i < 5; i++) {
            ga = new GADiploidCycleWithAverage(2, 50, -1, 0, TypeSelectionParents.SUS, TypeSelectionSurvival.FITNESS, AlgorithmType.RLS);
            addInfoToDataSet(ga, mono);
        }*/

        /*for (int i = 0; i < 5; i++) {
            ga = new GADiploidCycleWithAverage(2, 50, -1, 0.5, TypeSelectionParents.SUS, TypeSelectionSurvival.FITNESS, AlgorithmType.GREEDY);
            addInfoToDataSet(ga, delta);
        }*/
        final XYSeriesCollection dataset = new XYSeriesCollection( );
        dataset.addSeries(delta);
        dataset.addSeries(or);
        dataset.addSeries(mono);
        //dataset.addSeries(and);
        return dataset;
    }

    private static void addInfoToDataSet(GeneticAlgorithm ga, XYSeries s) throws GAException {
        ga.evalPopulation();
        int generations = 1;
        while (!ga.isTerminated(50) && generations <= 1000000) {
            //System.out.println(ga.getMaximalFitness());
            s.add(generations, ga.getMaximalFitness());
            ga.newGeneration();
            generations++;
        }
        System.out.println(generations);
        s.add(generations, ga.getMaximalFitness());
    }

    private static void addGenerationsToDataSet(GeneticAlgorithm ga, XYSeries s, int maxValue) throws GAException {
        ga.evalPopulation();
        int generations = 1;
        while ((!ga.isTerminated(maxValue))) {
            //System.out.println(ga.getMaximalFitness());
            ga.newGeneration();
            generations++;
        }
        System.out.println(generations);
        s.add(generations, ga.getMaximalFitness());
    }

    public static void main( String[ ] args ) {
        try {
            JFreeChart xylineChart = ChartFactory.createXYLineChart(
                    "Genetic Algorithms progress",
                    "Generations",
                    "Score",
                    Visualizer.createDataset(),
                    PlotOrientation.VERTICAL,
                    true, true, false);

            int width = 1000;   /* Width of the image */
            int height = 800;  /* Height of the image */
            File XYChart = new File( "CycleV2.jpeg" );
            ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);
        } catch (GAException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
}