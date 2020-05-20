/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.frame;

import ensino.planejamento.view.panels.ConfigPlanoDeEnsinoPanel;
import javax.swing.JInternalFrame;

/**
 *
 * @author nicho
 */
public class FramePlanoDeEnsino extends JInternalFrame {
    
    public FramePlanoDeEnsino() {
        super("Planos de Ensino", true, true, true, true);
        ConfigPlanoDeEnsinoPanel panel = new ConfigPlanoDeEnsinoPanel();
        getContentPane().add(panel);
        pack();
    }
    
}
