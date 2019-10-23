/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.frame;

import ensino.configuracoes.view.panels.CampusPanel;

/**
 *
 * @author nicho
 */
public class FrameCampus extends javax.swing.JInternalFrame {

    public FrameCampus() {
        super("Campi", true, true, true, true);
        CampusPanel panel = new CampusPanel(this);
        getContentPane().add(panel);
        pack();
    }

}
