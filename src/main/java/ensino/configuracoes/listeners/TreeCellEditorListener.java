/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.listeners;

import ensino.configuracoes.model.Conteudo;
import ensino.configuracoes.model.ObjetivoUC;
import ensino.patterns.factory.ControllerFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.TreeCellEditor;

/**
 *
 * @author santos
 */
public class TreeCellEditorListener implements CellEditorListener {

    @Override
    public void editingStopped(ChangeEvent ce) {
        Object source = ce.getSource();
        if (source instanceof TreeCellEditor) {
            TreeCellEditor cellEditor = (TreeCellEditor) source;
            Object value = cellEditor.getCellEditorValue();
            try {
                if (value instanceof ObjetivoUC) {
                    ControllerFactory.createObjetivoUCController().salvar((ObjetivoUC) value);
                } else if (value instanceof Conteudo) {
                    ControllerFactory.createConteudoController().salvar((Conteudo) value);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(TreeCellEditorListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void editingCanceled(ChangeEvent ce) {

    }
}
