/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicComboBoxEditor;

/**
 *
 * @author nicho
 */
public class GenJComboBox extends JComboBox {

    public GenJComboBox() {
        super();
        initComponents();
    }

    public GenJComboBox(Object[] items) {
        super(items);
        initComponents();
    }

    public GenJComboBox(ComboBoxModel model) {
        super(model);
        initComponents();
    }

    private void initComponents() {
        setEditor(new GenJComboBoxItemEditor());
        resetFontSize(16);
    }

    /**
     * Atualiza o tamanho da fonte do TextField
     *
     * @param size
     */
    private void resetFontSize(int size) {
        Font fieldFont = getFont();
        Font font = new Font(fieldFont.getFontName(),
                fieldFont.getStyle(), size);
        setFont(font);
    }

    private class GenJComboBoxItemEditor extends BasicComboBoxEditor {

        private JPanel panel = new JPanel();
        private JLabel labelItem = new JLabel();
        private Object selectedValue;

        public GenJComboBoxItemEditor() {
            panel.setLayout(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.weightx = 1.0;
            constraints.insets = new Insets(6, 6, 6, 6);

            labelItem.setOpaque(false);
            labelItem.setHorizontalAlignment(JLabel.LEFT);
            labelItem.setForeground(Color.BLACK);
            
            panel.add(labelItem, constraints);
            panel.setBackground(Color.WHITE);
        }

        @Override
        public Component getEditorComponent() {
            return this.panel;
        }

        @Override
        public Object getItem() {
            return this.selectedValue;
        }

        @Override
        public void setItem(Object item) {
            if (item == null) {
                return;
            }
            
            selectedValue = item;
            labelItem.setText(selectedValue.toString());
            fireActionEvent();
        }
    }

}
