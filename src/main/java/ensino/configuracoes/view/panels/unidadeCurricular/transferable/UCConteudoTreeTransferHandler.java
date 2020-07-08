/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.unidadeCurricular.transferable;

import ensino.components.GenJTree;
import ensino.components.GenTreeModel;
import ensino.configuracoes.model.Conteudo;
import ensino.defaults.GensinoTransferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
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
public class UCConteudoTreeTransferHandler extends TransferHandler {

    private final DataFlavor flavors[] = {GensinoTransferable.CONTEUDO_FLAVOR};

    private TreePath[] indexes;

    public UCConteudoTreeTransferHandler() {
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        support.setShowDropLocation(true);

        // for the demo, we will only support drops (not clipboard paste)
        if (!support.isDrop()) {
            return false;
        }

        if (!support.isDataFlavorSupported(flavors[0])) {
            return false;
        }

        // fetch the drop location
        JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
        TreePath path = dl.getPath();

        // tem que informar pelo menos um nó da árvore
        if (path == null) {
            return false;
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

        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) dest.getLastPathComponent();
        GenTreeModel<Conteudo> model = (GenTreeModel) tree.getModel();
        // Configure for drop mode
        int index = childIndex;     // DropMode.INSERT
        if (childIndex == -1) {     // DropMode.ON
            index = parent.getChildCount();
        }

        // Adiciona os dados no modelo
        for (int i = 0; i < data.length; i++) {
            DefaultMutableTreeNode child = new DefaultMutableTreeNode(data[i]);
            model.insertNodeInto(child, parent, index++);
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
            Object[] aConteudo = new Object[selectedCount];
            for (int i = 0; i < selectedCount; i++) {
                TreePath tp = indexes[i];
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tp.getLastPathComponent();
                if (node.getUserObject() instanceof Conteudo) {
                    displayDropLocation("Não é possível mover um Conteúdo da Árvore");
                    return null;
                }
                aConteudo[i] = node.getUserObject();
            }

            return new GensinoTransferable(aConteudo);
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
             * A remoção dos índices deve ser realizada de forma decresente para
             * não alterar os índices da tabela.
             */
            DefaultMutableTreeNode parentNode = null;
            for (int i = indexes.length - 1; i >= 0; i--) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) indexes[i].getLastPathComponent();
                model.removeNodeFromParent(node);
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
