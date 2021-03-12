/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.renderer;

import ensino.components.renderer.GenCellRenderer;
import ensino.components.GenJLabel;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.Metodologia;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.net.URL;
import java.util.StringJoiner;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTable;

/**
 *
 * @author nicho
 */
public class DetalhamentoCellRenderer extends GenCellRenderer {

    private JPanel createPanel(JTable table, Detalhamento o, int row) {
        String pathFormat = "/img/%s.png";
        URL urlSemana = getClass().getResource(String.format(pathFormat, "calendar-image-png-25px"));
        URL urlRecurso = getClass().getResource(String.format(pathFormat, "classroom-25px"));
        URL urlTecnica = getClass().getResource(String.format(pathFormat, "gear-icon-25px"));
        URL urlInstrumento = getClass().getResource(String.format(pathFormat, "duplicate-button-25px"));

        GenJLabel lblSemana = createLabel(String.format("%s - %s",
                o.getSemanaLetiva().toString(),
                o.getSemanaLetiva().getPeriodo().toString()));
        lblSemana.setIcon(new ImageIcon(urlSemana));
        lblSemana.toBold();
        
        GenJLabel lblAulas = createLabel(String.format("[Aulas Práticas: %d - Aulas Teóricas: %d]",
                o.getNAulasPraticas(), o.getNAulasTeoricas()));
        lblAulas.resetFontSize(12);

        JPanel panelSemana = new JPanel(new GridLayout(1, 2, 10, 10));
        panelSemana.setBackground(getBack());
        panelSemana.add(lblSemana);
        panelSemana.add(lblAulas);

        GenJLabel textArea = createLabel("");
        textArea.setText("<b>Conteúdo: </b>" + o.getConteudo(), 80);
        textArea.resetFontSize(12);

        StringJoiner sbTecnica = new StringJoiner(", "),
                sbRecurso = new StringJoiner(", "),
                sbInstrumento = new StringJoiner(", ");
        for (Metodologia metodo : o.getMetodologias()) {
            switch (metodo.getTipo()) {
                case TECNICA:
                    sbTecnica.add(metodo.getMetodo().getNome());
                    break;
                case RECURSO:
                    sbRecurso.add(metodo.getMetodo().getNome());
                    break;
                case INSTRUMENTO:
                    sbInstrumento.add(metodo.getMetodo().getNome());
                    break;
            }
        }

        GenJLabel lblTecnica = createLabel(sbTecnica.toString());
        lblTecnica.resetFontSize(12);
        lblTecnica.setIcon(new ImageIcon(urlTecnica));

        GenJLabel lblRecurso = createLabel(sbRecurso.toString());
        lblRecurso.setIcon(new ImageIcon(urlRecurso));
        lblRecurso.resetFontSize(12);

        GenJLabel lblInstrumento = createLabel(sbInstrumento.toString());
        lblInstrumento.setIcon(new ImageIcon(urlInstrumento));
        lblInstrumento.resetFontSize(12);

        JPanel panelMetodo = new JPanel(new GridLayout(3, 1));
        panelMetodo.add(createLayoutPanel(lblTecnica, FlowLayout.RIGHT));
        panelMetodo.add(createLayoutPanel(lblRecurso, FlowLayout.RIGHT));
        panelMetodo.add(createLayoutPanel(lblInstrumento, FlowLayout.RIGHT));
        panelMetodo.setBackground(getBack());

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(true);
        panel.setBackground(getBack());
        panel.add(panelSemana, BorderLayout.PAGE_START);
        panel.add(textArea, BorderLayout.CENTER);
        panel.add(panelMetodo, BorderLayout.PAGE_END);

        table.setRowHeight(row, panel.getPreferredSize().height);

        return panel;
    }

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

        if (value instanceof Detalhamento) {
            return createPanel(table, (Detalhamento) value, row);
        }
        if (value != null) {
            return createLabel(value.toString());
        }
        return null;
    }

}
