/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.components;

import ensino.components.listener.GenFocusAdapter;
import java.awt.Font;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;

/**
 *
 * @author nicho
 */
public class GenJSpinner extends JSpinner {
    
    public GenJSpinner() {
        super();
        initComponents();
    }
    
    public GenJSpinner(SpinnerModel model) {
        super(model);
        initComponents();
    }
    
    public GenJSpinner(JComponent editor) {
        this();
        super.setEditor(editor);
        initComponents();
    }
    
    private void initComponents() {
        setBounds(30, 30, 100, 30);
        
        addFocusListener(new GenFocusAdapter());
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
