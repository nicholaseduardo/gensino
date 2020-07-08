/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.components;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author santos
 */
public abstract class GenTreeModel<T> extends DefaultTreeModel {
    
//    protected DefaultTreeModel model;

    protected List<T> lista;
    
    public GenTreeModel() {
        this(new ArrayList(), new DefaultMutableTreeNode("Content"));
    }

    public GenTreeModel(List<T> lista, DefaultMutableTreeNode rootNode) {
        super(rootNode);
        
//        model = new DefaultTreeModel((DefaultMutableTreeNode) getRoot());
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

//    public TreeNode[] getPathToRoot(TreeNode aNode) {
//        return model.getPathToRoot(aNode);
//    }
//
//    public void insertNodeInto(JTree tree, MutableTreeNode child, MutableTreeNode parent, Integer index) {
//        model.insertNodeInto(child, parent, index);
//
//        TreeNode[] nodes = model.getPathToRoot(child);
//        TreePath path = new TreePath(nodes);
//        tree.scrollPathToVisible(path);
//        tree.setSelectionPath(path);
//        tree.startEditingAtPath(path);
//    }
//
//    public void removeNodeFromParent(MutableTreeNode node) {
//        model.removeNodeFromParent(node);
//    }
//
//    @Override
//    public Object getChild(Object o, int i) {
//        return model.getChild(o, i);
//    }
//
//    @Override
//    public int getChildCount(Object o) {
//        return model.getChildCount(o);
//    }
//
//    @Override
//    public boolean isLeaf(Object node) {
//        return model.isLeaf(node);
//    }
//
//    @Override
//    public void valueForPathChanged(TreePath path, Object newValue) {
//        model.valueForPathChanged(path, newValue);
//    }
//
//    @Override
//    public int getIndexOfChild(Object parent, Object child) {
//        return model.getIndexOfChild(parent, child);
//    }
//
//    @Override
//    public void addTreeModelListener(TreeModelListener l) {
//        model.addTreeModelListener(l);
//    }
//
//    @Override
//    public void removeTreeModelListener(TreeModelListener l) {
//        model.removeTreeModelListener(l);
//    }
    
}
