/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.renderer;

import ensino.components.renderer.GenCellRenderer;
import ensino.components.GenJLabel;
import static ensino.components.GenJPanel.IMG_SOURCE;
import ensino.configuracoes.model.Turma;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.net.URL;
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
                    row % 2 == 0
                            ? new Color(table.getBackground().getRGB())
                            : new Color(240, 240, 240));
        }

        if (value instanceof Turma) {
            Turma turma = (Turma) value;
            URL urlTurma = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "classroom-25px.png"));
            ImageIcon iconTurma = new ImageIcon(urlTurma);

            GenJLabel lblTitulo = createLabel("Turma: " + turma.getNome(), iconTurma, JLabel.LEFT);
            lblTitulo.setHorizontalTextPosition(JLabel.RIGHT);
            lblTitulo.resetFontSize(12);

            GenJLabel lblNEstudantes = createLabel(String.format("[Estudantes: %d]",
                    turma.getEstudantes().size()), JLabel.RIGHT);
            lblNEstudantes.resetFontSize(12);

            GenJLabel lblAno = createLabel(String.format("[Ano: %d]",
                    turma.getAno()), JLabel.RIGHT);
            lblAno.resetFontSize(12);

            JPanel panelTitulo = createPanel(new GridLayout(0, 2));
            panelTitulo.add(createPanel(new FlowLayout(FlowLayout.LEFT, 5, 5)).add(lblTitulo));
            panelTitulo.add(createPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5)).add(lblNEstudantes));
            panelTitulo.add(createPanel(new FlowLayout(FlowLayout.LEFT, 5, 5)).add(createLabel("")));
            panelTitulo.add(createPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5)).add(lblAno));

            JPanel panel = createPanel();
            panel.setLayout(new BorderLayout());
            panel.add(panelTitulo, BorderLayout.CENTER);

            table.setRowHeight(panel.getPreferredSize().height);
            panel.setOpaque(true);
            return panel;
        }
        return null;
    }

}
