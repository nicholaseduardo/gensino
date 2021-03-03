/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.transferable;

import ensino.components.GenJTree;
import ensino.components.GenTreeModel;
import ensino.configuracoes.model.Conteudo;
import ensino.configuracoes.model.SemanaLetiva;
import ensino.defaults.GensinoTransferable;
import ensino.patterns.BaseObject;
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
public class TreeTransferHandler extends TransferHandler {

    private final DataFlavor flavors[] = {
        GensinoTransferable.CONTEUDO_FLAVOR,
        GensinoTransferable.OBJETIVOUC_FLAVOR,
        GensinoTransferable.SEMANALETIVA_FLAVOR,
        GensinoTransferable.BASEOBJECT_FLAVOR};

    private TreePath[] indexes;
    private Boolean canMoveConteudo;

    public TreeTransferHandler(Boolean canMoveConteudo) {
        this.canMoveConteudo = canMoveConteudo;
    }

    public TreeTransferHandler() {
        this(Boolean.FALSE);
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        support.setShowDropLocation(true);

        // for the demo, we will only support drops (not clipboard paste)
        if (!support.isDrop()) {
            return false;
        }

        for (int i = 0; i < flavors.length; i++) {
            if (!support.isDataFlavorSupported(flavors[i])) {
                return false;
            }
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
         * Regras para aceitar o vínculo de objetos na árvore
         */
        for (int i = 0; i < data.length; i++) {
            Object oData = data[i];
            if (parentObject instanceof SemanaLetiva && oData instanceof SemanaLetiva
                    || parentObject instanceof String && oData instanceof SemanaLetiva
                    || parentObject instanceof BaseObject && oData instanceof BaseObject
                    || parentObject instanceof BaseObject && oData instanceof SemanaLetiva) {
                return false;
            } else if (oData instanceof BaseObject) {
                BaseObject baseObject = (BaseObject) oData;
                /**
                 * Evitar duplicidade de metodologia (BaseObject) nos nós
                 */
                for (int j = 0; j < parent.getChildCount(); j++) {
                    DefaultMutableTreeNode child = (DefaultMutableTreeNode) parent.getChildAt(j);
                    Object childConteudo = child.getUserObject();
                    if (childConteudo instanceof BaseObject &&
                            baseObject.equals(childConteudo)) {
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
                if (!canMoveConteudo && node.getUserObject() instanceof Conteudo) {
                    displayDropLocation("Não é possível mover um Conteúdo da Árvore");
                    return null;
                }
                aConteudo[i] = node.getUserObject();
            }

            return new GensinoTransferable(aConteudo);
        }
        return null;
    }

    @Override
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
