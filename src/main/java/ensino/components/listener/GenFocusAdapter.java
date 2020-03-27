/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.components.listener;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;

/**
 *
 * @author nicho
 */
public class GenFocusAdapter implements FocusListener {

    @Override
    public void focusGained(FocusEvent e) {
        if (e.getSource() instanceof JTextField) {
            JTextField text = (JTextField)e.getSource();
            text.selectAll();
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        
    }
    
}
