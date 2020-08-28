/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.components.GenTreeModel;
import ensino.configuracoes.model.ObjetivoUC;
import ensino.configuracoes.model.ObjetivoUCConteudo;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author santos
 */
public class ObjetivoUCTreeModel extends GenTreeModel<ObjetivoUC> {

    public ObjetivoUCTreeModel(List<ObjetivoUC> lista) {
        super(lista, new DefaultMutableTreeNode("Objetivos"));
    }

    @Override
    protected void loadTree() {
        if (!lista.isEmpty()) {
            for (int i = 0; i < lista.size(); i++) {
                ObjetivoUC o = lista.get(i);
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(o.getDescricao());
                node.setUserObject(o);
                /**
                 * Adiciona o objetivo no nó principal
                 */
                insertNodeInto(node, (DefaultMutableTreeNode) getRoot(), i);
                /**
                 * Adiciona os conteúdos relacionados ao objetivo
                 */
                List<ObjetivoUCConteudo> lOCC= o.getConteudos();
                if (!lOCC.isEmpty()) {
                    for(int j = 0; j < lOCC.size(); j++) {
                        ObjetivoUCConteudo oucc = lOCC.get(j);
                        DefaultMutableTreeNode child = new DefaultMutableTreeNode(oucc.getConteudo().getDescricao());
                        child.setUserObject(oucc);
                        
                        insertNodeInto(child, node, j);
                    }
                }
            }
        }
    }

}
