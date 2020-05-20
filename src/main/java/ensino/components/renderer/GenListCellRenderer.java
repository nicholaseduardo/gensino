/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.components.renderer;

import ensino.components.GenJLabel;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/**
 *
 * @author nicho
 */
public abstract class GenListCellRenderer implements ListCellRenderer {
    
    private Color back;
    private Color fore;

    protected void setColors(Color fore, Color back) {
        this.back = back;
        this.fore = fore;
    }

    protected Color getBack() {
        return back;
    }

    protected Color getFore() {
        return fore;
    }

    protected GenJLabel createLabel(String text) {
        return createLabel(text, JLabel.LEFT);
    }

    protected GenJLabel createLabel(String text, int position) {
        GenJLabel label = new GenJLabel(text, position);
        label.setForeground(this.fore);
        label.setBackground(this.back);
        return label;
    }

    protected JPanel createLayoutPanel(JComponent component, int orientation) {
        JPanel panel = new JPanel(new FlowLayout(orientation, 10, 0));
        panel.add(component);
        panel.setBackground(getBack());
        return panel;
    }

    protected void markAsDeleted() {
        setColors(new Color(0, 0, 0), new Color(150, 150, 150));
    }

}
