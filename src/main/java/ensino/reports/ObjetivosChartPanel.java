/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.reports;

import ensino.components.GenJLabel;
import ensino.components.renderer.GenCellRenderer;
import ensino.configuracoes.model.Estudante;
import ensino.planejamento.model.Objetivo;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.renderer.TableBarCellRenderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author santos
 */
public class ObjetivosChartPanel extends JPanel {

    private PlanoDeEnsino planoDeEnsino;
    private List<Estudante> listaEstudantes;

    private Double mediaGeral;

    private HashMap<Objetivo, Double> barMap;
    private HashMap<Objetivo, HashMap<String, Integer>> pieMap;

    private Object rowsData[][];
    private String columnNames[];

    public ObjetivosChartPanel() {
        this(null);
    }

    public ObjetivosChartPanel(PlanoDeEnsino planoDeEnsino) {
        super();
        this.planoDeEnsino = planoDeEnsino;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(5, 5));
    }

    public void setPlanoDeEnsino(PlanoDeEnsino planoDeEnsino) {
        this.planoDeEnsino = planoDeEnsino;
        listaEstudantes = planoDeEnsino.getTurma().getEstudantes();
    }

    private void addBarMap(List<Objetivo> lObjetivos, HashMap<Objetivo, Double> barMap,
            int i, int j, Double localMedia) {
        Objetivo obj = lObjetivos.get(j);
        /**
         * Se a chave para controle das médias não existe, então deve ser criada
         * somente para a etapa que não for recuperação
         */
        if (!barMap.containsKey(obj)) {
            barMap.put(obj, localMedia);
        } else {
            /**
             * Atualiza a soma das médias
             */
            barMap.replace(obj, barMap.get(obj) + localMedia);
        }
    }

    private void updatePieDataHashMap(HashMap<String, Integer> map, String title, Integer value) {
        if (!map.containsKey(title)) {
            map.put(title, value);
        } else {
            map.replace(title, map.get(title) + value);
        }
    }

    private String getPieLegenda(Double media) {
        String legenda = "";
        if (media < 4.0) {
            legenda = "< 4.0";
        } else if (media >= 4.0 && media < 6.0) {
            legenda = "Entre 4.0 e 6.0";
        } else {
            legenda = ">= 6.0";
        }
        return legenda;
    }

    private void addPieMap(List<Objetivo> lObjetivos, HashMap<Objetivo, HashMap<String, Integer>> pieMap,
            int i, int j, Double localMedia) {
        Objetivo ee = lObjetivos.get(j);
        HashMap<String, Integer> dataMap;

        /**
         * Se a chave para controle das médias não existe, então deve ser criada
         * somente para a etapa que não for recuperação
         */
        if (!pieMap.containsKey(ee)) {
            dataMap = new HashMap();
            pieMap.put(ee, dataMap);
        } else {
            dataMap = pieMap.get(ee);
        }
        updatePieDataHashMap(dataMap, getPieLegenda(localMedia), 1);

    }

    private void generateDataset() {
        List<Objetivo> lObjetivos = planoDeEnsino.getObjetivos();
        /**
         * Define a lista de nomes das colunas da tabela
         */
        columnNames = new String[lObjetivos.size() + 2];
        int i = 0;
        columnNames[0] = "Estudante";
        for (i = 0; i < lObjetivos.size(); i++) {
            columnNames[i + 1] = lObjetivos.get(i).getShortName();
        }
        columnNames[i + 1] = "Média Final";

        mediaGeral = 0.0;
        Double somaGeral = 0.0;
        Integer nEstudante = listaEstudantes.size();
        /**
         * Cria a estrutura de dados para armazenamento na tabela geral
         */
        rowsData = new Object[listaEstudantes.size()][columnNames.length];

        barMap = new HashMap();
        pieMap = new HashMap();
        for (i = 0; i < nEstudante; i++) {
            Estudante e = listaEstudantes.get(i);

            rowsData[i][0] = e.getNome();
            int j;
            for (j = 0; j < lObjetivos.size(); j++) {
                Objetivo obj = lObjetivos.get(j);

                Double localMedia = e.getMediaPorObjetivo(planoDeEnsino, obj);
                rowsData[i][j + 1] = localMedia;

                /**
                 * Controle do grafico de barras
                 */
                addBarMap(lObjetivos, barMap, i, j, localMedia);
                /**
                 * Controle do gráfico de pizza
                 */
                addPieMap(lObjetivos, pieMap, i, j, localMedia);
            }
            Double media = e.getMediaObjetivoGeral(planoDeEnsino);
            rowsData[i][j + 1] = media;
            somaGeral += media;
        }

        /**
         * Registra a média geral da turma
         */
        mediaGeral = somaGeral / nEstudante;
    }

    private JPanel createTablePanel() {
        JTable table = new JTable(rowsData, columnNames);
        GenCellRenderer cellRenderer = createCellRenderer();
        TableColumnModel tcm = table.getColumnModel();
        int size = table.getColumnModel().getColumnCount();
        for (int i = 0; i < size; i++) {
            TableColumn tc = tcm.getColumn(i);
            tc.setCellRenderer(cellRenderer);
            if (i == 0) {
                tc.setMinWidth(250);
            } else {
                tc.setMaxWidth(200);
            }
        }

        GenJLabel labelStatus = new GenJLabel(
                String.format("Estudantes %d", rowsData.length), JLabel.RIGHT);
        labelStatus.resetFontSize(10);
        labelStatus.setForeground(Color.white);

        JPanel panelLabel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelLabel.add(labelStatus);
        panelLabel.setBackground(Color.DARK_GRAY);
        panelLabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(
                ChartsFactory.createPanel("Médias dos Estudantes", table, new Dimension(640, 180)),
                BorderLayout.CENTER);
        panel.add(panelLabel, BorderLayout.PAGE_END);
        return panel;
    }

    private GenCellRenderer createCellRenderer() {
        return new TableBarCellRenderer();
    }

    public void loadCharts() {
        generateDataset();

        JPanel panelCenter = new JPanel(new BorderLayout(5, 5));

        panelCenter.add(createDetailDashboard(), BorderLayout.CENTER);
        panelCenter.add(createTablePanel(), BorderLayout.PAGE_END);

        add(panelCenter, BorderLayout.CENTER);
        add(createHeaderDashboard(), BorderLayout.PAGE_START);
    }

    private JPanel createDetailDashboardByObjetivo(
            DefaultPieDataset pieDataset,
            Objetivo obj) {

        return ChartsFactory.createPieChartPanel(pieDataset, "Média por Objetivo",
                obj.getShortName(),
                new Dimension(360, 280));
    }

    private JPanel createTablePanel(String title, Integer colObj,
            Double minValue, Double maxValue) {
        String colNames[] = new String[]{
            columnNames[0], columnNames[colObj]
        };

        List<List<Object>> dataList = new ArrayList();
        for (int i = 0; i < rowsData.length; i++) {
            List<Object> database = new ArrayList();
            Double val = (Double) rowsData[i][colObj];
            if (val.compareTo(minValue) >= 0 && val.compareTo(maxValue) <= 0) {
                database.add(rowsData[i][0]);
                database.add(val);

                dataList.add(database);
            }
        }

        Integer rows = 0, cols = 0;
        JTable table = null;
        if (!dataList.isEmpty()) {
            rows = dataList.size();
            cols = dataList.get(0).size();
            Object[][] newRowsData = new Object[rows][cols];
            for (int i = 0; i < rows; i++) {
                List<Object> database = dataList.get(i);
                for (int j = 0; j < cols; j++) {
                    newRowsData[i][j] = database.get(j);
                }
            }

            table = new JTable(newRowsData, colNames);
            Iterator<TableColumn> it = table.getColumnModel().getColumns().asIterator();
            int i = 0;
            GenCellRenderer cellRenderer = createCellRenderer();
            while (it.hasNext()) {
                TableColumn tc = it.next();
                tc.setCellRenderer(cellRenderer);
                if (i > 0) {
                    tc.setMaxWidth(200);
                }
                i++;
            }
        } else {
            table = new JTable();
        }

        GenJLabel labelStatus = new GenJLabel(String.format("Estudantes %d de %d [Perc: %.2f%%]",
                rows, rowsData.length, (rows.doubleValue() / rowsData.length) * 100.0),
                JLabel.RIGHT);
        labelStatus.resetFontSize(10);
        labelStatus.setForeground(Color.white);

        JPanel panelLabel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelLabel.add(labelStatus);
        panelLabel.setBackground(Color.DARK_GRAY);
        panelLabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(ChartsFactory.createPanel(title, table, new Dimension(440, 140)), BorderLayout.CENTER);
        panel.add(panelLabel, BorderLayout.PAGE_END);
        return panel;
    }

    private JPanel createDetailDashboard() {
        /**
         * Carrega os dados para o gráfico de pizza
         */
        JTabbedPane tabs = new JTabbedPane();

        List<Objetivo> lObjetivos = planoDeEnsino.getObjetivos();
        for (int i = 0; i < lObjetivos.size(); i++) {
            JPanel panelEE = new JPanel(new BorderLayout(10, 10));

            Objetivo obj = lObjetivos.get(i);

            /**
             * Carrega os dados para o gráfico de pizza por objetivos
             */
            DefaultPieDataset pieDataset = new DefaultPieDataset();
            HashMap<String, Integer> mapEtapa = pieMap.get(obj);
            if (mapEtapa != null) {
                mapEtapa.forEach((k, v) -> {
                    pieDataset.setValue(k, v);
                });
            }

            panelEE.add(createDetailDashboardByObjetivo(pieDataset, obj), BorderLayout.LINE_START);

            JTabbedPane tabsCat = new JTabbedPane();

            tabsCat.addTab("Média < 4.0", createTablePanel("Estudantes (média < 4.0)", i + 1, 0.0, 3.9999));
            tabsCat.addTab("4.0 >= Média < 6.0", createTablePanel("Estudantes (4.0 >= média < 6.0)", i + 1, 4.0, 5.9999));
            tabsCat.addTab("Média >= 6.0", createTablePanel("Estudantes (média >= 6.0)", i + 1, 6.0, 10.0));

            panelEE.add(tabsCat, BorderLayout.CENTER);

            tabs.addTab(obj.getShortName(), panelEE);
        }

        return ChartsFactory.createPanel(Color.DARK_GRAY, "Detalhamento das Médias", tabs, new Dimension(640, 480));
    }

    private JPanel createHeaderDashboard() {
        Double scala = 0.5;
        Integer w = 640, h = 480;
        Dimension d = new Dimension(Double.valueOf(w * scala).intValue(),
                Double.valueOf(h * scala).intValue());

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(ChartsFactory.createCarMeterChartPanel(mediaGeral * 10, "Média Geral", d),
                BorderLayout.LINE_START);

        /**
         * Carrega os dados para o gráfico de barras
         */
        List<Objetivo> lObjetivos = planoDeEnsino.getObjetivos();
        Integer nEstudante = listaEstudantes.size();
        DefaultCategoryDataset barCategoryDataset = new DefaultCategoryDataset();
        for (int i = 0; i < lObjetivos.size(); i++) {
            Objetivo obj = lObjetivos.get(i);
            barCategoryDataset.setValue(
                    barMap.get(obj) == null ? 0 : barMap.get(obj) / nEstudante, "Média",
                    obj.getShortName());
        }

        d = new Dimension(w, Double.valueOf(h * scala).intValue());
        panel.add(ChartsFactory.createBarPanel("Média por Objetivo", "Objetivos", "Médias", barCategoryDataset, d),
                BorderLayout.CENTER);

        return panel;
    }

//    public static void main(String args[]) {
//        try {
//            PlanoDeEnsino plano = ControllerFactory.createPlanoDeEnsinoController().buscarPorId(1);
//
//            JFrame f = new JFrame();
//            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//            ObjetivosChartPanel p = new ObjetivosChartPanel();
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
