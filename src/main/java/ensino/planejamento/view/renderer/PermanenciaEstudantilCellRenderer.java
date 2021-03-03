/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.renderer;

import ensino.components.renderer.GenCellRenderer;
import ensino.components.GenJLabel;
import ensino.configuracoes.model.Curso;
import ensino.helpers.DateHelper;
import ensino.planejamento.model.PermanenciaEstudantil;
import ensino.planejamento.view.models.PermanenciaEstudantilTableModel;
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
public class PermanenciaEstudantilCellRenderer extends GenCellRenderer {

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

        PermanenciaEstudantilTableModel model = (PermanenciaEstudantilTableModel) table.getModel();
        PermanenciaEstudantil permanenciaEstudantil = (PermanenciaEstudantil) model.getRow(row);
        Curso curso = permanenciaEstudantil.getId().getPlanoDeEnsino().getUnidadeCurricular().getCurso();

        GenJLabel lblTitle = createLabel(permanenciaEstudantil.getDescricao());

        GenJLabel lblCurso = createLabel(String.format("[Curso: %s]",
                curso.getNome()));
        lblCurso.resetFontSize(12);
        lblCurso.setIcon(new ImageIcon(getClass().getResource("/img/courses-icon-15px.png")));

        GenJLabel lblCampus = createLabel(String.format("[Campus: %s]", curso.getId().getCampus().getNome()));
        lblCampus.resetFontSize(12);
        lblCampus.setIcon(new ImageIcon(getClass().getResource("/img/university-icon-15px.png")));
        
        GenJLabel lblNEstudantes = createLabel(String.format("[Estudantes: %d]",
                permanenciaEstudantil.getAtendimentos().size()));
        lblNEstudantes.resetFontSize(12);

        GenJLabel lblAno = createLabel(String.format("[Data atendimento: %s %s]", 
                DateHelper.dateToString(permanenciaEstudantil.getDataAtendimento(), "dd/MM/yyyy"),
                DateHelper.dateToString(permanenciaEstudantil.getDataAtendimento(), "H:m")));
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
