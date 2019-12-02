/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.frame;

import ensino.configuracoes.model.Atividade;
import ensino.components.GenJButton;
import ensino.configuracoes.controller.AtividadeController;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.view.panels.AtividadeFields;
import ensino.defaults.DefaultFieldsPanel;
import ensino.defaults.DefaultFormPanel;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class FrameAtividade extends JDialog {

    private Atividade atividade;
    private Calendario calendario;
    private AtividadeFields panelFields;
    private GenJButton btGravar;
    private GenJButton btFechar;

    public FrameAtividade() {
        super();

        setTitle("Atividade");
        setModal(true);

        setLayout(new BorderLayout());

        ButtonAction btAction = new ButtonAction();

        panelFields = new AtividadeFields();
        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btGravar = new GenJButton("Gravar", new ImageIcon(getClass().getResource("/img/save-button-25px.png")));
        btGravar.addActionListener(btAction);
        btFechar = new GenJButton("Fechar", new ImageIcon(getClass().getResource("/img/exit-button-25px.png")));
        btFechar.addActionListener(btAction);
        panelButton.add(btGravar);
        panelButton.add(btFechar);
        panelButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        Container container = getContentPane();
        container.add(panelFields, BorderLayout.CENTER);
        container.add(panelButton, BorderLayout.SOUTH);
        pack();
    }

    /**
     * Mostra os dados nos campos. Null para incluir um novo
     *
     * @param atividade
     */
    public void setAtividade(Atividade atividade) {
        panelFields.clearFields();
        this.atividade = atividade;
        if (atividade != null) {
            calendario = atividade.getCalendario();
            panelFields.setStatusPanel(DefaultFieldsPanel.UPDATE_STATUS_PANEL);
            panelFields.setFieldValues(this.atividade);
        } else {
            panelFields.setStatusPanel(DefaultFieldsPanel.INSERT_STATUS_PANEL);
        }
        panelFields.setCalendario(calendario);
    }
    
    public Atividade getAtividade() {
        return this.atividade;
    }
    
    public void setCalendario(Calendario calendario) {
        this.calendario = calendario;
    }

    private class ButtonAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(btGravar)) {
                if (panelFields.isValidated()) {
                    HashMap<String, Object> params = panelFields.getFieldValues();
                    try {
                        AtividadeController col = new AtividadeController();
                        Object object = col.salvar(params);
                        if (panelFields.getStatusPanel() == DefaultFieldsPanel.UPDATE_STATUS_PANEL) {
                            calendario.updAtividade((Atividade) object);
                        } else {
                            calendario.addAtividade((Atividade) object);
                        }
                        JOptionPane.showMessageDialog(null, "Dados gravados com sucesso!",
                                "Informação", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } catch (Exception ex) {
                        Logger.getLogger(DefaultFormPanel.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(null, ex.getMessage(),
                                "Aviso", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Os campos em Asterisco (*) não foram preenchidos/selecioados.",
                            "Informação", JOptionPane.INFORMATION_MESSAGE);
                }
            } else if (e.getSource().equals(btFechar)) {
                dispose();
            }
        }

    }

}
