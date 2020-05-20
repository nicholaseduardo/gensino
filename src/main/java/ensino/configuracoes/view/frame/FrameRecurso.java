/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.frame;

import ensino.configuracoes.view.panels.RecursoPanel;
import javax.swing.JInternalFrame;

/**
 *
 * @author nicho
 */
public class FrameRecurso extends JInternalFrame {
    
    public FrameRecurso() {
        super("Recursos", true, true, true, true);
        RecursoPanel panel = new RecursoPanel(this);
        getContentPane().add(panel);
        pack();
    }
    
}
