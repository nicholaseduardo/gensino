/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.components;

import java.awt.Font;
import javax.swing.Icon;
import javax.swing.JLabel;

/**
 *
 * @author nicho
 */
public class GenJLabel extends JLabel {
    
    public GenJLabel() {
        
    }
    
    public GenJLabel(String text) {
        super(text);
        initComponents();
    }
    
    public GenJLabel(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);
        initComponents();
    }
    
    public GenJLabel(Icon image) {
        super(image);
        initComponents();
    }
    
    public GenJLabel(Icon image, int horizontalAlignment) {
        super(image, horizontalAlignment);
        initComponents();
    }
    
    private void initComponents() {
        resetFontSize(16);
    }
    
    private void formatFont(String fontname, int style, int size) {
        Font font = new Font(fontname, style, size);
        setFont(font);
    }
    
    /**
     * Atualiza o tamanho da fonte do TextField
     * @param size 
     */
    public void resetFontSize(int size) {
        Font fieldFont = getFont();
        formatFont(fieldFont.getFontName(), 
                        fieldFont.getStyle(), size);
    }
    
    public void toItalic() {
        Font fieldFont = getFont();
        formatFont(fieldFont.getFontName(), 
                        Font.ITALIC, 
                        fieldFont.getSize());
    }
    
    public void toBold() {
        Font fieldFont = getFont();
        formatFont(fieldFont.getFontName(), 
                        Font.BOLD, 
                        fieldFont.getSize());
    }
}
