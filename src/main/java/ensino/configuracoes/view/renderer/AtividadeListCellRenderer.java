/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/**
 *
 * @author nicho
 */
public class AtividadeListCellRenderer implements ListCellRenderer {

    protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Color backgroundColor;
        Color foregroundColor;
        if (isSelected) {
            foregroundColor = new Color(list.getSelectionForeground().getRGB());
            backgroundColor = new Color(list.getSelectionBackground().getRGB());
        } else {
            foregroundColor = new Color(list.getForeground().getRGB());
            backgroundColor = (index % 2 == 0 ? new Color(list.getBackground().getRGB()) : new Color(240, 240, 240));
        }

        JLabel lblTexto = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,
                isSelected, cellHasFocus);
        lblTexto.setForeground(foregroundColor);
        lblTexto.setBackground(backgroundColor);
        
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(lblTexto);
        panel.setBackground(backgroundColor);
        panel.setForeground(foregroundColor);
        panel.setOpaque(true);

        return panel;
    }

}
