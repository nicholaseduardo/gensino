/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.renderer;

import ensino.components.renderer.GenCellRenderer;
import ensino.components.GenJLabel;
import ensino.configuracoes.model.Conteudo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTable;

/**
 *
 * @author nicho
 */
public class ConteudoCellRenderer extends GenCellRenderer {

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

        if (value instanceof Conteudo) {
            Conteudo c = (Conteudo) value;
            GenJLabel lblTitle = createLabel("");
            
            int n = c.getNivel() != null && c.getNivel() > 1 ? c.getNivel() - 1 : 0;
            String t = "--".repeat(n).concat(c.getDescricao());
            
            lblTitle.setText(t, 60+(n*2));
            lblTitle.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
            lblTitle.toBold();

            String conteudoParent = "";
            if (c.hasParent()) {
                conteudoParent = c.getConteudoParent().getDescricao();
            }

            GenJLabel lblParent = createLabel("");
            lblParent.setText(conteudoParent, 60);
            lblParent.resetFontSize(12);
            
            GenJLabel lblNivel = createLabel(
                    String.format("Nivel: %d / Sequência: %d",
                            c.getNivel(), c.getSequencia()));
            lblNivel.resetFontSize(12);

            JPanel panel = createPanel(new BorderLayout());
            panel.add(lblTitle, BorderLayout.CENTER);
            panel.add(lblNivel, BorderLayout.LINE_END);
            panel.add(lblParent, BorderLayout.PAGE_END);

            int height = panel.getPreferredSize().height;
            if (height < 55) {
                height = 55;
            }

            table.setRowHeight(height);
            panel.setOpaque(true);
            return panel;
        }
        return null;
    }
}
