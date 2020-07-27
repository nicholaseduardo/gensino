/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.frame;

import ensino.configuracoes.model.UnidadeCurricular;
import ensino.planejamento.view.panels.PlanoDeEnsinoPanel;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;

/**
 *
 * @author santos
 */
public class FrameViewPlanoDeEnsino extends JInternalFrame {
    
    public FrameViewPlanoDeEnsino(UnidadeCurricular unidadeCurricular) {
        super("Planos de Ensino", true, true, true, true);
        PlanoDeEnsinoPanel panel = new PlanoDeEnsinoPanel(this, unidadeCurricular);
        JScrollPane scroll = new JScrollPane(panel);
        scroll.setAutoscrolls(true);
        getContentPane().add(panel);
    }
    
}
