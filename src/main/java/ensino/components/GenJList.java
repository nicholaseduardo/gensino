/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.components;

import java.awt.Font;
import javax.swing.JList;
import javax.swing.ListModel;

/**
 *
 * @author nicho
 */
public class GenJList extends JList {
    
    public GenJList() {
        super();
        initComponents();
    }
    
    public GenJList(Object[] listData) {
        super(listData);
        initComponents();
    }
    
    public GenJList(ListModel model) {
        super(model);
        initComponents();
    }
    
    public void setSelectedValue(Object anObject) {
        ListModel m = getModel();
        for (int i = 0; i < m.getSize(); i++) {
            if (anObject.equals(m.getElementAt(i))) {
                super.setSelectedIndex(i);
                super.ensureIndexIsVisible(i);
                break;
            }
        }
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
