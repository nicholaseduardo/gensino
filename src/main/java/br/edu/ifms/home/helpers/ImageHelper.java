/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifms.home.helpers;

import javax.swing.ImageIcon;

/**
 *
 * @author 1513003
 */
public class ImageHelper {
    
    public ImageIcon loadIcon(String resource) {
        return new ImageIcon(getClass().getResource(resource));
    }
    
}
