/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.reports;

import ensino.components.GenJLabel;
import ensino.components.renderer.GenCellRenderer;
import ensino.configuracoes.model.Estudante;
import ensino.configuracoes.model.EtapaEnsino;
import ensino.configuracoes.model.NivelEnsino;
import ensino.configuracoes.model.Turma;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.renderer.TableBarCellRenderer;
import ensino.util.types.SituacaoEstudante;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
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
public class EtapasChartPanel extends JPanel {

    private PlanoDeEnsino planoDeEnsino;
    private NivelEnsino nivelEnsino;
    private List<Estudante> listaEstudantes;

    private Double mediaDaTurma;

    private HashMap<EtapaEnsino, Double> barMap;
    private HashMap<EtapaEnsino, HashMap<FaixaMedia, Integer>> pieMap;

    private HashMap<FaixaMedia, List<Estudante>> mapFaixasEstudantes;
    private HashMap<SituacaoEstudante, Integer> mapSituacaoTurma;

    private Integer verticalGap;
    private Integer horizontalGap;
    private Color backgroundColor;

    /**
     * Atributos criados para serem utilizados na tabela de listagem geral dos
     * estudantes
     */
    private String columnNames[];
    private Object rowsData[][];

    public EtapasChartPanel() {
        this(null);
    }

    public EtapasChartPanel(PlanoDeEnsino planoDeEnsino) {
        super();
        this.planoDeEnsino = planoDeEnsino;

        verticalGap = 10;
        horizontalGap = 10;
        backgroundColor = Color.WHITE;

        setLayout(new BorderLayout(horizontalGap, verticalGap));
    }

    private void initValues() {
        mapSituacaoTurma = new HashMap();
        mapSituacaoTurma.put(SituacaoEstudante.APROVADO, 0);
        mapSituacaoTurma.put(SituacaoEstudante.REPROVADO_POR_NOTA, 0);
        mapSituacaoTurma.put(SituacaoEstudante.REPROVADO_POR_FALTA, 0);
        
        NivelEnsino ne = planoDeEnsino.getUnidadeCurricular().getCurso().getNivelEnsino();

        Integer nRows = listaEstudantes.size(),
                nCols = ne.getEtapas().size() + 2;
        columnNames = new String[nCols];
        columnNames[0] = "Estudante";
        rowsData = new Object[nRows][columnNames.length];

        barMap = new HashMap();
        pieMap = new HashMap();
        mapFaixasEstudantes = new HashMap();

        int i = 1;
        for (EtapaEnsino ee : ne.getEtapas()) {
            columnNames[i] = ee.getNome();
            barMap.put(ee, 0.0);

            HashMap<FaixaMedia, Integer> mapPieHash = new HashMap();
            for (FaixaMedia faixa : FaixaMedia.values()) {
                mapPieHash.put(faixa, 0);
                /**
                 * Cria a variável somente para a primeira etapa
                 */
                if (i == 1) {
                    mapFaixasEstudantes.put(faixa, new ArrayList<>());
                }
            }
            pieMap.put(ee, mapPieHash);
            i++;
        }
        columnNames[i] = "Média Final";
    }

    public void setPlanoDeEnsino(PlanoDeEnsino planoDeEnsino) {
        this.planoDeEnsino = planoDeEnsino;
        this.nivelEnsino = planoDeEnsino.getUnidadeCurricular().getCurso().getNivelEnsino();
        listaEstudantes = planoDeEnsino.getTurma().getEstudantes();
    }

    private FaixaMedia getFaixaByMedia(Double media) {
        if (media < 4.0) {
            return FaixaMedia.MENOR_40;
        } else if (media >= 4.0 && media < 6.0) {
            return FaixaMedia.MENOR_60;
        } else {
            return FaixaMedia.MAIOR_60;
        }
    }

    private void generateDataset() {
        initValues();
        
        Integer nDiarios = planoDeEnsino.getDiarios().size();

        List<EtapaEnsino> lEtapas = nivelEnsino.getEtapas();
        Integer nEstudante = listaEstudantes.size();
        Double somaGeral = 0.0;
        for (int i = 0; i < nEstudante; i++) {
            Estudante e = listaEstudantes.get(i);
            
            Double media = e.getMedia(planoDeEnsino);
            
            if (!SituacaoEstudante.DESLIGADO.equals(e.getSituacaoEstudante())) {
                Integer nFaltas = e.getFaltas(planoDeEnsino);
                Double percFaltas = nFaltas.doubleValue() / nDiarios;
                if (percFaltas > 0.25) {
                    Integer soma = mapSituacaoTurma.get(SituacaoEstudante.REPROVADO_POR_FALTA) + 1;
                    mapSituacaoTurma.replace(SituacaoEstudante.REPROVADO_POR_FALTA, soma);
                } else if (media < 6.0) {
                    Integer soma = mapSituacaoTurma.get(SituacaoEstudante.REPROVADO_POR_NOTA) + 1;
                    mapSituacaoTurma.replace(SituacaoEstudante.REPROVADO_POR_NOTA, soma);
                } else {
                    Integer soma = mapSituacaoTurma.get(SituacaoEstudante.APROVADO) + 1;
                    mapSituacaoTurma.replace(SituacaoEstudante.APROVADO, soma);
                }
            }
            
            rowsData[i][0] = e.getNome();
            /**
             * Dados a serem utilizados nos gráficos
             */
            Integer nEtapas = lEtapas.size();
            rowsData[i][nEtapas + 1] = media;
            somaGeral += media;
            FaixaMedia faixa = getFaixaByMedia(media);
            mapFaixasEstudantes.get(faixa).add(e);

            /**
             * Variável de controle do gráfico de pizza;
             */
            HashMap<FaixaMedia, Integer> dataMap;
            for (int j = 0; j < nEtapas; j++) {
                EtapaEnsino ee = lEtapas.get(j);

                Double localMedia = e.getMediaPorEtapa(planoDeEnsino, ee);
                // atualização da média da tabela geral
                rowsData[i][j + 1] = localMedia;
                /**
                 * Controle de armazenamento dos dados para os gráficos
                 */
                if (!ee.isRecuperacao()) {
                    /**
                     * Se não for recuperação, atualiza a soma das médias do
                     * gráfico de barras
                     */
                    barMap.replace(ee, barMap.get(ee) + localMedia);

                    /**
                     * atualiza os valores do gráfico de pizza
                     */
                    dataMap = pieMap.get(ee);
                    dataMap.replace(faixa, dataMap.get(faixa) + 1);
                } else {
                    /**
                     * Se for recuperação, verifica se é maior do que a média do
                     * nível dependente para atualizar a média da coluna
                     * anterior, que é a média recém lançada para os gráfico
                     */
                    Double mAnt = e.getMediaPorEtapa(planoDeEnsino, ee.getNivelDependente());
                    if (localMedia > mAnt) {
                        EtapaEnsino ant = ee.getNivelDependente();
                        barMap.replace(ant, barMap.get(ant) - mAnt + localMedia);

                        /**
                         * Recupera o valor da faixa anterior
                         */
                        dataMap = pieMap.get(ant);
                        FaixaMedia faixaAnt = getFaixaByMedia(mAnt);
                        // remove um a unidade
                        dataMap.replace(faixaAnt, dataMap.get(faixaAnt) - 1);
                        // adicionar o valor da anterior à faixa atual
                        dataMap.replace(faixa, dataMap.get(faixa) + 1);
                    }
                }
            }
        }

        /**
         * Registra a média geral da turma
         */
        mediaDaTurma = somaGeral / nEstudante;
    }

    private JPanel createTablePanel() {
        JTable table = new JTable(rowsData, columnNames);
        TableColumnModel tcm = table.getColumnModel();
        int size = tcm.getColumnCount();
        GenCellRenderer cellRenderer = createCellRenderer();
        for (int i = 0; i < size; i++) {
            TableColumn tc = tcm.getColumn(i);
            tc.setCellRenderer(cellRenderer);
            if (i == 0) {
                tc.setMinWidth(250);
            } else {
                tc.setMaxWidth(200);
            }
            i++;
        }

        GenJLabel labelStatus = new GenJLabel(
                String.format("Estudantes %d", rowsData.length), JLabel.RIGHT);
        labelStatus.resetFontSize(10);
        labelStatus.setForeground(Color.white);

        JPanel panelLabel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelLabel.add(labelStatus);
        panelLabel.setBackground(Color.DARK_GRAY);
        panelLabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        JPanel panel = new JPanel(new BorderLayout(horizontalGap, verticalGap));
        panel.setBackground(backgroundColor);
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
        setBackground(backgroundColor);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Dados da Turma", createTurmaDadosPanel());
        tabs.addTab("Situação da Turma", createAprovacaoTurmaPanel());
        
//        JPanel panelDados = new JPanel(new GridLayout(0, 1, horizontalGap, verticalGap));
//        panelDados.setBackground(backgroundColor);
//        panelDados.add(createTurmaDadosPanel());
//        panelDados.add(createAprovacaoTurmaPanel());
        
        JPanel panelCenter = new JPanel(new BorderLayout(horizontalGap, verticalGap));
        panelCenter.setBackground(backgroundColor);
        panelCenter.add(createDetailDashboard(), BorderLayout.CENTER);
        panelCenter.add(tabs, BorderLayout.LINE_END);
        panelCenter.add(createTablePanel(), BorderLayout.PAGE_END);
        
        
        
        add(createHeaderDashboard(), BorderLayout.PAGE_START);
        add(panelCenter, BorderLayout.CENTER);
    }

    private JPanel createNumberDashboard(String title, String status, Object value, 
            Dimension d) {
        if (d == null) {
            d = new Dimension(180,180);
        }
        String sValue = value.toString();
        if (value instanceof Double) {
            sValue = String.format("%.2f", value);
        } else if (value instanceof Integer) {
            sValue = String.format("%d", value);
        }
        
        GenJLabel label = new GenJLabel(sValue, JLabel.CENTER);
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
    
    private JPanel createTurmaDadosPanel() {
        Turma turma = planoDeEnsino.getTurma();
        Integer nDesligados = 0, nEmCurso = 0,
                nEstudantes = listaEstudantes.size();
        for (SituacaoEstudante situacao : SituacaoEstudante.values()) {
            Integer nSituacao = turma.getNumeroDeEstudatesPor(situacao);
            switch(situacao) {
                case DESLIGADO:
                    nDesligados = nSituacao;
                    break;
                case REPROVADO_POR_FALTA:
                case REPROVADO_POR_NOTA:
                case EM_CURSO:
                    nEmCurso += nSituacao;
                    break;
            }
        }
        
        JPanel panelNumerosTurma = new JPanel(new GridLayout(1, 2, 
                horizontalGap, verticalGap));
        panelNumerosTurma.setBackground(backgroundColor);
        panelNumerosTurma.add(createNumberDashboard("Em curso", 
                "Estudantes Em Curso", nEmCurso, null));
        panelNumerosTurma.add(createNumberDashboard("Desistentes", 
                "Estudantes desistentes", nDesligados, null));

        JPanel panelNumeros = new JPanel(new BorderLayout(horizontalGap, 
                verticalGap));
        panelNumeros.setBackground(backgroundColor);
        panelNumeros.add(createNumberDashboard("Estudantes inscritos",
                    "N.o de Estudantes inscritos na Turma", nEstudantes, null), 
                BorderLayout.CENTER);
        panelNumeros.add(panelNumerosTurma, BorderLayout.PAGE_END);
        
        return panelNumeros;
    }
    
    private JPanel createAprovacaoTurmaPanel() {
        JPanel panelNumerosTurma = new JPanel(new GridLayout(1, 2, 
                horizontalGap, verticalGap));
        panelNumerosTurma.setBackground(backgroundColor);
        panelNumerosTurma.add(createNumberDashboard("Reprovação (Nota)", 
                "Reprovados Por Nota", 
                mapSituacaoTurma.get(SituacaoEstudante.REPROVADO_POR_NOTA), 
                null));
        panelNumerosTurma.add(createNumberDashboard("Reprovados (Falta)", 
                "Reprovados por Falta", 
                mapSituacaoTurma.get(SituacaoEstudante.REPROVADO_POR_FALTA), 
                null));

        JPanel panelNumeros = new JPanel(new BorderLayout(horizontalGap, 
                verticalGap));
        panelNumeros.setBackground(backgroundColor);
        panelNumeros.add(createNumberDashboard("Estudantes Aprovados",
                    "N.o de Estudantes Aprovados", 
                mapSituacaoTurma.get(SituacaoEstudante.APROVADO), null), 
                BorderLayout.CENTER);
        panelNumeros.add(panelNumerosTurma, BorderLayout.PAGE_END);
        
        return panelNumeros;
    }

    private JPanel createDetailDashboardByEtapaEnsino(
            DefaultPieDataset pieDataset,
            EtapaEnsino ee) {

        return ChartsFactory.createPieChartPanel(pieDataset, "Média por Etapa de Ensino",
                ee.getNome(), new Dimension(360, 280));
    }

    private JPanel createTablePanel(String title, Integer colEtapa, 
            Integer colRecuperacao, Double minValue, Double maxValue) {
        String colNames[] = new String[]{
            columnNames[0], columnNames[colEtapa], columnNames[colRecuperacao]
        };

        List<List<Object>> dataList = new ArrayList();
        for (int i = 0; i < rowsData.length; i++) {
            List<Object> database = new ArrayList();
            Double val = (Double) rowsData[i][colEtapa];
            Double rec = (Double) rowsData[i][colRecuperacao];
            Double max = Double.max(val, rec);
            if (max.compareTo(minValue) >= 0 && max.compareTo(maxValue) <= 0) {
                database.add(rowsData[i][0]);
                database.add(val);
                database.add(rec);

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

        JPanel panel = new JPanel(new BorderLayout(horizontalGap, verticalGap));
        panel.setBackground(backgroundColor);
        panel.add(ChartsFactory.createPanel(title, table, new Dimension(440, 140)), BorderLayout.CENTER);
        panel.add(panelLabel, BorderLayout.PAGE_END);
        return panel;
    }

    private JPanel createDetailDashboard() {
        /**
         * Carrega os dados para o gráfico de pizza
         */
        JTabbedPane tabs = new JTabbedPane();

        List<EtapaEnsino> lEtapas = nivelEnsino.getEtapas();
        for (int i = 0; i < lEtapas.size(); i++) {
            JPanel panelEE = new JPanel(new BorderLayout(horizontalGap, verticalGap));
            panelEE.setBackground(backgroundColor);

            EtapaEnsino ee = lEtapas.get(i);
            if (!ee.isRecuperacao()) {
                /**
                 * Carrega os dados para o gráfico de pizza por etapas
                 */
                DefaultPieDataset pieDataset = new DefaultPieDataset();
                HashMap<FaixaMedia, Integer> mapEtapa = pieMap.get(ee);
                mapEtapa.forEach((k, v) -> {
                    pieDataset.setValue(k.toString(), v);
                });

                panelEE.add(createDetailDashboardByEtapaEnsino(pieDataset, ee), BorderLayout.LINE_START);

                JTabbedPane tabsCat = new JTabbedPane();
                tabsCat.addTab("Média < 4.0", createTablePanel("Estudantes (média < 4.0)", i + 1, i + 2, 0.0, 3.9999));
                tabsCat.addTab("4.0 >= Média < 6.0", createTablePanel("Estudantes (4.0 >= média < 6.0)", i + 1, i + 2, 4.0, 5.9999));
                tabsCat.addTab("Média >= 6.0", createTablePanel("Estudantes (média >= 6.0)", i + 1, i + 2, 6.0, 10.0));

                panelEE.add(tabsCat, BorderLayout.CENTER);

                tabs.addTab(ee.getNome(), panelEE);
            }
        }
        
        return ChartsFactory.createPanel(Color.DARK_GRAY, "Detalhamento das Médias", tabs, new Dimension(640, 480));
    }

    private JPanel createHeaderDashboard() {
        Double scala = 0.5;
        Integer w = 640, h = 480;
        Dimension d = new Dimension(Double.valueOf(w * scala).intValue(),
                Double.valueOf(h * scala).intValue());

        JPanel panel = new JPanel(new BorderLayout(horizontalGap, verticalGap));
        panel.setBackground(backgroundColor);
        panel.add(ChartsFactory.createCarMeterChartPanel(mediaDaTurma * 10, 
                "Média da Turma", d),
                BorderLayout.LINE_START);

        /**
         * Carrega os dados para o gráfico de barras
         */
        List<EtapaEnsino> lEtapas = nivelEnsino.getEtapas();
        Integer nEstudante = listaEstudantes.size();
        DefaultCategoryDataset barCategoryDataset = new DefaultCategoryDataset();
        for (int i = 0; i < lEtapas.size(); i++) {
            EtapaEnsino ee = lEtapas.get(i);
            if (!ee.isRecuperacao()) {
                barCategoryDataset.setValue(barMap.get(ee) / nEstudante, 
                        "Média", ee.getNome());
            }
        }
        
        /**
         * Calculo da média por faixa
         */
        DefaultPieDataset dataset = new DefaultPieDataset();
        for(FaixaMedia faixa: FaixaMedia.values()) {
            List<Estudante> l = mapFaixasEstudantes.get(faixa);
            Double somaFaixa = 0.0;
            Integer n = l.size();
            for (int i = 0; i < n; i++) {
                Estudante e = l.get(i);
                somaFaixa += e.getMedia(planoDeEnsino);
            }
            dataset.setValue(faixa.toString(), somaFaixa / n);
        }
        
        panel.add(ChartsFactory.createPieChartPanel(dataset, "Média da Turma por Faixa", 
                "Média por Faixa de Nota", d),
                BorderLayout.LINE_END);

        d = new Dimension(w, Double.valueOf(h * scala).intValue());
        panel.add(ChartsFactory.createBarPanel("Média da Turma por Etapa", 
                "Etapas de Ensino", "Médias", barCategoryDataset, d),
                BorderLayout.CENTER);

        return panel;
    }

    private enum FaixaMedia {
        MENOR_40(0), MENOR_60(1), MAIOR_60(2);

        private final int value;

        FaixaMedia(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static FaixaMedia of(int value) {
            return Stream.of(FaixaMedia.values()).filter(t -> t.getValue() == value)
                    .findFirst().orElseThrow(IllegalArgumentException::new);
        }

        @Override
        public String toString() {
            switch (value) {
                default:
                case 0:
                    return "Média < 4.0";
                case 1:
                    return "4.0 >= Média < 6.0";
                case 2:
                    return "Média >= 6.0";
            }
        }

    }

//    public static void main(String args[]) {
//        try {
//            PlanoDeEnsino plano = ControllerFactory.createPlanoDeEnsinoController().buscarPorId(1);
//
//            JFrame f = new JFrame();
//            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//            EtapasChartPanel p = new EtapasChartPanel();
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
