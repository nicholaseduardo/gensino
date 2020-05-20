/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.renderer;

import ensino.components.renderer.GenCellRenderer;
import ensino.components.GenJLabel;
import ensino.configuracoes.view.models.CalendarioTableModel;
import ensino.configuracoes.model.Calendario;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

/**
 *
 * @author nicho
 */
public class CalendarioCellRenderer extends GenCellRenderer {

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

        CalendarioTableModel model = (CalendarioTableModel) table.getModel();
        Calendario cal = (Calendario) model.getRow(row);
        GenJLabel lblTitle = createLabel(cal.getDescricao());
        
        GenJLabel lblCampus = createLabel(String.format("[Campus: %s]", 
                cal.getId().getCampus().getNome()));
        lblCampus.resetFontSize(12);
        lblCampus.setIcon(new ImageIcon(getClass().getResource("/img/university-icon-15px.png")));
        
        GenJLabel lblTasks = createLabel(String.format("Atividades: %d", cal.getAtividades().size()), JLabel.RIGHT);
        lblTasks.resetFontSize(12);
        lblTasks.setIcon(new ImageIcon(getClass().getResource("/img/tasks-icon-15px.png")));
        
        GenJLabel lblPeriodoEnsino = createLabel(String.format("Períodos de Ensino: %d", 
                cal.getPeriodosLetivos().size()), JLabel.RIGHT);
        lblPeriodoEnsino.resetFontSize(12);
        
        JPanel panelIcons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelIcons.add(lblTasks);
        panelIcons.add(lblPeriodoEnsino);
        panelIcons.setBackground(getBack());

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 0));
        panel.add(lblTitle);
        panel.add(new JLabel(""));
        panel.add(lblCampus);
        panel.add(panelIcons);
        panel.setBackground(getBack());

        table.setRowHeight(panel.getPreferredSize().height + 10);
        panel.setOpaque(true);
        return panel;
    }

}
