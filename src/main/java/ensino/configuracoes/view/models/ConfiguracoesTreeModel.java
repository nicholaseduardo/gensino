/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author nicho
 */
public class ConfiguracoesTreeModel implements TreeModel {

    private DefaultTreeModel modeloInterno;
    private DefaultMutableTreeNode noRaiz = new DefaultMutableTreeNode("Configurações");
    private DefaultMutableTreeNode legendaNode = new DefaultMutableTreeNode("Legenda");
    private DefaultMutableTreeNode metodologiaNode = new DefaultMutableTreeNode("Metodologias");
    private DefaultMutableTreeNode recursoNode = new DefaultMutableTreeNode("Recurso");
    private DefaultMutableTreeNode tecnicaNode = new DefaultMutableTreeNode("Técnica");
    private DefaultMutableTreeNode instrumentoNode = new DefaultMutableTreeNode("Instrumento de Avaliação");
    private DefaultMutableTreeNode docenteNode = new DefaultMutableTreeNode("Docente");
    private DefaultMutableTreeNode nivelEnsinoNode = new DefaultMutableTreeNode("Nível de Ensino");

    public ConfiguracoesTreeModel() {
        modeloInterno = new DefaultTreeModel(noRaiz);
        int rootIndex = 0, geralIndex = 0, metodoIndex = 0;

        DefaultMutableTreeNode geraisNode = new DefaultMutableTreeNode("Dados gerais");
        modeloInterno.insertNodeInto(geraisNode, noRaiz, rootIndex++);
        // docente
        modeloInterno.insertNodeInto(docenteNode, geraisNode, geralIndex++);
        // legenda
        modeloInterno.insertNodeInto(legendaNode, geraisNode, geralIndex++);
        // niveis de ensino
        modeloInterno.insertNodeInto(nivelEnsinoNode, geraisNode, geralIndex++);

        // Metodologia
        modeloInterno.insertNodeInto(metodologiaNode, noRaiz, rootIndex++);
        modeloInterno.insertNodeInto(recursoNode, metodologiaNode, metodoIndex++);
        modeloInterno.insertNodeInto(tecnicaNode, metodologiaNode, metodoIndex++);
        modeloInterno.insertNodeInto(instrumentoNode, metodologiaNode, metodoIndex++);
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
