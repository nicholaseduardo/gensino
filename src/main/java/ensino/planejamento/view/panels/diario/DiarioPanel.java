/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.diario;

import ensino.components.GenJButton;
import ensino.components.GenJLabel;
import ensino.components.GenJPanel;
import static ensino.components.GenJPanel.IMG_SOURCE;
import ensino.planejamento.model.PlanoDeEnsino;
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
public class DiarioPanel extends GenJPanel {

    private PlanoDeEnsino planoDeEnsino;

    private GenJButton btExit;
    private JTabbedPane tabs;

    private Component frame;

    public DiarioPanel(Component frame, PlanoDeEnsino planoDeEnsino) {
        super();
        this.frame = frame;
        this.planoDeEnsino = planoDeEnsino;

        initComponents();
    }

    private void initComponents() {
        URL url = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "duplicate-button-50px.png"));

        // Título da Janela
        GenJLabel titleLabel = new GenJLabel("Diário - " + planoDeEnsino.getUnidadeCurricular().getNome());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(new EmptyBorder(5, 10, 5, 0));
        titleLabel.setIcon(new ImageIcon(url));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        btExit = createButton(new ActionHandler(AcoesBotoes.CLOSE));
        tabs = new JTabbedPane();

        DiarioFrequenciaPanel freqPanel = new DiarioFrequenciaPanel(frame, planoDeEnsino);
        DiarioAvaliacaoPanel avaliacaoPanel = new DiarioAvaliacaoPanel(frame, planoDeEnsino);
        DiarioConteudoPanel conteudoPanel = new DiarioConteudoPanel(null, planoDeEnsino);

        tabs.addTab("Frequência", iconFrequency, freqPanel);
        tabs.addTab("Conteúdo Programático", iconContent, conteudoPanel);
        tabs.addTab("Avaliações", iconEvaluation, avaliacaoPanel);

        setLayout(new BorderLayout(10, 10));

        add(titleLabel, BorderLayout.PAGE_START);
        add(tabs, BorderLayout.CENTER);
        add(btExit, BorderLayout.PAGE_END);
    }

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
