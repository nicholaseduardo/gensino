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
import java.awt.Component;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;

/**
 *
 * @author nicho
 */
public class PlanoDeEnsinoCharts extends DefaultFieldsPanel {

    private PlanoDeEnsino planoDeEnsino;
    private JTabbedPane tabs;
    private Component frame;

    public PlanoDeEnsinoCharts(Component frame, PlanoDeEnsino planoDeEnsino) {
        super();
        this.frame = frame;
        this.planoDeEnsino = planoDeEnsino;
        
        setName("objetivos.chart");
        setBorder(BorderFactory.createEtchedBorder());
        setLayout(new BorderLayout(10, 10));

        tabs = new JTabbedPane();
        add(tabs, BorderLayout.CENTER);
        refreshComponents();
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
}
