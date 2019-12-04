/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.components.listener;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

/**
 *
 * @author nicho
 */
public class GenKeyAdapter extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent evt) {
        int key = evt.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            if (evt.getSource() instanceof JTextField) {
                JTextField t = (JTextField) evt.getSource();
                t.transferFocus();
            }
        }
    }

}
