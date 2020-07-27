/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.renderer;

import ensino.components.GenJTextArea;
import ensino.components.renderer.GenItemRenderer;
import ensino.configuracoes.model.Conteudo;
import ensino.planejamento.model.Objetivo;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JList;

/**
 *
 * @author santos
 */
public class TextAreaItemRenderer extends GenItemRenderer {

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
        String text = "";
        if (value instanceof Objetivo) {
            Objetivo o = (Objetivo) value;
            text = o.getDescricao();
        } else if (value instanceof Objetivo) {
            Conteudo o = (Conteudo) value;
            text = o.getDescricao();
        }

        GenJTextArea textArea = new GenJTextArea(2, 70);
        textArea.setText(text);
        textArea.actAsLabel();
        textArea.setBackground(getBack());
        textArea.setForeground(getFore());
        textArea.setOpaque(true);

        return textArea;
    }

}
