/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.frame;

import ensino.configuracoes.view.panels.nivelEnsino.NivelEnsinoPanel;
import javax.swing.JInternalFrame;

/**
 *
 * @author nicho
 */
public class FrameNivelEnsino extends JInternalFrame {
    
    public FrameNivelEnsino() {
        super("Níveis de Ensino", true, true, true, true);
        NivelEnsinoPanel panel = new NivelEnsinoPanel(this);
        getContentPane().add(panel);
        pack();
    }
    
}
