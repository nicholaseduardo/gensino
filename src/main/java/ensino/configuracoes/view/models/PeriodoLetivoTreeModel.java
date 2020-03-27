/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.model.PeriodoLetivo;
import java.util.List;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author nicho
 */
public class PeriodoLetivoTreeModel implements TreeModel {

    private DefaultTreeModel modeloInterno;
    private DefaultMutableTreeNode noRaiz;

    private List<PeriodoLetivo> listaPeriodos;

    public PeriodoLetivoTreeModel(List<PeriodoLetivo> lista) {
        super();
        this.listaPeriodos = lista;
        initComponents();
    }

    public PeriodoLetivoTreeModel() {
        this(null);
    }

    private void initComponents() {
        noRaiz = new DefaultMutableTreeNode("Períodos Letivos");
        modeloInterno = new DefaultTreeModel(noRaiz);
        reloadTree();
    }

    public PeriodoLetivoTreeModel setData(List<PeriodoLetivo> lista) {
        this.listaPeriodos = lista;
        return this;
    }

    public void reloadTree() {
        noRaiz.removeAllChildren();
        modeloInterno.reload();

        for (int i = 0; i < listaPeriodos.size(); i++) {
            PeriodoLetivo periodoLetivo = listaPeriodos.get(i);
            
            DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(periodoLetivo);
            modeloInterno.insertNodeInto(treeNode, noRaiz, i);
        }
    }

    @Override
    public Object getRoot() {
        return modeloInterno.getRoot();
    }

    @Override
    public Object getChild(Object parent, int index) {
        return modeloInterno.getChild(parent, index);
    }

    @Override
    public int getChildCount(Object parent) {
        return modeloInterno.getChildCount(parent);
    }

    @Override
    public boolean isLeaf(Object node) {
        return modeloInterno.isLeaf(node);
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        modeloInterno.valueForPathChanged(path, newValue);
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return modeloInterno.getIndexOfChild(parent, child);
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        modeloInterno.addTreeModelListener(l);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        modeloInterno.removeTreeModelListener(l);
    }

}
