/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.renderer;

import ensino.components.renderer.GenCellRenderer;
import ensino.components.GenJLabel;
import ensino.planejamento.model.PlanoAvaliacao;
import ensino.planejamento.view.models.PlanoAvaliacaoTableModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Calendar;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

/**
 *
 * @author nicho
 */
public class PlanoAvaliacaoCellRenderer extends GenCellRenderer {

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

        PlanoAvaliacaoTableModel model = (PlanoAvaliacaoTableModel) table.getModel();
        PlanoAvaliacao planoAvaliacao = (PlanoAvaliacao) model.getRow(row);

        /**
         * muda a cor da linha caso o objeto esteja marcado para exclusão
         */
        if (planoAvaliacao.isDeleted()) {
            markAsDeleted();
        }

        GenJLabel lblTitle = createLabel(planoAvaliacao.getNome());
        
        GenJLabel lblBimestre = createLabel(String.format("[%s - %s]",
                planoAvaliacao.getInstrumentoAvaliacao().toString(),
                planoAvaliacao.getEtapaEnsino() != null ?
                        planoAvaliacao.getEtapaEnsino().getNome() : ""));
        lblBimestre.resetFontSize(12);

        GenJLabel lblValores = createLabel(String.format("[Valor: %.2f - Peso: %.2f] Nota: %.2f",
                planoAvaliacao.getValor(), planoAvaliacao.getPeso(),
                planoAvaliacao.getNota()), JLabel.RIGHT);
        lblValores.resetFontSize(12);

        Calendar cal = Calendar.getInstance();
        cal.setTime(planoAvaliacao.getData());
        GenJLabel lblPrevisao = new GenJLabel(String.format("[Data prevista: %d/%d/%d]",
                cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)));
        lblPrevisao.resetFontSize(12);
        lblPrevisao.setForeground(getFore());

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 0));
        panel.add(createLayoutPanel(lblTitle, FlowLayout.LEFT));
        panel.add(createLayoutPanel(lblPrevisao, FlowLayout.RIGHT));
        panel.add(createLayoutPanel(lblBimestre, FlowLayout.LEFT));
        panel.add(createLayoutPanel(lblValores, FlowLayout.RIGHT));
        panel.setBackground(getBack());
        
        table.setRowHeight(panel.getPreferredSize().height + 10);
        panel.setOpaque(true);
        return panel;
    }

}
