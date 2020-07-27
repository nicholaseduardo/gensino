/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.components;

import java.awt.Font;
import java.awt.Insets;
import javax.swing.JTextArea;
import javax.swing.UIManager;

/**
 *
 * @author nicho
 */
public class GenJTextArea extends JTextArea {
    
    public GenJTextArea() {
        super();
        initComponents();
    }
    
    public GenJTextArea(int rows, int columns) {
        super(rows, columns);
        initComponents();
    }
    
    private void initComponents() {
        setLineWrap(true);
        setWrapStyleWord(true);
        resetFontSize(16);
        super.setMargin(new Insets(6,6,6,6));
    }
    
    /**
     * Atualiza o tamanho da fonte do TextField
     * @param size 
     */
    private void resetFontSize(int size) {
        Font fieldFont = getFont();
        Font font = new Font(fieldFont.getFontName(), 
                        fieldFont.getStyle(), size);
        setFont(font);
    }
    
    public void actAsLabel() {
        setWrapStyleWord(true);
        setLineWrap(true);
        setOpaque(false);
        setEditable(false);
        setFocusable(false);
        setBackground(UIManager.getColor("Label.background"));
        setFont(UIManager.getFont("Label.font"));
        setBorder(UIManager.getBorder("Label.border"));
    }
    
}
