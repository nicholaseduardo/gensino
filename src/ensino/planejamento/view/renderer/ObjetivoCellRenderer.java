/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.renderer;

import ensino.components.GenJLabel;
import ensino.components.renderer.TextAreaCellRenderer;
import ensino.planejamento.model.Objetivo;
import ensino.planejamento.view.models.ObjetivoTableModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.JTable;

/**
 *
 * @author nicho
 */
public class ObjetivoCellRenderer extends TextAreaCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int col) {
        if (isSelected) {
            setColors(new Color(table.getSelectionForeground().getRGB()),
                    new Color(table.getSelectionBackground().getRGB()));
        } else {
            setColors(new Color(table.getForeground().getRGB()),
                    (row % 2 == 0
                            ? new Color(table.getBackground().getRGB())
                            : new Color(240, 240, 240)));
        }
        
        ObjetivoTableModel model = (ObjetivoTableModel) table.getModel();
        Objetivo objetivo = (Objetivo) model.getRow(row);
        /**
         * Marca o objeto que está registrado para exclusão
         */
        if (objetivo.isDeleted()) {
            markAsDeleted();
        }
        
        GenJLabel labelData = createLabel((String) value);

        JPanel panel;
        if (col == 0) {
            panel = createLayoutPanel(labelData, FlowLayout.LEFT);
        } else {
            panel = createLayoutPanel(labelData, FlowLayout.CENTER);
        }
        
        table.setRowHeight(panel.getPreferredSize().height + 5);
        panel.setOpaque(true);
        return panel;
    }

}
