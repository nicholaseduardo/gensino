/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.unidadeCurricular.transferable;

import ensino.components.GenJTree;
import ensino.components.GenTreeModel;
import ensino.configuracoes.model.Conteudo;
import ensino.configuracoes.model.ObjetivoUC;
import ensino.configuracoes.model.ObjetivoUCConteudo;
import ensino.configuracoes.model.ObjetivoUCConteudoFactory;
import ensino.defaults.GensinoTransferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author santos
 */
public class ObjetivoUCConteudoTreeTransferHandler extends TransferHandler {

    private final DataFlavor flavors[] = {GensinoTransferable.OBJETIVOUC_FLAVOR,
        GensinoTransferable.CONTEUDO_FLAVOR};

    private TreePath[] indexes;

    public ObjetivoUCConteudoTreeTransferHandler() {
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        support.setShowDropLocation(true);

        // for the demo, we will only support drops (not clipboard paste)
        if (!support.isDrop()) {
            return false;
        }

        if (!support.isDataFlavorSupported(flavors[0])
                || !support.isDataFlavorSupported(flavors[1])) {
            return false;
        }

        // fetch the drop location
        JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
        TreePath path = dl.getPath();

        // tem que informar pelo menos um nó da árvore
        if (path == null) {
            return false;
        }

        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) path.getLastPathComponent();
        Object parentObject = parent.getUserObject();
        /**
         * Aceita somente se destino for ObjetivoUC
         */
        if (parentObject instanceof Conteudo) {
            return false;
        }
        // fetch the data and bail if this fails
        Object[] data;
        try {
            data = (Object[]) support.getTransferable().getTransferData(flavors[0]);
        } catch (UnsupportedFlavorException e) {
            System.out.println("UnsupportedFlavor: " + e.getMessage());
            return false;
        } catch (java.io.IOException e) {
            System.out.println("I/O error: " + e.getMessage());
            return false;
        }

        /**
         * Conteudo pode ser adicionado somente em objetivos
         */
        for (int i = 0; i < data.length; i++) {
            Object oData = data[i];
            if ((parentObject instanceof ObjetivoUC && oData instanceof ObjetivoUC)
                    || (parentObject instanceof String && oData instanceof Conteudo)) {
                return false;
            } else if (parentObject instanceof ObjetivoUC && oData instanceof Conteudo) {
                Conteudo conteudo = (Conteudo) oData;
                /**
                 * Evitar duplicidade de conteudos no nó do ObjetivoUC
                 */
                for (int j = 0; j < parent.getChildCount(); j++) {
                    DefaultMutableTreeNode child = (DefaultMutableTreeNode) parent.getChildAt(j);
                    Conteudo childConteudo = (Conteudo)child.getUserObject();
                    if (conteudo.equals(childConteudo)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport support) {
        // if we cannot handle the import, say so
        if (!canImport(support)) {
            return false;
        }

        // fetch the data and bail if this fails
        Object[] data;
        try {
            data = (Object[]) support.getTransferable().getTransferData(flavors[0]);
        } catch (UnsupportedFlavorException e) {
            System.out.println("UnsupportedFlavor: " + e.getMessage());
            return false;
        } catch (java.io.IOException e) {
            System.out.println("I/O error: " + e.getMessage());
            return false;
        }

        JTree tree = (JTree) support.getComponent();
        int childIndex = -1;
        TreePath dest;
        if (support.isDrop()) {
            // fetch the drop location
            JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
            childIndex = dl.getChildIndex();
            dest = dl.getPath();
        } else {
            dest = tree.getSelectionPath();
        }

        DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) dest.getLastPathComponent();

        // Configure for drop mode
        int index = childIndex;     // DropMode.INSERT
        if (childIndex == -1) {     // DropMode.ON
            index = parentNode.getChildCount();
        }

        // Adiciona os dados no modelo
        GenTreeModel<Conteudo> model = (GenTreeModel) tree.getModel();
        for (int i = 0; i < data.length; i++) {
            Object nodeObject = data[i];
            Object parentObject = parentNode.getUserObject();
            /**
             * Adiciona o Conteúdo ao ObjetivoUC antes de colocá-lo na árvore
             */
            if (parentObject instanceof ObjetivoUC && nodeObject instanceof Conteudo) {
                ObjetivoUC objetivoUC = (ObjetivoUC) parentObject;
                ObjetivoUCConteudo ouc = ObjetivoUCConteudoFactory.getInstance().createObject(objetivoUC,
                        (Conteudo) nodeObject, index);
                objetivoUC.addConteudo(ouc);
            }

            DefaultMutableTreeNode node = new DefaultMutableTreeNode(nodeObject);
            model.insertNodeInto(node, parentNode, index++);
            /**
             * Se o objeto a ser adicionado for um ObjetivoUC, deve-se trazer,
             * também, os conteúdos vinculados a ele
             */
            if (parentObject instanceof String && nodeObject instanceof ObjetivoUC) {
                ObjetivoUC objetivoUC = (ObjetivoUC) nodeObject;
                for (int j = 0; j < objetivoUC.getConteudos().size(); j++) {
                    ObjetivoUCConteudo ouc = objetivoUC.getConteudos().get(j);
                    DefaultMutableTreeNode child = new DefaultMutableTreeNode(ouc.getConteudo());
                    model.insertNodeInto(child, node, j);
                }
            }

        }

        return true;
    }

    /**
     * * ----------- **
     */
    public int getSourceActions(JComponent comp) {
        return COPY_OR_MOVE;
    }

    public Transferable createTransferable(JComponent comp) {
        GenJTree t = null;
        if (comp instanceof GenJTree) {
            t = (GenJTree) comp;

            Integer selectedCount = t.getSelectionCount();
            if (selectedCount < 0 || selectedCount > t.getRowCount()) {
                return null;
            }

            indexes = t.getSelectionPaths();
            List<Object> lData = new ArrayList();
            for (int i = 0; i < selectedCount; i++) {
                TreePath tp = indexes[i];
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tp.getLastPathComponent();
                lData.add(node.getUserObject());

                if (node.getUserObject() instanceof Conteudo
                        && !node.isLeaf()) {
                    displayDropLocation("Só é possível mover o Conteúdo no\núltimo nível da Árvore");
                    return null;
                }
            }

            return new GensinoTransferable(lData.toArray());
        }
        return null;
    }

    public void exportDone(JComponent comp, Transferable trans, int action) {
        if (action != MOVE) {
            return;
        }

        if (comp instanceof GenJTree) {
            GenJTree t = (GenJTree) comp;
            GenTreeModel model = (GenTreeModel) t.getModel();
            /**
             * A remoção dos índices deve ser realizada somente para objetivos e
             * de forma decresente para não alterar os índices da tabela.
             */
            DefaultMutableTreeNode parentNode = null, node = null;
            for (int i = indexes.length - 1; i >= 0; i--) {
                node = (DefaultMutableTreeNode) indexes[i].getLastPathComponent();
                parentNode = (DefaultMutableTreeNode) node.getParent();
                Object child = node.getUserObject();
                Object parent = parentNode.getUserObject();

                if ((child instanceof ObjetivoUC)
                        || (parent instanceof ObjetivoUC
                        && child instanceof Conteudo)) {
                    model.removeNodeFromParent(node);
                }
            }
        }
    }

    private void displayDropLocation(final String string) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(null, string);
            }
        });
    }

}
