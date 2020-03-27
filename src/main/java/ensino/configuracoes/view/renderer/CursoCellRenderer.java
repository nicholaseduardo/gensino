/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.renderer;

import ensino.components.GenJLabel;
import ensino.configuracoes.controller.TurmaController;
import ensino.configuracoes.controller.UnidadeCurricularController;
import ensino.configuracoes.view.models.CursoTableModel;
import ensino.configuracoes.model.Curso;
import ensino.patterns.factory.ControllerFactory;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

/**
 *
 * @author nicho
 */
public class CursoCellRenderer extends GenCellRenderer {

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

        CursoTableModel model = (CursoTableModel) table.getModel();
        Curso o = (Curso) model.getRow(row);
        GenJLabel lblTitle = createLabel(o.getNome());

        GenJLabel lblCampus = createLabel(String.format("[Campus: %s]", o.getId().getCampus().getNome()));
        lblCampus.resetFontSize(12);
        lblCampus.setIcon(new ImageIcon(getClass().getResource("/img/university-icon-15px.png")));
        
        GenJLabel lblTurma = createLabel(String.format("Turmas: %d", o.getTurmas().size()), JLabel.RIGHT);
        lblTurma.resetFontSize(12);
        lblTurma.setIcon(new ImageIcon(getClass().getResource("/img/classroom-15px.png")));

        GenJLabel lblUnd = createLabel(String.format("Disciplinas: %d", o.getUnidadesCurriculares().size()), JLabel.RIGHT);
        lblUnd.resetFontSize(12);
        lblUnd.setIcon(new ImageIcon(getClass().getResource("/img/school-icon-15px.png")));

        JPanel panelIcons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelIcons.add(lblTurma);
        panelIcons.add(lblUnd);
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
