package org.arosso.stats;

import java.awt.Color;
import java.awt.Font;
import java.text.NumberFormat;

import javax.swing.BorderFactory;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class VPanel
{
    public XYSeries Vx,Vy,Vz;
    int maxAge;
    ChartPanel chartPanel;
    NumberAxis domain;
    public VPanel(int age)
    {
        maxAge=age;
    }
    public void GUI()
    {
        this.Vx = new XYSeries("Vx");
        this.Vy = new XYSeries("Vy");
        this.Vz = new XYSeries("Vz");
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(Vx);
        dataset.addSeries(Vy);
        dataset.addSeries(Vz);
        domain = new NumberAxis("Time");
        NumberAxis range = new NumberAxis("Vx Wy Vz");
        domain.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        range.setTickLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        domain.setLabelFont(new Font("SansSerif", Font.PLAIN, 14));
        range.setLabelFont(new Font("SansSerif", Font.PLAIN, 14));
       
        NumberFormat nf= new MyNumberFormat();
        domain.setNumberFormatOverride(nf);
        //domain.setStandardTickUnits(domain.createIntegerTickUnits());
        domain.setAutoRange(true);
       
        domain.setFixedAutoRange(maxAge/1000);
        domain.setLowerMargin(0.0);
        domain.setUpperMargin(0.0);

        domain.setTickLabelsVisible(true);
        domain.setMinorTickMarksVisible(false);
        XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);
        renderer.setSeriesPaint(0, Color.red);
        renderer.setSeriesPaint(1, Color.blue);
        renderer.setSeriesPaint(2, Color.green);
        XYPlot subplot = new XYPlot(dataset,domain,range,renderer);
        subplot.setDomainGridlinePaint(Color.black);
        subplot.setRangeGridlinesVisible(false);
        JFreeChart chart = new JFreeChart(" ",new Font("SansSerif", Font.BOLD, 24), subplot, true);
        chartPanel = new ChartPanel(chart);
        chartPanel.setLayout(null);
        chartPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4),BorderFactory.createLineBorder(Color.black)));


    }

}