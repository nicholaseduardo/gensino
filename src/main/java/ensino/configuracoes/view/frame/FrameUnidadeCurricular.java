/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.frame;

import ensino.configuracoes.view.panels.unidadeCurricular.UnidadeCurricularPanel;
import javax.swing.JInternalFrame;

/**
 *
 * @author nicho
 */
public class FrameUnidadeCurricular extends JInternalFrame {
    
    public FrameUnidadeCurricular() {
        super("Unidades Curriculares", true, true, true, true);
        UnidadeCurricularPanel panel = new UnidadeCurricularPanel(this);
        getContentPane().add(panel);
        pack();
    }
    
}
