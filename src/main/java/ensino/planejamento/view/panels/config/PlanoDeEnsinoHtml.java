/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.config;

import ensino.defaults.DefaultFieldsPanel;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.reports.FichaPlanoDeEnsino;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author nicho
 */
public class PlanoDeEnsinoHtml extends DefaultFieldsPanel {

    private JScrollPane scrollPane;
    private PlanoDeEnsino planoDeEnsino;

    public PlanoDeEnsinoHtml(PlanoDeEnsino planoDeEnsino) {
        super();
        this.planoDeEnsino = planoDeEnsino;
        initComponents();
    }

    private void initComponents() {
        setName("plano.html");
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        setBorder(BorderFactory.createEtchedBorder());
        setLayout(new BorderLayout());

        // create a scrollpane; modify its attributes as desired
        scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(1240, 800));
        scrollPane.setAutoscrolls(true);

        JPanel panel = new JPanel();
        panel.add(scrollPane);
        this.add(scrollPane, BorderLayout.CENTER);

        try {
            /**
             * Criando o PDF de acordo com o plano de ensino
             */
            FichaPlanoDeEnsino report = new FichaPlanoDeEnsino(planoDeEnsino);
            report.initReport();
            scrollPane.setViewportView(report.getViewer());
        } catch (IOException ex) {
            showErrorMessage(ex);
        }
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
