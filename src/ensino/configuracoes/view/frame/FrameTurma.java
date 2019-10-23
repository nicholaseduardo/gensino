/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.frame;

import ensino.configuracoes.view.panels.turma.TurmaPanel;
import javax.swing.JInternalFrame;

/**
 *
 * @author nicho
 */
public class FrameTurma extends JInternalFrame {
    
    public FrameTurma() {
        super("Turmas", true, true, true, true);
        TurmaPanel panel = new TurmaPanel(this);
        getContentPane().add(panel);
        pack();
    }
    
}
