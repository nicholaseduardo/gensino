    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.frame;

import ensino.configuracoes.view.panels.InstrumentoAvaliacaoPanel;
import javax.swing.JInternalFrame;

/**
 *
 * @author nicho
 */
public class FrameInstrumentoAvaliacao extends JInternalFrame {
    
    public FrameInstrumentoAvaliacao() {
        super("Instrumentos Avaliativos", true, true, true, true);
        InstrumentoAvaliacaoPanel panel = new InstrumentoAvaliacaoPanel(this);
        getContentPane().add(panel);
        pack();
    }
    
}
