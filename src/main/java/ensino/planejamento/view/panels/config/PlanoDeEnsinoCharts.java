/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.config;

import ensino.components.GenJButton;
import ensino.defaults.DefaultFieldsPanel;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.reports.ChartsFactory;
import ensino.reports.EtapasChartPanel;
import ensino.reports.FrequenciaChartPanel;
import ensino.reports.ObjetivosChartPanel;
import ensino.util.types.AcoesBotoes;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author nicho
 */
public class PlanoDeEnsinoCharts extends DefaultFieldsPanel {

    private PlanoDeEnsino planoDeEnsino;
    private JTabbedPane tabs;
    private Component frame;

    public PlanoDeEnsinoCharts(Component frame) {
        super();
        this.frame = frame;
        setName("objetivos.chart");
        setBorder(BorderFactory.createEtchedBorder());
        setLayout(new BorderLayout(10, 10));

        backColor = ChartsFactory.ligthGreen;
        foreColor = ChartsFactory.darkGreen;
        setBackground(backColor);

        GenJButton btClose = createButton(new ActionHandler(AcoesBotoes.CLOSE), backColor, foreColor);

        JPanel pb = createPanel(new FlowLayout(FlowLayout.RIGHT));
        pb.add(btClose);
        add(pb, BorderLayout.PAGE_END);

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

    @Override
    public void onCloseAction(ActionEvent e) {
        if (frame instanceof JInternalFrame) {
            JInternalFrame f = (JInternalFrame) frame;
            f.dispose();
        } else if (frame instanceof JDialog) {
            JDialog d = (JDialog) frame;
            d.dispose();
        } else {
            JFrame f = (JFrame) frame;
            f.dispose();
        }
    }
}
