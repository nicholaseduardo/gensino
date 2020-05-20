/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.frame;

import ensino.configuracoes.view.panels.DocentePanel;
import javax.swing.JInternalFrame;

/**
 *
 * @author nicho
 */
public class FrameDocente extends JInternalFrame {
    
    public FrameDocente() {
        super("Docentes", true, true, true, true);
        DocentePanel panel = new DocentePanel(this);
        getContentPane().add(panel);
        pack();
    }
    
}
