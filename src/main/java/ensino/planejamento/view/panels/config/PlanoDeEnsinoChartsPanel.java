/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.config;

import ensino.defaults.DefaultFieldsPanel;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.reports.EtapasChartPanel;
import ensino.reports.FrequenciaChartPanel;
import ensino.reports.ObjetivosChartPanel;
import java.awt.BorderLayout;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;

/**
 *
 * @author nicho
 */
public class PlanoDeEnsinoChartsPanel extends DefaultFieldsPanel {

    private PlanoDeEnsino planoDeEnsino;
    private JTabbedPane tabs;

    public PlanoDeEnsinoChartsPanel() {
        super();
        setName("objetivos.chart");
        setBorder(BorderFactory.createEtchedBorder());
        setLayout(new BorderLayout(10, 10));

        tabs = new JTabbedPane();

        add(tabs, BorderLayout.CENTER);
    }

    private void refreshComponents() {
        /**
         * Limpa o TabbedPane para recriar os objetos
         */
        if (tabs.getTabCount() > 0) {
            tabs.removeAll();
        }

        ObjetivosChartPanel objetivoPanel = new ObjetivosChartPanel();
        objetivoPanel.setPlanoDeEnsino(planoDeEnsino);
        objetivoPanel.loadCharts();

        EtapasChartPanel etapasChartPanel = new EtapasChartPanel();
        etapasChartPanel.setPlanoDeEnsino(planoDeEnsino);
        etapasChartPanel.loadCharts();
        
        FrequenciaChartPanel frequenciaChartPanel = new FrequenciaChartPanel();
        frequenciaChartPanel.setPlanoDeEnsino(planoDeEnsino);
        frequenciaChartPanel.loadCharts();

        tabs.addTab("Painel da Turma", etapasChartPanel);
        tabs.addTab("Painel Objetivos da Turma", objetivoPanel);
        tabs.addTab("Painel Frequências", frequenciaChartPanel);
        tabs.repaint();
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        return null;
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {

    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof PlanoDeEnsino) {
            planoDeEnsino = (PlanoDeEnsino) object;

            refreshComponents();
        }
    }

    @Override
    public boolean isValidated() {
        return true;
    }

    @Override
    public void clearFields() {

    }

    @Override
    public void enableFields(boolean active) {

    }

    @Override
    public void initFocus() {

    }

//    public static void main(String args[]) {
//        try {
//            PlanoDeEnsino plano = ControllerFactory.createPlanoDeEnsinoController().buscarPorId(1);
//
//            JFrame f = new JFrame();
//            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//            PlanoDeEnsinoChartsPanel p = new PlanoDeEnsinoChartsPanel();
//            p.setFieldValues(plano);
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
