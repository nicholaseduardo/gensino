/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.frame;

import ensino.configuracoes.view.panels.CalendarioPanel;
import javax.swing.JInternalFrame;

/**
 *
 * @author nicho
 */
public class FrameCalendario extends JInternalFrame {
    
    public FrameCalendario() {
        super("Calendários", true, true, true, true);
        CalendarioPanel panel = new CalendarioPanel(this);
        getContentPane().add(panel);
        setSize(680, 680);
    }
    
}
