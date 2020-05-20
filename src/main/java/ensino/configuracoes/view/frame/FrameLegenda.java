/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.frame;

import ensino.configuracoes.view.panels.LegendaPanel;
import javax.swing.JInternalFrame;

/**
 *
 * @author nicho
 */
public class FrameLegenda extends JInternalFrame {
    
    public FrameLegenda() {
        super("Legendas", true, true, true, true);
        LegendaPanel panel = new LegendaPanel(this);
        getContentPane().add(panel);
        pack();
    }
    
}
