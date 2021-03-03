/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.components.GenTreeModel;
import ensino.components.ToolTipTreeNode;
import ensino.configuracoes.model.Conteudo;
import ensino.patterns.factory.ControllerFactory;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author santos
 */
public class ConteudoTreeModel extends GenTreeModel<Conteudo> {

    public ConteudoTreeModel(List<Conteudo> lista) {
        super(lista, new DefaultMutableTreeNode("Conteúdo"));
    }

    /**
     *
     * @throws Exception
     */
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
                        try {
                            insertNodeInto(node, parentNode, conteudo.getSequencia());
                        } catch (ArrayIndexOutOfBoundsException ex) {
                            try {
                                /**
                                 * Força a correção do indice do conteúdo colocando-o
                                 * em última posição
                                 */
                                conteudo.setSequencia(parentNode.getChildCount()-1);
                                ControllerFactory.createConteudoController().salvar(conteudo);
                                insertNodeInto(node, parentNode, conteudo.getSequencia());
                            } catch (Exception ex1) {
                                Logger.getLogger(ConteudoTreeModel.class.getName()).log(Level.SEVERE, null, ex1);
                                ex1.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

}
