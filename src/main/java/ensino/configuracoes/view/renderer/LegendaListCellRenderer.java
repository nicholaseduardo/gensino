/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.renderer;

import ensino.components.GenJLabel;
import ensino.components.renderer.GenListCellRenderer;
import ensino.configuracoes.model.Legenda;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 *
 * @author santos
 */
public class LegendaListCellRenderer extends GenListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (isSelected) {
            setColors(new Color(list.getSelectionForeground().getRGB()),
                    new Color(list.getSelectionBackground().getRGB()));
        } else {
            setColors(new Color(list.getForeground().getRGB()),
                    new Color(255, 255, 255)
            );
        }
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        if (value instanceof Legenda) {
            Legenda leg = (Legenda) value;
            GenJLabel renderer = new GenJLabel(leg.getNome());
            renderer.toBold();
            renderer.resetFontSize(16);

            if (isSelected) {
                renderer.setIcon(new ImageIcon(getClass().getResource("/img/check-white-15.png")));
            } else {
                renderer.setIcon(null);
            }
            renderer.setForeground(leg.getCor());
            panel.setBackground(getBack());

            panel.add(renderer);
        }

        panel.setOpaque(true);

        return panel;
    }

}
