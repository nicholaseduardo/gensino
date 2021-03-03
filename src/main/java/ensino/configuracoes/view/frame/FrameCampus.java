/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.frame;

import ensino.configuracoes.view.panels.campus.CampusPanel;
import javax.swing.JInternalFrame;

/**
 *
 * @author nicho
 */
public class FrameCampus extends JInternalFrame {

    public FrameCampus(JInternalFrame areaDeTrabalho) {
        super("Campi", true, true, true, true);
        CampusPanel panel = new CampusPanel(this, areaDeTrabalho);
        getContentPane().add(panel);
        pack();
    }

}
