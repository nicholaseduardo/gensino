/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.renderer;

import ensino.components.renderer.GenCellRenderer;
import ensino.components.GenJLabel;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.JTable;

/**
 *
 * @author nicho
 */
public class TableBarCellRenderer extends GenCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int col) {
        if (isSelected) {
            setColors(new Color(table.getSelectionForeground().getRGB()),
                    new Color(table.getSelectionBackground().getRGB()));
        } else {
            setColors(new Color(table.getForeground().getRGB()),
                    new Color(255, 255, 255)
            );
        }

        int textAlign = FlowLayout.LEFT;
        GenJLabel label;
        if (value instanceof Double) {
            textAlign = FlowLayout.RIGHT;
            Double v = (Double) value;
            label = createLabel(String.format("%.2f", v));
            Color cor = null;
            if (v < 4.0) {
                cor = Color.DARK_GRAY;
            } else if (v >= 4.0 && v < 6.0) {
                cor = Color.RED;
            } else {
                cor = Color.BLUE;
            }
            label.setForeground(cor);
        } else if (value instanceof Integer) {
            Integer v = (Integer)value;
            label = createLabel(v.toString());
        } else {
            label = createLabel((String) value);
        }

        label.resetFontSize(12);

        JPanel p = new JPanel(new FlowLayout(textAlign, 5, 5));
        p.add(label);
        p.setBackground(getBack());
        p.setOpaque(true);
        table.setRowHeight(p.getPreferredSize().height);
        return p;
    }

}
