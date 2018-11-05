import java.awt.Color;
import java.awt.BasicStroke;
import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

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
        GeneticAlgoritm ga;
        int generations;
        final XYSeries mutation = new XYSeries( "Mutation" );
        for (int i = 0; i < 5; i++) {
            ga = new GeneticAlgoritm(100, 50, -1, 0, 2, 1);
            ga.evalPopulation();
            generations = 1;
            while (!ga.isTerminated(50)) {
                mutation.add(generations, ga.getPopulation().maximalFitness());
                ga.mutation();
                ga.evalPopulation();
                generations++;
            }
            mutation.add(generations, 50);
        }

        final XYSeries crossover = new XYSeries( "Crossover with 0.5 ptobability" );
        /*for (int i = 0; i < 5; i++) {
            ga = new GeneticAlgoritm(100, 50, 0, 0.5, 0, 1);
            ga.evalPopulation();
            generations = 1;
            while (!ga.isTerminated(50)) {
                crossover.add(generations, ga.getPopulation().maximalFitness());
                ga.crossover(1);
                ga.evalPopulation();
                generations++;
            }
            crossover.add(generations, 50);
        }*/

        final XYSeries mutationAndCrossover = new XYSeries( "Mutation and one-point crossover" );
        for (int i = 0; i < 5; i++) {
            ga = new GeneticAlgoritm(100, 50, 1, 0.5, 2, 1);
            ga.evalPopulation();
            generations = 1;
            while (!ga.isTerminated(50)) {
                mutationAndCrossover.add(generations, ga.getPopulation().maximalFitness());
                ga.crossoverAndMutation(0);
                ga.evalPopulation();
                generations++;
            }
            mutationAndCrossover.add(generations, 50);
        }

        final XYSeries mutationAndCrossoverP = new XYSeries( "Mutation and 0.5 crossover" );
        for (int i = 0; i < 5; i++) {
            ga = new GeneticAlgoritm(100, 50, 1, 0.5, 2, 1);
            ga.evalPopulation();
            generations = 1;
            while (!ga.isTerminated(50)) {
                mutationAndCrossoverP.add(generations, ga.getPopulation().maximalFitness());
                ga.crossoverAndMutation(1);
                ga.evalPopulation();
                generations++;
            }
            mutationAndCrossoverP.add(generations, 50);
        }

        final XYSeriesCollection dataset = new XYSeriesCollection( );
        dataset.addSeries( mutation );
        dataset.addSeries( crossover );
        dataset.addSeries( mutationAndCrossover );
        dataset.addSeries(mutationAndCrossoverP);
        return dataset;
    }

    public static void main( String[ ] args ) {
        try {
            JFreeChart xylineChart = ChartFactory.createXYLineChart(
                    "Genetic Algorithms progress",
                    "Category",
                    "Score",
                    Visualizer.createDataset(),
                    PlotOrientation.VERTICAL,
                    true, true, false);

            int width = 1000;   /* Width of the image */
            int height = 800;  /* Height of the image */
            File XYChart = new File( "Type2.jpeg" );
            ChartUtilities.saveChartAsJPEG( XYChart, xylineChart, width, height);
        } catch (GAException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
