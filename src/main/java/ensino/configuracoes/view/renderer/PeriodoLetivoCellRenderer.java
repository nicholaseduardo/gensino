/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.renderer;

import ensino.components.renderer.GenCellRenderer;
import ensino.components.GenJLabel;
import ensino.configuracoes.view.models.PeriodoLetivoTableModel;
import ensino.configuracoes.model.PeriodoLetivo;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTable;

/**
 *
 * @author nicho
 */
public class PeriodoLetivoCellRenderer extends GenCellRenderer {

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

        PeriodoLetivoTableModel model = (PeriodoLetivoTableModel) table.getModel();
        PeriodoLetivo pl = (PeriodoLetivo) model.getRow(row);
        
        GenJLabel lblTitle = createLabel(String.format("[%d] %s", 
                pl.getId().getNumero(),
                pl.getDescricao()));
        
        GenJLabel lblPeriodo = createLabel(String.format("[Período: %s]", 
                pl.getPeriodo().toString()));
        lblPeriodo.resetFontSize(12);
        lblPeriodo.setIcon(new ImageIcon(getClass().getResource("/img/calendar-image-png-15px.png")));
        
        GenJLabel lblSemanas = createLabel(String.format("%d Semanas Letivas", 
                pl.getSemanasLetivas().size()));
        lblSemanas.resetFontSize(12);
        
        JPanel pLabels = createPanel(new GridLayout(1,2,10,10));
        pLabels.add(lblPeriodo);
        pLabels.add(lblSemanas);
        
        JPanel panel = createPanel(new GridLayout(2, 1));
        
        panel.add(lblTitle);
        panel.add(pLabels);

        table.setRowHeight(panel.getPreferredSize().height + 10);
        panel.setOpaque(true);
        return panel;
    }

}
