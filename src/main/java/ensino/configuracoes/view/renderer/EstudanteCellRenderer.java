/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.renderer;

import ensino.components.renderer.GenCellRenderer;
import ensino.components.GenJLabel;
import ensino.configuracoes.view.models.EstudanteTableModel;
import ensino.configuracoes.model.Estudante;
import ensino.helpers.DateHelper;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JTable;

/**
 *
 * @author nicho
 */
public class EstudanteCellRenderer extends GenCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int col) {
        if (isSelected) {
            setColors(new Color(table.getSelectionForeground().getRGB()),
                    new Color(table.getSelectionBackground().getRGB()));
        } else {
            setColors(new Color(table.getForeground().getRGB()),
                    row % 2 == 0
                            ? new Color(table.getBackground().getRGB())
                            : new Color(240, 240, 240));
        }

        EstudanteTableModel model = (EstudanteTableModel) table.getModel();
        Estudante o = (Estudante) model.getRow(row);

        GenJLabel lblTitle = createLabel(String.format("[%d] %s", o.getId().getId(),
                o.getNome()));

        GenJLabel lblIngresso = createLabel(String.format("[Data de Ingresso: %s]",
                DateHelper.dateToString(o.getIngresso(), "dd/MM/yyyy")));
        lblIngresso.resetFontSize(12);

        GenJLabel lblRA = createLabel(String.format("[R.A.: %s]", o.getRegistro()));
        lblRA.resetFontSize(12);
        
        GenJLabel lblSituacao = createLabel(String.format("[Situação: %s]",
                o.getSituacaoEstudante() != null ? o.getSituacaoEstudante().toString() : "-"));
        lblSituacao.resetFontSize(12);

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 0));
        panel.add(createLayoutPanel(lblTitle, FlowLayout.LEFT));
        panel.add(createLayoutPanel(lblSituacao, FlowLayout.RIGHT));
        panel.add(createLayoutPanel(lblIngresso, FlowLayout.LEFT));
        panel.add(createLayoutPanel(lblRA, FlowLayout.RIGHT));
        panel.setBackground(getBack());

        table.setRowHeight(panel.getPreferredSize().height + 5);
        panel.setOpaque(true);
        return panel;
    }

}
