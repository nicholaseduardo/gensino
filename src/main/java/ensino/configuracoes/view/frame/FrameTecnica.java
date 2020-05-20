/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.frame;

import ensino.configuracoes.view.panels.TecnicaPanel;
import javax.swing.JInternalFrame;

/**
 *
 * @author nicho
 */
public class FrameTecnica extends JInternalFrame {
    
    public FrameTecnica() {
        super("Técnicas", true, true, true, true);
        TecnicaPanel panel = new TecnicaPanel(this);
        getContentPane().add(panel);
        pack();
    }
    
}
