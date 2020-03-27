/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.renderer;

import ensino.configuracoes.view.renderer.*;
import ensino.components.GenJLabel;
import ensino.defaults.DefaultTableModel;
import ensino.planejamento.model.Metodologia;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.JTable;

/**
 *
 * @author nicho
 */
public class MetodologiaCellRenderer extends GenCellRenderer {
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int col) {
        if (isSelected) {
            setColors(new Color(table.getSelectionForeground().getRGB()),
                    new Color(table.getSelectionBackground().getRGB()));
        } else {
            setColors(new Color(table.getForeground().getRGB()),
                    (row % 2 == 0 ? 
                        new Color(table.getBackground().getRGB()) : 
                        new Color(240,240,240)));
        }
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        Metodologia metodologia = (Metodologia) model.getRow(row);
        if (metodologia.isDeleted()) {
            markAsDeleted();
        }
        
        GenJLabel label = createLabel(metodologia.toString());
        
        JPanel panel = new JPanel(new BorderLayout(10, 100));
        panel.add(createLayoutPanel(label, FlowLayout.LEFT));
        panel.setBackground(getBack());
        
        table.setRowHeight(panel.getPreferredSize().height + 10);
        panel.setOpaque(true);
        return panel;
    }
}
