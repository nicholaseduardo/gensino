/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.frame;

import ensino.configuracoes.view.panels.CursoPanel;
import javax.swing.JInternalFrame;

/**
 *
 * @author nicho
 */
public class FrameCursos extends JInternalFrame {
    
    public FrameCursos() {
        super("Cursos", true, true, true, true);
        CursoPanel panel = new CursoPanel(this);
        getContentPane().add(panel);
        pack();
    }
    
}
