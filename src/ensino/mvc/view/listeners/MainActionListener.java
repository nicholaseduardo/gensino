/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.mvc.view.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

/**
 *
 * @author nicho
 */
public class MainActionListener implements ActionListener {

    private JDesktopPane desktop;

    public MainActionListener(JDesktopPane desktop) {
        this.desktop = desktop;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Alterar para recuperar o texto de um recurso
        if ("Sair".equals(e.getActionCommand())) {
            System.exit(0);
        } else {
            try {
                switch (e.getActionCommand()) {
                    case "Configurações":
                        JInternalFrame fConfiguracao = new ensino.configuracoes.view.frame.FrameConfiguracao();
                        desktop.add(fConfiguracao);
                        fConfiguracao.setVisible(true);
                        fConfiguracao.setSelected(true);
                        fConfiguracao.pack();
                        break;
                    case "Campi":
                        JInternalFrame fCampus = new ensino.configuracoes.view.frame.FrameCampus();
                        desktop.add(fCampus);
                        fCampus.setVisible(true);
                        fCampus.setSelected(true);
                        fCampus.pack();
                        break;
                    case "Cursos":
                        JInternalFrame fCursos = new ensino.configuracoes.view.frame.FrameCursos();
                        desktop.add(fCursos);
                        fCursos.setVisible(true);
                        fCursos.setSelected(true);
                        fCursos.pack();
                        break;
                    case "Turma":
                        JInternalFrame fTurma = new ensino.configuracoes.view.frame.FrameTurma();
                        desktop.add(fTurma);
                        fTurma.setVisible(true);
                        fTurma.setSelected(true);
                        fTurma.pack();
                        break;
                    case "UnidadeCurricular":
                        JInternalFrame fUnidadeCurricular = new ensino.configuracoes.view.frame.FrameUnidadeCurricular();
                        desktop.add(fUnidadeCurricular);
                        fUnidadeCurricular.setVisible(true);
                        fUnidadeCurricular.setSelected(true);
                        fUnidadeCurricular.pack();
                        break;  
                    case "Calendário":
                        JInternalFrame fCalendario = new ensino.configuracoes.view.frame.FrameCalendario();
                        desktop.add(fCalendario);
                        fCalendario.pack();
                        fCalendario.setVisible(true);
                        fCalendario.setSelected(true);
                        break;
                    case "Bibliografia":
                        JInternalFrame fBibliografia = new ensino.configuracoes.view.frame.FrameBibliografia();
                        desktop.add(fBibliografia);
                        fBibliografia.setVisible(true);
                        fBibliografia.setSelected(true);
                        fBibliografia.pack();
                        break;
                    case "PlanoEnsino":
                        JInternalFrame fPlanoEnsino = new ensino.planejamento.view.frame.FramePlanoDeEnsino();
                        desktop.add(fPlanoEnsino);
                        fPlanoEnsino.setVisible(true);
                        fPlanoEnsino.setSelected(true);
                        fPlanoEnsino.pack();
                        break;
                }
            } catch (PropertyVetoException ex) {
                Logger.getLogger(MainActionListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
