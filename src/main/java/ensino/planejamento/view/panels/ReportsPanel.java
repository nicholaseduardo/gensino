/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels;

import ensino.components.GenJButton;
import ensino.components.GenJLabel;
import ensino.components.GenJPanel;
import static ensino.components.GenJPanel.IMG_SOURCE;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoCharts;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoHtml;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoHtmlNotas;
import ensino.util.types.AcoesBotoes;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author santos
 */
public class ReportsPanel extends GenJPanel {
    private PlanoDeEnsino planoDeEnsino;

    private GenJButton btExit;
    private JTabbedPane tabs;

    private Component frame;
    
    public ReportsPanel(Component frame, PlanoDeEnsino planoDeEnsino) {
        super();
        this.frame = frame;
        this.planoDeEnsino = planoDeEnsino;

        initComponents();
    }

    private void initComponents() {
        URL url = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "Custom-reports-icon-50px.png"));

        // Título da Janela
        GenJLabel titleLabel = new GenJLabel("Relatórios");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(new EmptyBorder(5, 10, 5, 0));
        titleLabel.setIcon(new ImageIcon(url));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        btExit = createButton(new ActionHandler(AcoesBotoes.CLOSE));
        tabs = new JTabbedPane();
        
        PlanoDeEnsinoHtml planoDeEnsinoReport = new PlanoDeEnsinoHtml(planoDeEnsino);
        PlanoDeEnsinoHtmlNotas notasReport = new PlanoDeEnsinoHtmlNotas(planoDeEnsino);
        PlanoDeEnsinoCharts graficosPanel = new PlanoDeEnsinoCharts(planoDeEnsino);
        
        tabs.addTab("Impressão do Plano de Ensino", iconReport, planoDeEnsinoReport);
        tabs.addTab("Relatório de Notas", iconReport, notasReport);
        tabs.addTab("Gráficos de Controle", iconChart, graficosPanel);
        
        setLayout(new BorderLayout(10, 10));

        add(titleLabel, BorderLayout.PAGE_START);
        add(tabs, BorderLayout.CENTER);
        add(btExit, BorderLayout.PAGE_END);
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
