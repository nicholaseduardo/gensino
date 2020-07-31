/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.components;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author santos
 */
public abstract class GenTreeModel<T> extends DefaultTreeModel {

    protected List<T> lista;
    
    public GenTreeModel() {
        this(new ArrayList(), new DefaultMutableTreeNode("Content"));
    }

    public GenTreeModel(List<T> lista, DefaultMutableTreeNode rootNode) {
        super(rootNode);
        
        this.lista = lista;
        loadTree();
    }

    public DefaultMutableTreeNode searchNode(T o) {
        DefaultMutableTreeNode node = null;
        Enumeration e = ((DefaultMutableTreeNode)getRoot()).breadthFirstEnumeration();
        while (e.hasMoreElements()) {
            Object obj = e.nextElement();
            node = (DefaultMutableTreeNode) obj;
            
            if (o.equals(node.getUserObject())) {
                return node;
            }
        }
        return null;
    }

    protected abstract void loadTree();
    
}
