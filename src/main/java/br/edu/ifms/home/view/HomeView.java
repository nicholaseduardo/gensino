/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifms.home.view;

import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 *
 * @author 1513003
 */
public class HomeView extends JFrame {

    private JDesktopPane desktop;
    private JMenuBar menuPrincipal;
    
    private JMenu menuArquivo;
    private JMenuItem menuItemSair;

    public HomeView() {

    }

    private void initComponents() {
        /**
         * Criação do menu principal da aplicação.
         */
        menuPrincipal = new JMenuBar();
        setJMenuBar(menuPrincipal);
        
        /**
         * Criação da área principal da aplicação que gerenciará as janelas
         * internas.
         */
        desktop = new JDesktopPane();

        /**
         * Vinculo da área principal ao componente
         */
        JPanel contentPanel = new JPanel(new BorderLayout(0, 0));
        contentPanel.add(desktop, BorderLayout.CENTER);
        setContentPane(contentPanel);
    }
    
    public ImageIcon loadIcon(String resource) {
        return new ImageIcon(getClass().getResource(resource));
    }
    
    private void createMenus() {
        menuArquivo = new JMenu("Arquivo");
        
        menuPrincipal.add(menuArquivo);
        
        menuItemSair = new JMenuItem("Sair", loadIcon("/img/icons/exit-48.png"));
        menuArquivo.add(menuItemSair);
    }
}
