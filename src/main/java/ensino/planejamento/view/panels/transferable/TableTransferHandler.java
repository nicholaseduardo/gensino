/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.config.transferable;

import ensino.defaults.DefaultTableModel;
import ensino.defaults.GensinoTransferable;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

/**
 *
 * @author santos
 */
public class TableTransferHandler extends TransferHandler {

    private final DataFlavor flavors[] = {GensinoTransferable.SEMANALETIVA_FLAVOR};
    private int[] indexes;

    public TableTransferHandler() {
        super();
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

        return true;
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport support) {
        // if we cannot handle the import, say so
        if (!canImport(support)) {
            return false;
        }

        // fetch the drop location
        JTable.DropLocation dl = (JTable.DropLocation) support.getDropLocation();

        int row = dl.getRow();

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

        Component comp = support.getComponent();

        if (comp instanceof JTable) {
            JTable t = (JTable) comp;
            DefaultTableModel model = (DefaultTableModel) t.getModel();

            for (int i = 0; i < data.length; i++) {
                model.addRow(data[i]);
            }

            Rectangle rect = t.getCellRect(row, 0, false);
            if (rect != null) {
                t.scrollRectToVisible(rect);
            }

            return true;
        }

        return false;
    }

    /**
     * * -------- **
     */
    public int getSourceActions(JComponent comp) {
        return COPY_OR_MOVE;
    }

    public Transferable createTransferable(JComponent comp) {
        if (comp instanceof JTable) {
            JTable t = (JTable) comp;

            Integer selectedCount = t.getSelectedRowCount();
            if (selectedCount < 0 || selectedCount > t.getRowCount()) {
                return null;
            }

            indexes = t.getSelectedRows();
            DefaultTableModel model = (DefaultTableModel) t.getModel();
            Object[] aConteudo = new Object[selectedCount];
            for (int i = 0; i < selectedCount; i++) {
                aConteudo[i] = model.getRow(indexes[i]);
            }

            return new GensinoTransferable(aConteudo);
        }
        return null;
    }

    public void exportDone(JComponent comp, Transferable trans, int action) {
        if (action != MOVE) {
            return;
        }

        if (comp instanceof JTable) {
            JTable t = (JTable) comp;
            DefaultTableModel model = (DefaultTableModel) t.getModel();
            /**
             * A remoção dos índices deve ser realizada de forma decresente para
             * não alterar os índices da tabela.
             */
            for (int i = indexes.length - 1; i >= 0; i--) {
                model.removeRow(indexes[i]);
            }
        }
    }
}
