/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.mvc.view.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

/**
 *
 * @author nicho
 */
public class MainActionListener implements ActionListener {

    private JDesktopPane desktop;

    public MainActionListener(JDesktopPane desktop) {
        this.desktop = desktop;
    }

    private void addFrame(JInternalFrame frame) throws PropertyVetoException {
        frame.setMaximizable(true);
        frame.setIconifiable(true);
        frame.setClosable(true);
        frame.setSelected(true);
        frame.setVisible(true);
        frame.pack();
        desktop.add(frame);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Alterar para recuperar o texto de um recurso
        if ("Sair".equals(e.getActionCommand())) {
            System.exit(0);
        } else {
            try {
                switch (e.getActionCommand()) {
                    case "Calendário":
                        addFrame(new ensino.configuracoes.view.frame.FrameCalendario());
                        break;
                    case "Bibliografia":
                        addFrame(new ensino.configuracoes.view.frame.FrameBibliografia());
                        break;
                    case "PlanoEnsino":
                        addFrame(new ensino.planejamento.view.frame.FramePlanoDeEnsino());
                        break;
                }
            } catch (PropertyVetoException ex) {
                Logger.getLogger(MainActionListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
