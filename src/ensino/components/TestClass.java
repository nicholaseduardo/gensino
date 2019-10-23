/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.components;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 *
 * @author nicho
 */
public class TestClass {
    public static void main(String args[]) {
        JFrame f = new JFrame("Teste");
        JPanel p = (JPanel) f.getContentPane();
        
        JPanel p1 = new JPanel(new FlowLayout());
        Border border = BorderFactory.createRaisedBevelBorder();
        p1.setBorder(border);
        p.add(p1);
        
        p1.add(new GenJLabel("Teste"), BorderLayout.NORTH);
        GenJTextField t = new GenJTextField(10);
        
        p1.add(t, java.awt.BorderLayout.CENTER);
        
        f.setSize(new java.awt.Dimension(300, 300));
        f.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}
