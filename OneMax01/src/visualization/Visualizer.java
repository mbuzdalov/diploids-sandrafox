package visualization;

import geneticalgorithmdiploid.DominanceType;
import geneticalgorithmmonoid.GAException;
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
        GeneticAlgoritm ga;
        int generations;
        final XYSeries mutation = new XYSeries( "RLS" );
        for (int i = 0; i < 5; i++) {
            ga = new GeneticAlgoritm(50, 50, -1, 0, 2, 1);
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
            ga = new GeneticAlgoritm(50, 50, -1, 0.5, 2, 1);
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
        geneticalgorithmdiploid.GeneticAlgoritm ga;
        int generations;
        /*final XYSeries and = new XYSeries( "andSBM" );
        for (int i = 0; i < 5; i++) {
            ga = new geneticalgorithmdiploid.GeneticAlgoritm(50, 50, -1, 0, 2, 1, DominanceType.AND);
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
        final XYSeries or = new XYSeries( "orSBM");
        for (int i = 0; i < 5; i++) {
            ga = new geneticalgorithmdiploid.GeneticAlgoritm(1, 50, -1, 0, 1, 1, DominanceType.OR);
            ga.evalPopulation();
            generations = 1;
            while (!ga.isTerminated(50)) {
                //System.out.println(ga.getMaximalFitness());
                or.add(generations, ga.getMaximalFitness());
                ga.standardBitMutation();
                generations++;
            }
            System.out.println(generations);
            or.add(generations, 50);
        }
        final XYSeries mono = new XYSeries("monoSBM");
        geneticalgorithmmonoid.GeneticAlgoritm ga1;
        for (int i = 0; i < 5; i++) {
            ga1 = new geneticalgorithmmonoid.GeneticAlgoritm(1, 50, -1, 0, 1, 1);
            ga1.evalPopulation();
            generations = 1;
            while (!ga1.isTerminated(50)) {
                //System.out.println(ga.getMaximalFitness());
                mono.add(generations, ga1.getMaximalFitness());
                ga1.standardBitMutation();
                generations++;
            }
            System.out.println(generations);
            mono.add(generations, 50);
        }
        final XYSeries delta = new XYSeries( "deltaSBM" );
        for (int i = 0; i < 5; i++) {
            ga = new geneticalgorithmdiploid.GeneticAlgoritm(1, 50, -1, 0, 1, 1, DominanceType.DELTA);
            ga.evalPopulation();
            generations = 1;
            while (!ga.isTerminated(50)) {
                //System.out.println(ga.getMaximalFitness());
                delta.add(generations, ga.getMaximalFitness());
                ga.standardBitMutation();
                generations++;
            }
            //System.out.println(generations);
            delta.add(generations, 50);
        }
        final XYSeriesCollection dataset = new XYSeriesCollection( );
        dataset.addSeries(delta);
        dataset.addSeries(or);
        dataset.addSeries(mono);
        //dataset.addSeries(and);
        return dataset;
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
            File XYChart = new File( "SBMmono&diploid2.jpeg" );
            ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);
        } catch (GAException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
