/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.mvc.view.main;

import ensino.configuracoes.controller.DocenteController;
import ensino.instalacao.PanelInformacao;
import ensino.patterns.factory.ControllerFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author nicho
 */
public class Inicializacao {

    public static void main(String args[]) {
        try {
            /**
             * Abre o programa que fará a inicialização do sistema. Será executado
             * somente se o docente ainda não tenha sido cadastrado no sistema
             */
            DocenteController col = ControllerFactory.createDocenteController();
            if (col.listar().isEmpty()) {
                PanelInformacao info = new PanelInformacao();
                info.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                info.setLocationRelativeTo(null);
                info.pack();
                info.setVisible(true);
            } else {
                
                /* Set the Nimbus look and feel */
                //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
                /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
                * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
                */
                try {
                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                            UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                    }
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                //</editor-fold>

                //</editor-fold>
                
                /* Create and display the form */
                java.awt.EventQueue.invokeLater(() -> {
                    MainFrame m = new MainFrame();
                    m.setLocationRelativeTo(null);
                    m.setVisible(true);
                });
            }
        } catch (Exception ex) {
            Logger.getLogger(Inicializacao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
