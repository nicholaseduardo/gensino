/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.renderer;

import ensino.components.renderer.GenCellRenderer;
import ensino.components.GenJLabel;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.view.models.CampusTableModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

/**
 *
 * @author nicho
 */
public class CampusCellRenderer extends GenCellRenderer {

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

        CampusTableModel model = (CampusTableModel) table.getModel();
        Campus campus = (Campus) model.getRow(row);
        GenJLabel lblTitle = createLabel(campus.getNome());
        if (campus.isVigente()) {
            lblTitle.toBold();
            lblTitle.setIcon(new ImageIcon(getClass().getResource("/img/check-tick-icon-15px.png")));
        }
        lblTitle.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));

        GenJLabel lblCurso = createLabel(String.format("Cursos: %d", campus.getCursos().size()), JLabel.RIGHT);
        lblCurso.resetFontSize(12);
        lblCurso.setIcon(new ImageIcon(getClass().getResource("/img/courses-icon-15px.png")));

        GenJLabel lblCal = createLabel(String.format("Calendários: %d", campus.getCalendarios().size()), JLabel.RIGHT);
        lblCal.resetFontSize(12);
        lblCal.setIcon(new ImageIcon(getClass().getResource("/img/calendar-image-png-15px.png")));

        JPanel panelIcons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelIcons.add(lblCurso);
        panelIcons.add(lblCal);
        panelIcons.setBackground(getBack());

        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 0));
        panel.add(lblTitle);
        panel.add(panelIcons);
        panel.setBackground(getBack());

        table.setRowHeight(panel.getPreferredSize().height + 10);
        panel.setOpaque(true);
        return panel;
    }
}
