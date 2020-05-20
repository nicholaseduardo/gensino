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
import ensino.reports.NotasReport;
import ensino.util.types.AcoesBotoes;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author nicho
 */
public class PlanoDeEnsinoHtmlNotas extends DefaultFieldsPanel {

    private PlanoDeEnsino planoDeEnsino;

    private JScrollPane scrollPane;
    private Component frame;

    public PlanoDeEnsinoHtmlNotas(Component frame) {
        super();
        this.frame = frame;
        initComponents();
    }

    private void initComponents() {
        setName("notas.html");
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        setBorder(BorderFactory.createEtchedBorder());
        setLayout(new BorderLayout());

        backColor = ChartsFactory.ligthGreen;
        foreColor = ChartsFactory.darkGreen;
        setBackground(backColor);

        GenJButton btClose = createButton(new ActionHandler(AcoesBotoes.CLOSE), backColor, foreColor);

        JPanel pb = createPanel(new FlowLayout(FlowLayout.RIGHT));
        pb.add(btClose);
        add(pb, BorderLayout.PAGE_END);

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
