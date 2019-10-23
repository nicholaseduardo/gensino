/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.components;

import java.awt.CheckboxGroup;
import java.awt.Font;
import javax.swing.JCheckBox;

/**
 *
 * @author nicho
 */
public class GenJCheckBox extends JCheckBox {
    
    public GenJCheckBox() {
        super();
        initComponents();
    }
    
    public GenJCheckBox(String text) {
        super(text);
        initComponents();
    }
    
    private void initComponents() {
        resetFontSize(16);
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
    
}
