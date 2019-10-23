/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.frame;

import ensino.configuracoes.model.Bibliografia;
import ensino.configuracoes.view.panels.BibliografiaPanel;

/**
 *
 * @author nicho
 */
public class FrameBibliografia extends javax.swing.JInternalFrame {

    private BibliografiaPanel bibliografiaPanel;
    private Bibliografia selectedItem;

    public FrameBibliografia() {
        super("Bibliografia", true, true, true, true);
        initComponents();
    }

    private void initComponents() {
        bibliografiaPanel = new BibliografiaPanel(this);
        getContentPane().add(bibliografiaPanel);
        pack();
    }
    
    public Bibliografia getSelectedItem() {
        return selectedItem;
    }

}
