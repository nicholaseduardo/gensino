/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.renderer;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author santos
 */
public class ColorCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        //Cells are by default rendered as a JLabel.
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

        if (c instanceof JLabel && value instanceof Color) {
            JLabel label = (JLabel) c;
            label.setOpaque(true);
            label.setBackground((Color)value);
            label.setText(" ");
        }

        //Return the JLabel which renders the cell.
        return c;
    }

}
