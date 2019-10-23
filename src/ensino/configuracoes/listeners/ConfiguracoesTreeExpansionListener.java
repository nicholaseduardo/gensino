/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.listeners;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.TreePath;

/**
 *
 * @author nicho
 */
public class ConfiguracoesTreeExpansionListener implements TreeExpansionListener {

    @Override
    public void treeExpanded(TreeExpansionEvent event) {
        TreePath tp = event.getPath();
        System.out.println("Expansion: " + tp.getLastPathComponent());
    }

    @Override
    public void treeCollapsed(TreeExpansionEvent event) {
        TreePath tp = event.getPath();
        System.out.println("Collapse: " + tp.getLastPathComponent());
    }

}
