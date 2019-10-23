/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.renderer;

import ensino.components.GenJLabel;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.view.models.UnidadeCurricularTableModel;
import ensino.configuracoes.model.UnidadeCurricular;
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
public class UnidadeCurricularCellRenderer extends GenCellRenderer {

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

        UnidadeCurricularTableModel model = (UnidadeCurricularTableModel) table.getModel();
        UnidadeCurricular und = (UnidadeCurricular) model.getRow(row);
        Curso curso = und.getCurso();

        GenJLabel lblTitle = createLabel(und.getNome());

        GenJLabel lblCurso = createLabel(String.format("[Curso: %s]",
                und.getCurso().getNome()));
        lblCurso.resetFontSize(12);
        lblCurso.setIcon(new ImageIcon(getClass().getResource("/img/courses-icon-15px.png")));

        GenJLabel lblCampus = createLabel(String.format("[Campus: %s]", curso.getCampus().getNome()));
        lblCampus.resetFontSize(12);
        lblCampus.setIcon(new ImageIcon(getClass().getResource("/img/university-icon-15px.png")));

        GenJLabel lblReferencias = createLabel(String.format("Referências bibliográficas: %d", und.getReferenciasBibliograficas().size()), JLabel.RIGHT);
        lblReferencias.resetFontSize(12);
        lblReferencias.setIcon(new ImageIcon(getClass().getResource("/img/library-icon-15px.png")));

        GenJLabel lblNAulas = createLabel(
                String.format("[Aulas Teórico/Práticas: %d/%d]",
                        und.getnAulasTeoricas(), und.getnAulasPraticas()));
        lblNAulas.resetFontSize(12);

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 0));
        panel.add(createLayoutPanel(lblTitle, FlowLayout.LEFT));
        panel.add(createLayoutPanel(createLabel(" "), FlowLayout.RIGHT));
        panel.add(createLayoutPanel(lblCurso, FlowLayout.LEFT));
        panel.add(createLayoutPanel(lblNAulas, FlowLayout.RIGHT));
        panel.add(createLayoutPanel(lblCampus, FlowLayout.LEFT));
        panel.add(createLayoutPanel(lblReferencias, FlowLayout.RIGHT));
        panel.setBackground(getBack());

        table.setRowHeight(panel.getPreferredSize().height + 5);
        panel.setOpaque(true);
        return panel;
    }

}
