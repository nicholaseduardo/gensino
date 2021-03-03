/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.renderer;

import ensino.components.renderer.GenCellRenderer;
import ensino.components.GenJLabel;
import ensino.defaults.DefaultTableModel;
import ensino.patterns.BaseObject;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.JTable;

/**
 *
 * @author nicho
 */
public class BaseObjectCellRenderer extends GenCellRenderer {
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int col) {
        if (isSelected) {
            setColors(new Color(table.getSelectionForeground().getRGB()),
                    new Color(table.getSelectionBackground().getRGB()));
        } else {
            setColors(new Color(table.getForeground().getRGB()),
                    row % 2 == 0 ? 
                        new Color(table.getBackground().getRGB()) : 
                        new Color(240,240,240));
        }
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        BaseObject base = (BaseObject) model.getRow(row);
        
        GenJLabel label = createLabel(String.format("[%d] %s", base.getId(),
                base.getNome()));
        label.setForeground(getFore());
        
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel.add(label);
        panel.setBackground(getBack());
        table.setRowHeight(panel.getPreferredSize().height + 5);
        
        panel.setOpaque(true);
        return panel;
    }
}
