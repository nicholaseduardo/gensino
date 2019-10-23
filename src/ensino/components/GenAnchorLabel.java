/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.util.Map;

/**
 *
 * @author nicho
 */
public class GenAnchorLabel extends GenJLabel
        implements MouseListener {

    private Color hoverColor;
    private Color defaultColor;

    public GenAnchorLabel() {
        super();
        setLinkColors(Color.blue, getForeground());
        initComponent();
    }

    public GenAnchorLabel(String text) {
        super(text);
        setLinkColors(Color.blue, getForeground());
        initComponent();
    }

    public GenAnchorLabel(String text, Color hoverColor, Color defaultColor) {
        super(text);
        setLinkColors(hoverColor, defaultColor);
        initComponent();
    }

    public void setLinkColors(Color hoverColor, Color defaultColor) {
        this.hoverColor = hoverColor;
        this.defaultColor = defaultColor;
    }

    private void initComponent() {
        this.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setForeground(this.hoverColor);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        setForeground(this.defaultColor);
    }

//    public static void main(String args[]) {
//        javax.swing.JFrame f = new javax.swing.JFrame("Teste");
//        f.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
//        f.setSize(new java.awt.Dimension(300,300));
//        javax.swing.JPanel panel = (javax.swing.JPanel)f.getContentPane();
//        panel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
//        panel.add(new GenAnchorLabel("Teste de label"));
//        
//        f.setVisible(true);
//    }
}
