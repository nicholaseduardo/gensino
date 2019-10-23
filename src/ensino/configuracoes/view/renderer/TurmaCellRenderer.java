/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.renderer;

import ensino.components.GenJLabel;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.view.models.TurmaTableModel;
import ensino.configuracoes.model.Turma;
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
public class TurmaCellRenderer extends GenCellRenderer {

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

        TurmaTableModel model = (TurmaTableModel) table.getModel();
        Turma turma = (Turma) model.getRow(row);
        Curso curso = turma.getCurso();

        GenJLabel lblTitle = createLabel(turma.getNome());

        GenJLabel lblCurso = createLabel(String.format("[Curso: %s]",
                turma.getCurso().getNome()));
        lblCurso.resetFontSize(12);
        lblCurso.setIcon(new ImageIcon(getClass().getResource("/img/courses-icon-15px.png")));

        GenJLabel lblCampus = createLabel(String.format("[Campus: %s]", curso.getCampus().getNome()));
        lblCampus.resetFontSize(12);
        lblCampus.setIcon(new ImageIcon(getClass().getResource("/img/university-icon-15px.png")));

        GenJLabel lblNEstudantes = createLabel(String.format("[Estudantes: %d]",
                        turma.getEstudantes().size()));
        lblNEstudantes.resetFontSize(12);
        
        GenJLabel lblAno = createLabel(String.format("[Ano: %d]", turma.getAno()));
        lblAno.resetFontSize(12);

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 0));
        panel.add(createLayoutPanel(lblTitle, FlowLayout.LEFT));
        panel.add(createLayoutPanel(createLabel(" "), FlowLayout.RIGHT));
        panel.add(createLayoutPanel(lblCurso, FlowLayout.LEFT));
        panel.add(createLayoutPanel(lblNEstudantes, FlowLayout.RIGHT));
        panel.add(createLayoutPanel(lblCampus, FlowLayout.LEFT));
        panel.add(createLayoutPanel(lblAno, FlowLayout.RIGHT));
        panel.setBackground(getBack());

        table.setRowHeight(panel.getPreferredSize().height + 5);
        panel.setOpaque(true);
        return panel;
    }

}
