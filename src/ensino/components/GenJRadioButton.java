/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.components;

import java.awt.Font;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

/**
 *
 * @author nicho
 */
public class GenJRadioButton extends JRadioButton {
    private Object objectValue;
    
    public GenJRadioButton() {
        super();
        initComponents();
    }
    
    public GenJRadioButton(String text) {
        super(text);
        initComponents();
    }
    
    public GenJRadioButton(String text, boolean active, ButtonGroup bg) {
        super(text, active);
        bg.add(this);
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

    /**
     * Recupera o objeto vinculado ao Radio
     * @return 
     */
    public Object getObjectValue() {
        return objectValue;
    }

    /**
     * Atribui uma instância de classe ao Radio
     * @param object 
     */
    public void setObjectValue(Object object) {
        this.objectValue = object;
    }
}
