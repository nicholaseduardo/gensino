/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.config.transferable;

import ensino.defaults.GensinoTransferable;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

/**
 *
 * @author santos
 */
public class ListTransferHandler extends TransferHandler {

    private final Class baseclass;
    private final DataFlavor flavors[] = {GensinoTransferable.BASEOBJECT_FLAVOR};
    private int[] indexes;

    public ListTransferHandler(Class base) {
        super();
        this.baseclass = base;
    }

    public int getSourceActions(JComponent comp) {
        return COPY;
    }

    public boolean canImport(TransferHandler.TransferSupport support) {
        // for the demo, we'll only support drops (not clipboard paste)
        if (!support.isDrop()) {
            return false;
        }

        if (!support.isDataFlavorSupported(flavors[0])) {
            return false;
        }
        
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
        if (baseclass != data[0].getClass()) {
            return false;
        }

        return true;
    }

    public boolean importData(TransferHandler.TransferSupport support) {
        // if we can't handle the import, say so
        if (!canImport(support)) {
            return false;
        }

        // fetch the drop location
        JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();

        int index = dl.getIndex();

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

        if (comp instanceof JList) {
            JList list = (JList) support.getComponent();
            DefaultListModel model = (DefaultListModel) list.getModel();

            for (int i = 0; i < data.length; i++) {
                if (baseclass == data[i].getClass()) {
                    model.insertElementAt(data[i], index++);
                } else {
                    return false;
                }
            }

            Rectangle rect = list.getCellBounds(index, index);
            list.scrollRectToVisible(rect);
            list.setSelectedIndex(index);
            list.requestFocusInWindow();

            return true;
        }
        return false;
    }

    public Transferable createTransferable(JComponent comp) {
        if (comp instanceof JList) {
            JList l = (JList) comp;

            indexes = l.getSelectedIndices();
            Integer selectedCount = indexes.length;
            if (selectedCount == 0) {
                return null;
            }

            DefaultListModel model = (DefaultListModel) l.getModel();
            Object[] aConteudo = new Object[selectedCount];
            for (int i = 0; i < selectedCount; i++) {
                aConteudo[i] = model.get(indexes[i]);
            }

            return new GensinoTransferable(aConteudo);
        }
        return null;
    }

    public void exportDone(JComponent comp, Transferable trans, int action) {
        if (action != COPY) {
            return;
        }
    }
}
