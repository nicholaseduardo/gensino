/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.reports;

import ensino.components.GenJLabel;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.text.DecimalFormat;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPosition;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.CategoryLabelWidthType;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DialShape;
import org.jfree.chart.plot.MeterInterval;
import org.jfree.chart.plot.MeterPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.text.TextBlockAnchor;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.Range;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.DefaultValueDataset;

/**
 *
 * @author santos
 */
public class ChartsFactory {

    private static Font font = new Font("Arial", 1, 14);
    private static final Double DEFAULT_SCALE = 0.8;
    
    public static Color ardoziaBlueColor = new Color(0, 127, 255);
    public static Color amberColor = new Color(240, 163, 10, 50);
    public static Color lightBlue = new Color(166, 218, 247);
    public static Color ligthGreen = new Color(177, 247, 166, 70);
    public static Color darkGreen = new Color(9, 112, 62);
    
    private static Dimension scaleDimension(Dimension d, Double s) {
        Integer w = Double.valueOf(d.getWidth() * s).intValue(),
                h = Double.valueOf(d.getHeight() * s).intValue();
        Dimension n = new Dimension(w, h);
        return n;
    }

    public static JPanel createPanel(String title, JComponent component,
            Dimension d) {
        return createPanel(ardoziaBlueColor, title, component, d);
    }
    
    public static JPanel createPanel(Color backgroundColor, String panelTitle, 
            JComponent innerComponent, Dimension d) {
        return createPanel(backgroundColor, panelTitle, Color.WHITE, innerComponent, d);
    }

    public static JPanel createPanel(Color backgroundColor, String panelTitle, 
            Color foregroundColor, JComponent innerComponent, Dimension d) {
        return createPanel(backgroundColor, panelTitle, Color.WHITE, innerComponent, d, null);
    }

    public static JPanel createPanel(Color backgroundColor, String panelTitle, 
            Color foregroundColor, JComponent innerComponent, Dimension d,
            JComponent buttonComponent) {
        GenJLabel label = new GenJLabel(panelTitle);
        label.resetFontSize(16);
        label.toBold();
        label.setForeground(foregroundColor);

        JPanel titlePanel = new JPanel(new BorderLayout(10, 10));
        titlePanel.setBackground(backgroundColor);
        titlePanel.add(label, BorderLayout.CENTER);
        if (buttonComponent != null) {
            titlePanel.add(buttonComponent, BorderLayout.LINE_END);
        }

//        innerComponent.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        JScrollPane scroll = new JScrollPane();
        scroll.setAutoscrolls(true);
        scroll.setBackground(Color.white);
        if (innerComponent instanceof JTable) {
            scroll.setPreferredSize(scaleDimension(d, 0.85));
        }
        scroll.setViewportView(innerComponent);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createLineBorder(backgroundColor, 2, true));
        panel.setBackground(backgroundColor);
        if (d != null) {
            panel.setPreferredSize(d);
        }
        panel.add(titlePanel, BorderLayout.PAGE_START);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    public static JPanel createPanel(String title, JComponent component,
            Integer w, Integer h) {
        return createPanel(title, component, new Dimension(w, h));
    }

    public static JPanel createCarMeterChartPanel(Double value, String title, Dimension d) {

        DefaultValueDataset dataset = new DefaultValueDataset(value);

        MeterPlot meterplot = new MeterPlot(dataset);
        meterplot.setRange(new Range(0.0, 100));
        meterplot.addInterval(new MeterInterval("Ruim", new Range(0.0D, 40.0D),
                Color.red, new BasicStroke(2.0F), new Color(255, 0, 0, 128)));
        meterplot.addInterval(new MeterInterval("Alerta", new Range(40.0D, 60.0D),
                Color.yellow, new BasicStroke(2.0F), new Color(255, 255, 0, 64)));
        meterplot.addInterval(new MeterInterval("Bom", new Range(60.0D, 100.00D),
                Color.green, new BasicStroke(2.0F), new Color(0, 255, 0, 64)));

        meterplot.setNeedlePaint(Color.darkGray);
        meterplot.setDialBackgroundPaint(Color.white);
        meterplot.setDialOutlinePaint(Color.black);
        meterplot.setDialShape(DialShape.CHORD);
        meterplot.setMeterAngle(180);

        meterplot.setTickLabelsVisible(true);
        meterplot.setTickLabelFont(font);
        meterplot.setTickLabelPaint(Color.black);
        meterplot.setTickSize(5D);
        meterplot.setTickPaint(Color.gray);

        meterplot.setValuePaint(Color.black);
        meterplot.setValueFont(font);

        JFreeChart chart = new JFreeChart("",
                JFreeChart.DEFAULT_TITLE_FONT, meterplot, true);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.getChart().getLegend().setItemFont(font);
        chartPanel.setBackground(Color.white);
        if (d != null) {
            chartPanel.setPreferredSize(scaleDimension(d, DEFAULT_SCALE));
        }

        return createPanel(title, chartPanel, d);
    }

    public static JPanel createPieChartPanel(DefaultPieDataset dataset, String titlePanel, 
            String titleChart, Dimension d) {
        JFreeChart pieChart = ChartFactory.createPieChart(titleChart,
                dataset, true, true, true);

        PieSectionLabelGenerator lableGenerator = new StandardPieSectionLabelGenerator(
                "Média {0} : ({2})", new DecimalFormat("0"), new DecimalFormat("0%")
        );
        ((PiePlot) pieChart.getPlot()).setLabelGenerator(lableGenerator);

        pieChart.getLegend().setItemFont(font);

        ChartPanel chartPanel = new ChartPanel(pieChart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        if (d != null) {
            chartPanel.setPreferredSize(scaleDimension(d, DEFAULT_SCALE));
        }

        return createPanel(titlePanel, chartPanel, d);
    }

    public static JPanel createBarPanel(String title,
            String domainAxisLabel, String rangeAxisLabel,
            DefaultCategoryDataset dataset, Dimension d) {

        JFreeChart chart = ChartFactory.createBarChart(
                "", // chart title
                domainAxisLabel, // domain axis label
                rangeAxisLabel, // range axis label
                dataset, // data
                PlotOrientation.HORIZONTAL, // orientation
                false, // include legend
                true, // tooltips
                false // urls
        );
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setForegroundAlpha(1.0f);

        CategoryAxis axis = plot.getDomainAxis();
        CategoryLabelPositions p = axis.getCategoryLabelPositions();

        CategoryLabelPosition left = new CategoryLabelPosition(
                RectangleAnchor.LEFT, TextBlockAnchor.CENTER_LEFT,
                TextAnchor.CENTER_LEFT, 0.0,
                CategoryLabelWidthType.RANGE, 0.30f
        );
        axis.setCategoryLabelPositions(CategoryLabelPositions.replaceLeftPosition(p, left));

        ChartPanel chartPanel = new ChartPanel(chart);
        if (d != null) {
            chartPanel.setPreferredSize(scaleDimension(d, DEFAULT_SCALE));
        }

        return createPanel(title, chartPanel, d);
    }

//    public static void main(String args[]) {
//        Dimension d = new Dimension(640, 480);
//        JFrame f = new JFrame("Teste");
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        f.getContentPane().add(ChartsFactory.createCarMeterChartPanel(75.0, "Teste", d));
//        f.pack();
//        f.setVisible(true);
//    }

}
