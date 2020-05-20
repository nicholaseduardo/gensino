/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.renderer;

import ensino.components.GenJTextArea;
import ensino.components.renderer.GenItemRenderer;
import ensino.planejamento.model.Objetivo;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 *
 * @author santos
 */
public class ObjetivoItemRenderer extends GenItemRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected,
                cellHasFocus);
        if (isSelected) {
            setColors(new Color(getForeground().getRGB()),
                    new Color(getBackground().getRGB()));
        } else {
            setColors(new Color(getForeground().getRGB()),
                    (index % 2 == 0
                            ? new Color(getBackground().getRGB())
                            : new Color(240, 240, 240)));
        }
        if (value instanceof Objetivo) {
            Objetivo o = (Objetivo) value;
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            GenJTextArea textArea = new GenJTextArea();
            textArea.setText(o.getDescricao());
            textArea.setColumns(50);
            panel.add(textArea);
            return panel;
        }
        return this;
    }

}
