/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.components.GenTreeModel;
import ensino.components.ToolTipTreeNode;
import ensino.configuracoes.model.Conteudo;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author santos
 */
public class ConteudoTreeModel extends GenTreeModel<Conteudo> {

    public ConteudoTreeModel(List<Conteudo> lista) {
        super(lista, new DefaultMutableTreeNode("Conteúdo"));
    }

    @Override
    protected void loadTree() {
        if (!lista.isEmpty()) {
            for (Conteudo conteudo : lista) {
                ToolTipTreeNode node = null;
                if (!conteudo.hasParent()) {
                    /**
                     * Se não tem parent, adiciona no ROOT
                     */
                    node = new ToolTipTreeNode(conteudo.getDescricao());
                    node.setUserObject(conteudo);
                    insertNodeInto(node, (DefaultMutableTreeNode) getRoot(), conteudo.getSequencia());
                } else {
                    /**
                     * Se tem parent, localiza-o e o adiciona na ordem da
                     * sequencia
                     */
                    Conteudo oParent = conteudo.getConteudoParent();
                    ToolTipTreeNode parentNode = (ToolTipTreeNode) searchNode(oParent);
                    node = new ToolTipTreeNode(conteudo.getDescricao());
                    node.setUserObject(conteudo);

                    if (conteudo.getSequencia() != null) {
                        insertNodeInto(node, parentNode, conteudo.getSequencia());
                    }
                }
            }
        }
    }

}
