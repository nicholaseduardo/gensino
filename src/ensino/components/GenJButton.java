/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.components;

import java.awt.Font;
import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author nicho
 */
public class GenJButton extends JButton {
    public GenJButton() {
        super();
        initComponents();
    }
    
    public GenJButton(String text) {
        super(text);
        initComponents();
    }
    
    public GenJButton(ImageIcon image) {
        super(image);
        initComponents();
    }
    
    public GenJButton(String text, ImageIcon image) {
        super(text, image);
        initComponents();
    }
    
    private void initComponents() {
        setMargin(new Insets(5, 5, 5, 5));
//        setBackground(Color.white);
//        Border border = BorderFactory.createLineBorder(Color.black, 1, true);
//        setBorder(border);
        resetFontSize(16);
    }
    
    public void resetFontSize(int size) {
        Font font = getFont();
        setFont(font.deriveFont(size));
    }
}
