/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.renderer;

import ensino.components.GenJPanel;
import ensino.components.GenJTextArea;
import ensino.components.GenJTree;
import ensino.configuracoes.model.ObjetivoUC;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;

/**
 *
 * @author santos
 */
public class ObjetivoUCTreeCellEditor extends AbstractCellEditor implements TreeCellEditor {

    private final DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
    private final GenJTextArea txtDescricao = new GenJTextArea(3, 30);
    private final GenJPanel p = new GenJPanel(new BorderLayout());
    private String str = null;
    private ObjetivoUC objetivoUC;

    public ObjetivoUCTreeCellEditor(GenJTree tree) {
        super();
        
        txtDescricao.addFocusListener(new TextFocusEvent(tree));

        p.setFocusable(false);
        p.setRequestFocusEnabled(false);
        p.setOpaque(false);
        p.add(txtDescricao, BorderLayout.WEST);
        txtDescricao.setOpaque(false);
    }

    @Override
    public Object getCellEditorValue() {
        return objetivoUC;
    }

    @Override
    public boolean isCellEditable(EventObject event) {
        boolean returnValue = false;
        if (event instanceof MouseEvent) {
            MouseEvent mouseEvent = (MouseEvent) event;
            return mouseEvent.getClickCount() == 2;
        }
        return returnValue;
    }

    @Override
    public Component getTreeCellEditorComponent(JTree tree, Object value,
            boolean isSelected, boolean expanded, boolean leaf, int row) {
        JLabel l = (JLabel) renderer.getTreeCellRendererComponent(tree, value,
                true, expanded, leaf, row, true);

        if (value instanceof DefaultMutableTreeNode) {
            Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
            if (userObject instanceof ObjetivoUC) {
                objetivoUC = (ObjetivoUC) userObject;
                str = objetivoUC.getDescricao();
                txtDescricao.setText(str);
            }
            return p;
        }
        return l;
    }
    
    private class TextFocusEvent implements FocusListener {

        private GenJTree tree;

        public TextFocusEvent(GenJTree tree) {
            this.tree = tree;
        }

        @Override
        public void focusGained(FocusEvent fe) {
            
        }

        @Override
        public void focusLost(FocusEvent fe) {
            Object source = fe.getSource();
            if (source instanceof GenJTextArea) {
                GenJTextArea txt = (GenJTextArea) source;

                String value = txt.getText();
                Object obj = tree.getLastSelectedPathComponent();
                if (obj instanceof DefaultMutableTreeNode) {
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) obj;
                    
                    Object o = selectedNode.getUserObject();
                    if (o instanceof ObjetivoUC) {
                        ObjetivoUC objetivoUC = (ObjetivoUC) o;
                        objetivoUC.setDescricao(value);
                    }
                }
            }
            ObjetivoUCTreeCellEditor.super.stopCellEditing();
        }

    }
}
