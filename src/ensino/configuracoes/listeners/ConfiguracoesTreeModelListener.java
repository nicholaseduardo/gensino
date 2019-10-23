/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.listeners;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author nicho
 */
public class ConfiguracoesTreeModelListener implements TreeModelListener {

    @Override
    public void treeNodesChanged(TreeModelEvent e) {
        TreePath tp = e.getTreePath();
        Object[] children = e.getChildren();
        DefaultMutableTreeNode changedNode;
        if (children != null) {
            changedNode = (DefaultMutableTreeNode) children[0];
        } else {
            changedNode = (DefaultMutableTreeNode) tp.getLastPathComponent();
        }

        System.out.println("Model change path: " + tp + "New data: "
                + changedNode.getUserObject());
    }

    @Override
    public void treeNodesInserted(TreeModelEvent e) {

    }

    @Override
    public void treeNodesRemoved(TreeModelEvent e) {

    }

    @Override
    public void treeStructureChanged(TreeModelEvent e) {

    }

}
