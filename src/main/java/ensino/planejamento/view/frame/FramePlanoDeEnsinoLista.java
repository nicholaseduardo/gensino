/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.frame;

import ensino.planejamento.view.panels.PlanoDeEnsinoPanel;
import javax.swing.JInternalFrame;

/**
 *
 * @author nicho
 */
public class FramePlanoDeEnsinoLista extends JInternalFrame {
    
    public FramePlanoDeEnsinoLista() {
        super("Planos de Ensino", true, true, true, true);
        PlanoDeEnsinoPanel panel = new PlanoDeEnsinoPanel(this);
        getContentPane().add(panel);
        pack();
    }
    
}
