/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.renderer;

import ensino.components.GenJLabel;
import ensino.configuracoes.view.models.SemanaLetivaTableModel;
import ensino.configuracoes.model.SemanaLetiva;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTable;

/**
 *
 * @author nicho
 */
public class SemanaLetivaCellRenderer extends GenCellRenderer {

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

        SemanaLetivaTableModel model = (SemanaLetivaTableModel) table.getModel();
        SemanaLetiva semana = (SemanaLetiva) model.getRow(row);
        if (semana.isDeleted()) {
            markAsDeleted();
        }
        GenJLabel lblTitle = createLabel(semana.getDescricao());
        
        GenJLabel lblSemana = createLabel(String.format("[Semana: %s]", 
                semana.getPeriodo().toString()));
        lblSemana.resetFontSize(12);
        lblSemana.setIcon(new ImageIcon(getClass().getResource("/img/calendar-image-png-15px.png")));
        
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 0));
        
        panel.add(lblTitle);
        panel.add(lblSemana);
        panel.setBackground(getBack());

        table.setRowHeight(panel.getPreferredSize().height + 10);
        panel.setOpaque(true);
        
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        p.add(panel);
        p.setBackground(getBack());
        p.setOpaque(true);
        return p;
    }

}
