/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.config;

import ensino.defaults.DefaultFieldsPanel;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.reports.NotasReport;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author nicho
 */
public class PlanoDeEnsinoHtmlNotasPanel extends DefaultFieldsPanel {

    private PlanoDeEnsino planoDeEnsino;

    private JScrollPane scrollPane;

    public PlanoDeEnsinoHtmlNotasPanel() {
        super();
        initComponents();
    }

    private void initComponents() {
        setName("notas.html");
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
            try {
                planoDeEnsino = (PlanoDeEnsino) object;

                /**
                 * Criando o PDF de acordo com o plano de ensino
                 */
                NotasReport notasReport = new NotasReport(planoDeEnsino);
                notasReport.initReport();
                scrollPane.setViewportView(notasReport.getViewer());
            } catch (Exception ex) {
                showErrorMessage(ex);
            }
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
}
