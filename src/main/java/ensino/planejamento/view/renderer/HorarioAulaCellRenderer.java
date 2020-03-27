/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.renderer;

import ensino.configuracoes.view.renderer.*;
import ensino.components.GenJLabel;
import ensino.planejamento.model.HorarioAula;
import ensino.planejamento.view.models.HorarioAulaTableModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

/**
 *
 * @author nicho
 */
public class HorarioAulaCellRenderer extends GenCellRenderer {

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

        HorarioAulaTableModel model = (HorarioAulaTableModel) table.getModel();
        HorarioAula horarioAula = (HorarioAula) model.getRow(row);
        if (horarioAula.isDeleted()) {
            markAsDeleted();
        }

        GenJLabel lblTitle = createLabel(horarioAula.getDiaDaSemana().toString());
        
        GenJLabel lblTurno = createLabel(String.format("[%s - %s]",
                "Turno", horarioAula.getTurno().toString()));
        lblTurno.resetFontSize(12);

        GenJLabel lblHorario = createLabel(
                String.format("[Horario: %s]", horarioAula.getHorario()),
                JLabel.RIGHT);
        lblHorario.resetFontSize(12);

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 0));
        panel.add(createLayoutPanel(lblTitle, FlowLayout.LEFT));
        panel.add(createLayoutPanel(lblHorario, FlowLayout.RIGHT));
        panel.add(createLayoutPanel(new GenJLabel(" "), FlowLayout.LEFT));
        panel.add(createLayoutPanel(lblTurno, FlowLayout.RIGHT));
        panel.setBackground(getBack());

        table.setRowHeight(panel.getPreferredSize().height + 10);
        panel.setOpaque(true);
        return panel;
    }

}
