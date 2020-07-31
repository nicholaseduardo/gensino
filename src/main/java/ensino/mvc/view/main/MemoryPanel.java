/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.mvc.view.main;

import ensino.components.GenJLabel;
import ensino.components.GenJPanel;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.DialShape;
import org.jfree.chart.plot.MeterInterval;
import org.jfree.chart.plot.MeterPlot;
import org.jfree.data.Range;
import org.jfree.data.general.DefaultValueDataset;

/**
 *
 * @author santos
 */
public class MemoryPanel extends GenJPanel {

    private static Font font = new Font("Arial", Font.BOLD, 12);
    private MeterPlot meterplot;
    private ChartPanel chartPanel;
    private GenJLabel lblConsumo;
    
    private double maxVMMemory;

    public MemoryPanel() {
        super();

        initComponents();
    }

    private void initComponents() {
        maxVMMemory = Runtime.getRuntime().maxMemory() / 1048576.0;

        meterplot = new MeterPlot();
        meterplot.setRange(new Range(0.0, maxVMMemory));

        double goodValue = maxVMMemory * 0.4,
                mediumValue = maxVMMemory * 0.8;

        meterplot.addInterval(new MeterInterval("Bom", new Range(0.0D, goodValue),
                Color.green, new BasicStroke(2.0F), new Color(0, 255, 0, 64)));

        meterplot.addInterval(new MeterInterval("Alerta", new Range(goodValue, mediumValue),
                Color.yellow, new BasicStroke(2.0F), new Color(255, 255, 0, 64)));

        meterplot.addInterval(new MeterInterval("Ruim", new Range(mediumValue, maxVMMemory),
                Color.red, new BasicStroke(2.0F), new Color(255, 0, 0, 128)));

        meterplot.setNeedlePaint(Color.darkGray);
        meterplot.setDialBackgroundPaint(Color.white);
        meterplot.setDialOutlinePaint(Color.black);
        meterplot.setDialShape(DialShape.CHORD);
        meterplot.setMeterAngle(270);

        meterplot.setTickLabelsVisible(true);
        meterplot.setTickLabelFont(font);
        meterplot.setTickLabelPaint(Color.black);
        meterplot.setTickSize(5D);
        meterplot.setTickPaint(Color.gray);

        meterplot.setValuePaint(Color.black);
        meterplot.setValueFont(font);
        JFreeChart chart = new JFreeChart("", font, meterplot, true);
        chartPanel = new ChartPanel(chart);
        chartPanel.getChart().getLegend().setItemFont(font);
        chartPanel.setBackground(Color.white);
        chartPanel.setPreferredSize(new Dimension(200, 150));

        GenJLabel lblTible = new GenJLabel("Consumo de Memória (MB)");
        lblTible.resetFontSize(10);
        lblTible.setHorizontalAlignment(JLabel.CENTER);

        lblConsumo = new GenJLabel("0");
        lblConsumo.resetFontSize(9);
        lblConsumo.setHorizontalAlignment(JLabel.CENTER);

        setLayout(new BorderLayout());
        add(lblTible, BorderLayout.PAGE_START);
        add(chartPanel, BorderLayout.CENTER);
        add(lblConsumo, BorderLayout.PAGE_END);
    }

    public void startMonitor() {

        Thread t = new Thread(() -> {
            while (true) {
                Runtime rt = Runtime.getRuntime();
                double totalVMUsed = rt.totalMemory(),
                        total = maxVMMemory,
                        used = total - (rt.freeMemory() / 1048576.0);
                
                lblConsumo.setText(String.format("Consumo: %.3f MB de %.3f MB", used, maxVMMemory));
                
                DefaultValueDataset dataset = new DefaultValueDataset(used);
                meterplot.setDataset(dataset);
                
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MemoryPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        t.start();
    }

//    public static void main(String args[]) {
//        JFrame f = new JFrame("Teste");
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        f.add(new MemoryPanel());
//        f.pack();
//        f.setVisible(true);
//        Runtime rt = Runtime.getRuntime();
//        double total = rt.totalMemory() / 1048576.0,
//                used = total - rt.freeMemory() / 1048576.0;
//        System.out.printf("Total: %.3f MB / used: %.3f MB", total, used);
//    }
}
