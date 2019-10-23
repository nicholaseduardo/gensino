/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.components;

import ensino.components.listener.GenFocusAdapter;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 *
 * @author nicho
 */
public class GenJTextField extends JTextField {
    
    public GenJTextField() {
        super();
    }
    
    public GenJTextField(int columns) {
        super(columns);
        initComponents();
    }
    
    private void initComponents() {
        resetFontSize(16);
        super.setMargin(new Insets(6,6,6,6));
        super.addFocusListener(new GenFocusAdapter());
    }
    
    /**
     * Cria um objeto da classe <code>Border</code>
     * @param label     Texto que será colocado como título do campo 
     */
    protected void setLabelFor(String label) {
        Border lineBorder = BorderFactory.createLineBorder(Color.BLACK);
        setBorder(BorderFactory.createTitledBorder(
                lineBorder, label, 
                TitledBorder.LEFT, 
                TitledBorder.TOP));
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
