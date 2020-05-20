/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.reports;

import ensino.components.GenJLabel;
import ensino.components.renderer.GenCellRenderer;
import ensino.configuracoes.model.Estudante;
import ensino.planejamento.model.Diario;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.util.types.SituacaoEstudante;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.table.TableColumn;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author santos
 */
public class FrequenciaChartPanel extends JPanel {

    private PlanoDeEnsino planoDeEnsino;
    private List<Estudante> listaEstudantes;

    private Integer verticalGap;
    private Integer horizontalGap;
    private Color backgroundColor;

    private List<Estudante> lista20;
    private List<Estudante> listaAte25;
    private List<Estudante> listaMaior25;

    public FrequenciaChartPanel() {
        super();
        setLayout(new BorderLayout(5, 5));

        verticalGap = 10;
        horizontalGap = 10;
        backgroundColor = Color.WHITE;
    }

    public void loadCharts() {
        Double scale = 0.75;
        Double w = 640 * scale,
                h = 480 * scale;
        Dimension d = new Dimension(w.intValue(), h.intValue());

        generateDataset();

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Painel de Faltas", createNumberFaltasDashboard(d));

        add(createNumberFaltasDashboard(d), BorderLayout.CENTER);
    }

    private JPanel createNumberFaltasDashboard(Dimension d) {
        List<Diario> lDiarios = planoDeEnsino.getDiarios();
        Integer nDiarios = lDiarios.size();

        DefaultPieDataset pieDatasetFaltas = new DefaultPieDataset();
        Integer menor20 = 0, ate25 = 0, maior25 = 0;

        for (int i = 0; i < listaEstudantes.size(); i++) {
            Estudante e = listaEstudantes.get(i);
            if (!SituacaoEstudante.DESLIGADO.equals(e.getSituacaoEstudante())) {
                Integer nFaltas = e.getFaltas(planoDeEnsino);
                Double perc = nFaltas.doubleValue() / nDiarios.doubleValue();

                if (perc < 0.2) {
                    menor20++;
                } else if (perc > 0.2 && perc <= 0.25) {
                    ate25++;
                } else {
                    maior25++;
                }
            }
        }

        pieDatasetFaltas.setValue("Faltas < 20%", menor20);
        pieDatasetFaltas.setValue("20% >= Faltas <= 25%", ate25);
        pieDatasetFaltas.setValue("Faltas > 25%", maior25);

        JPanel panelNumerosFaltas = new JPanel(new FlowLayout(FlowLayout.LEFT, horizontalGap, verticalGap));
        panelNumerosFaltas.setBackground(backgroundColor);
        panelNumerosFaltas.add(createNumberDashboard("Faltas < 20%", "Estudantes com até 20% de faltas", menor20, null));
        panelNumerosFaltas.add(createNumberDashboard("20% >= Faltas <= 25%", "Estudantes entre 20% e 25% de faltas", ate25, null));
        panelNumerosFaltas.add(createNumberDashboard("Faltas > 25%", "Estudantes com mais de 25% de faltas", maior25, null));

        JPanel panelNumeros = new JPanel(new BorderLayout(horizontalGap, verticalGap));
        panelNumeros.setBackground(backgroundColor);
        panelNumeros.add(createNumberDashboard("Aulas", "Total de aulas do período", nDiarios, null), BorderLayout.CENTER);
        panelNumeros.add(panelNumerosFaltas, BorderLayout.PAGE_END);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Faltas < 20%", createFaltasTablePanel(lista20));
        tabs.addTab("20% >= Faltas <= 25%", createFaltasTablePanel(listaAte25));
        tabs.addTab("Faltas > 25%", createFaltasTablePanel(listaMaior25));

        JPanel panel = new JPanel(new BorderLayout(horizontalGap, verticalGap));
        panel.setBackground(backgroundColor);
        panel.add(panelNumeros, BorderLayout.LINE_END);
        panel.add(ChartsFactory.createPieChartPanel(pieDatasetFaltas, "Controle de Faltas",
                "Monitoramento de Faltas dos Estudantes", d), BorderLayout.CENTER);
        panel.add(tabs, BorderLayout.PAGE_END);

        return panel;
    }

    private JPanel createNumberDashboard(String title, String status, Integer value, Dimension d) {
        GenJLabel label = new GenJLabel(value.toString(), JLabel.CENTER);
        label.resetFontSize(40);

        JPanel panel = new JPanel(new BorderLayout(horizontalGap, verticalGap));
        panel.setBackground(backgroundColor);
        panel.add(label, BorderLayout.CENTER);

        GenJLabel labelStatus = new GenJLabel(status);
        labelStatus.resetFontSize(10);
        labelStatus.setForeground(Color.white);

        JPanel panelLabel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelLabel.add(labelStatus);
        panelLabel.setBackground(Color.DARK_GRAY);
        panelLabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        panel.add(panelLabel, BorderLayout.PAGE_END);

        return ChartsFactory.createPanel(title, panel, d);
    }

    private GenCellRenderer createCellRenderer() {
        return new GenCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col
            ) {
                if (isSelected) {
                    setColors(new Color(table.getSelectionForeground().getRGB()),
                            new Color(table.getSelectionBackground().getRGB()));
                } else {
                    setColors(new Color(table.getForeground().getRGB()),
                            new Color(255, 255, 255)
                    );
                }

                int textAlign = FlowLayout.LEFT;
                GenJLabel label;
                if (value instanceof Double) {
                    textAlign = FlowLayout.RIGHT;
                    Double v = (Double) value;
                    label = createLabel(String.format("%.2f", v));
                    Color cor = null;
                    if (v < 20.0) {
                        cor = Color.DARK_GRAY;
                    } else if (v >= 20.0 && v <= 25.0) {
                        cor = Color.BLUE;
                    } else {
                        cor = Color.RED;
                    }
                    label.setForeground(cor);
                } else if (value instanceof Integer) {
                    textAlign = FlowLayout.CENTER;
                    Integer v = (Integer) value;
                    label = createLabel(v.toString());
                } else {
                    label = createLabel((String) value);
                }
                label.resetFontSize(12);

                JPanel p = new JPanel(new FlowLayout(textAlign, 5, 5));
                p.add(label);
                p.setBackground(getBack());
                p.setOpaque(true);
                table.setRowHeight(p.getPreferredSize().height);
                return p;
            }
        };
    }

    private void generateDataset() {
        Integer nEstudantes = listaEstudantes.size();
        Integer nDiarios = planoDeEnsino.getDiarios().size();

        lista20 = new ArrayList<>();
        listaAte25 = new ArrayList<>();
        listaMaior25 = new ArrayList<>();

        for (int i = 0; i < nEstudantes; i++) {
            Estudante e = listaEstudantes.get(i);
            SituacaoEstudante se = e.getSituacaoEstudante();

            if (!SituacaoEstudante.DESLIGADO.equals(se)) {
                Integer nFaltas = e.getFaltas(planoDeEnsino);
                Double percFaltas = nFaltas.doubleValue() / nDiarios * 100;
                if (percFaltas < 20.0) {
                    lista20.add(e);
                } else if (percFaltas >= 20.0 && percFaltas <= 25.0) {
                    listaAte25.add(e);
                } else {
                    listaMaior25.add(e);
                }
            }
        }

    }

    private JPanel createFaltasTablePanel(List<Estudante> lista) {
        Integer nRows = lista.size();
        Integer nDiarios = planoDeEnsino.getDiarios().size();
        String columnNames[] = new String[]{"Estudante", "N.o Faltas", "Faltas(%)"};

        Object rowsData[][] = new Object[nRows][columnNames.length];
        for (int i = 0; i < nRows; i++) {
            Estudante e = lista.get(i);
            Integer nFaltas = e.getFaltas(planoDeEnsino);

            rowsData[i][0] = e.getNome();
            rowsData[i][1] = nFaltas;
            rowsData[i][2] = (nFaltas.doubleValue() / nDiarios.doubleValue() * 100);
        }

        JTable table = new JTable(rowsData, columnNames);
        Iterator<TableColumn> it = table.getColumnModel().getColumns().asIterator();
        int i = 0;
        GenCellRenderer cellRenderer = createCellRenderer();
        while (it.hasNext()) {
            TableColumn tc = it.next();
            tc.setCellRenderer(cellRenderer);
            if (i == 0) {
                tc.setMinWidth(250);
            } else {
                tc.setMinWidth(80);
                tc.setMaxWidth(200);
            }
            i++;
        }

        GenJLabel labelStatus = new GenJLabel(
                String.format("Estudantes %d", nRows), JLabel.RIGHT);
        labelStatus.resetFontSize(10);
        labelStatus.setForeground(Color.white);

        JPanel panelLabel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelLabel.add(labelStatus);
        panelLabel.setBackground(Color.DARK_GRAY);
        panelLabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        JPanel panel = new JPanel(new BorderLayout(horizontalGap, verticalGap));
        panel.add(
                ChartsFactory.createPanel("Faltas dos Estudantes", table, new Dimension(640, 180)),
                BorderLayout.CENTER);
        panel.add(panelLabel, BorderLayout.PAGE_END);
        return panel;
    }

    public void setPlanoDeEnsino(PlanoDeEnsino planoDeEnsino) {
        this.planoDeEnsino = planoDeEnsino;
        listaEstudantes = planoDeEnsino.getTurma().getEstudantes();
    }

//    public static void main(String args[]) {
//        try {
//            PlanoDeEnsino plano = ControllerFactory.createPlanoDeEnsinoController().buscarPorId(1);
//
//            JFrame f = new JFrame();
//            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//            FrequenciaChartPanel p = new FrequenciaChartPanel();
//            p.setPlanoDeEnsino(plano);
//            p.loadCharts();
//
//            f.getContentPane().add(p);
//            f.pack();
//            f.setLocationRelativeTo(null);
//            f.setVisible(true);
//        } catch (Exception ex) {
//            Logger.getLogger(PlanoDeEnsinoHtmlNotasPanel.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

}
