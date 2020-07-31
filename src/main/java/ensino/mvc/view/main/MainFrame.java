/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.mvc.view.main;

import ensino.configuracoes.model.Campus;
import ensino.configuracoes.view.frame.FrameDocente;
import ensino.configuracoes.view.frame.FrameInstrumentoAvaliacao;
import ensino.configuracoes.view.frame.FrameLegenda;
import ensino.configuracoes.view.frame.FrameNivelEnsino;
import ensino.configuracoes.view.frame.FrameRecurso;
import ensino.configuracoes.view.frame.FrameTecnica;
import ensino.patterns.factory.ControllerFactory;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

/**
 *
 * @author nicho
 */
public class MainFrame extends javax.swing.JFrame {

    private JDesktopPane desktop;
    private HashMap<MenuOpcoes, String> mapPathIcons;
    private List<JButton> listButtons;
    private JInternalFrame framePainel;
    private JInternalFrame frameButtons;

    private BufferedImage img;

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        listButtons = new ArrayList();
        initComponents();

        URL url = getClass().getResource("/img/background-white.png");
        try {
            img = ImageIO.read(url);
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        desktop = new JDesktopPane() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.white);
                Dimension d = getSize();
                g.fillRect(0, 0, d.width, d.height);
            }
        };

        JPanel contentPanel = new JPanel(new BorderLayout(0, 0));
        contentPanel.add(desktop, BorderLayout.CENTER);
        setContentPane(contentPanel);

        initIconsPath();
        initListeners();
        initDesktopView();
    }

    private void initDesktopView() {
        frameButtons = new JInternalFrame();
        framePainel = new JInternalFrame();
        JPanel paneButtons = new JPanel();

        for (JButton b : listButtons) {
            paneButtons.add(b);
        }
        frameButtons.getContentPane().add(paneButtons);

        frameButtons.setMaximizable(false);
        frameButtons.setResizable(false);
        frameButtons.setIconifiable(false);
        frameButtons.setClosable(false);
        frameButtons.pack();

        frameButtons.setVisible(true);
        desktop.add(frameButtons);

        Dimension dd = desktop.getPreferredSize(),
                df = frameButtons.getSize(),
                d = null;

        Campus campusVigente = ControllerFactory.getCampusVigente();
        if (campusVigente != null) {
            AreaDeTrabalhoView p = new AreaDeTrabalhoView(campusVigente, framePainel);
            p.setDesktop(desktop);

            JScrollPane scroll = new JScrollPane(p);
            scroll.setAutoscrolls(true);

            framePainel.setTitle("Lista de Cursos do Campus");
            framePainel.getContentPane().add(scroll);
            framePainel.setMaximizable(false);
            framePainel.setResizable(false);
            framePainel.setIconifiable(false);
            framePainel.setClosable(false);
            framePainel.pack();
            framePainel.setVisible(true);

            desktop.add(framePainel);
        }

        JInternalFrame frames[] = desktop.getAllFrames();
        if (frames.length > 0) {
            try {
                frames[frames.length - 1].setSelected(true);
            } catch (PropertyVetoException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void initIconsPath() {
        mapPathIcons = new HashMap();
        mapPathIcons.put(MenuOpcoes.CAMPI, "/img/university-icon-50px.png");
        mapPathIcons.put(MenuOpcoes.CALENDARIO, "/img/calendar-image-png-50px.png");
        mapPathIcons.put(MenuOpcoes.BIBLIOGRAFIA, "/img/library-icon-50px.png");
        mapPathIcons.put(MenuOpcoes.SAIR, "/img/exit-button-50px.png");
    }

    private void addIcon(String text, String sCommand,
            Icon icon, MainActionListener listener) {
        JButton button = new JButton(text, icon);
        button.setActionCommand(sCommand);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setFocusable(false);
        button.setPreferredSize(new Dimension(120, 100));

        button.addActionListener(listener);
        button.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200, 60)));

        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                Object o = evt.getSource();
                if (o instanceof JButton) {
                    JButton b = (JButton) o;
                    b.setBackground(Color.GRAY);
                }
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                Object o = evt.getSource();
                if (o instanceof JButton) {
                    JButton b = (JButton) o;
                    b.setBackground(UIManager.getColor("control"));
                }
            }
        });

        listButtons.add(button);
    }

    private void addFrame(JInternalFrame frame) throws PropertyVetoException {
        Dimension d = desktop.getSize(), df = null;

        frame.setMaximizable(true);
        frame.setIconifiable(true);
        frame.setClosable(true);
        frame.pack();

        df = frame.getSize();
        int x = (d.width / 2) - (df.width / 2),
                y = (d.height / 2) - (df.height / 2);
        frame.setLocation(new Point(x, y));
        frame.setVisible(true);
        desktop.add(frame);

        JInternalFrame frames[] = desktop.getAllFrames();
        if (frames.length > 0) {
            frames[frames.length - 1].setSelected(true);
        }
    }

    private void updateComponents() {
        Integer x = 10, y = 20;
        Dimension distance = new Dimension(140, 140),
                dDesktop = desktop.getSize();
        Integer nRowButtons = dDesktop.height / distance.height,
                nColButtons = dDesktop.width / distance.width,
                inc = 0, nButtons = listButtons.size();
        Integer col = 1;
        Dimension dbf = null, dpf = null;
        if (nButtons > nRowButtons && nRowButtons > 0) {
            col = nButtons / nRowButtons;
            dbf = new Dimension(col * distance.width, nRowButtons * distance.height);
        } else {
            dbf = new Dimension(distance.width, nButtons * distance.height);
        }
        if (frameButtons != null) {
            frameButtons.setSize(dbf);
        }

        if (framePainel != null) {
            dpf = framePainel.getSize();
            dpf = new Dimension(dpf.width + 5, dpf.height + 5);
            framePainel.setSize(dpf);
            framePainel.setLocation(new Point(dbf.width + 5, 0));
        }
    }

    private void createButtons(MainActionListener listener) {
        for (MenuOpcoes menu : MenuOpcoes.values()) {
            Icon icon = new ImageIcon(getClass().getResource(mapPathIcons.get(menu)));
            addIcon(menu.toString(), menu.getValue(), icon, listener);
        }
        updateComponents();
    }

    private void initListeners() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                updateComponents();
            }
        });

        MainActionListener mainListener = new MainActionListener();
        menuItemCampus.addActionListener(mainListener);
        menuItemCalendario.addActionListener(mainListener);
        menuItemBibliografia.addActionListener(mainListener);
        menuItemPlanoDeEnsino.addActionListener(mainListener);
        menuItemSair.addActionListener(mainListener);

        menuItemCampus.setActionCommand(MenuOpcoes.CAMPI.getValue());
        menuItemCalendario.setActionCommand(MenuOpcoes.CALENDARIO.getValue());
        menuItemBibliografia.setActionCommand(MenuOpcoes.BIBLIOGRAFIA.getValue());
        menuItemSair.setActionCommand(MenuOpcoes.SAIR.getValue());

        createButtons(mainListener);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private class MainActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            MenuOpcoes menu = MenuOpcoes.of(e.getActionCommand());

            try {
                switch (menu) {
                    case CAMPI:
                        addFrame(new ensino.configuracoes.view.frame.FrameCampus(framePainel));
                        break;
                    case CALENDARIO:
                        addFrame(new ensino.configuracoes.view.frame.FrameCalendario());
                        break;
                    case BIBLIOGRAFIA:
                        addFrame(new ensino.configuracoes.view.frame.FrameBibliografia());
                        break;
                    case SAIR:

                        Runtime rt = Runtime.getRuntime();
                        System.out.println("\nMemória depois da criação dos objetos: " + rt.freeMemory());
                        rt.gc();
                        System.out.println("Memória depois executar o gc: " + rt.freeMemory());
                        
                        System.exit(0);
                        break;
                }
            } catch (PropertyVetoException ex) {
                Logger.getLogger(MainActionListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private enum MenuOpcoes {
        CAMPI("campi"), CALENDARIO("cal"),
        BIBLIOGRAFIA("bib"), SAIR("sair");

        private String value;

        MenuOpcoes(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static MenuOpcoes of(String value) {
            return Stream.of(MenuOpcoes.values()).filter(t -> t.getValue().equals(value))
                    .findFirst().orElseThrow(IllegalArgumentException::new);
        }

        @Override
        public String toString() {
            switch (value) {
                case "campi":
                    return "Campi";
                case "cal":
                    return "Calendário";
                case "curso":
                    return "Curso";
                case "turma":
                    return "Turma";
                case "undCurr":
                    return "Unidade Curricular";
                case "bib":
                    return "Bibliografia";
                case "planoEnsino":
                    return "Plano de Ensino";
                default:
                case "sair":
                    return "Sair";
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuPrincipal = new javax.swing.JMenuBar();
        menuArquivo = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        menuItemDocente = new javax.swing.JMenuItem();
        menuItemLegenda = new javax.swing.JMenuItem();
        menuItemNivelEnsino = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        menuItemRecurso = new javax.swing.JMenuItem();
        menuItemTecnica = new javax.swing.JMenuItem();
        menuItemInstrumentoAvaliacao = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        menuItemCampus = new javax.swing.JMenuItem();
        menuItemCalendario = new javax.swing.JMenuItem();
        menuItemBibliografia = new javax.swing.JMenuItem();
        separadorArquivo = new javax.swing.JPopupMenu.Separator();
        menuItemSair = new javax.swing.JMenuItem();
        menuGerenciamento = new javax.swing.JMenu();
        menuItemPlanoDeEnsino = new javax.swing.JMenuItem();
        menuItemPlanoDeEnsinoLista = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        menuArquivo.setActionCommand("Arquivo");
        menuArquivo.setLabel("Arquivo");

        jMenu2.setText("Configurações");

        menuItemDocente.setText("Docente");
        menuItemDocente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemDocenteActionPerformed(evt);
            }
        });
        jMenu2.add(menuItemDocente);

        menuItemLegenda.setText("Legenda");
        menuItemLegenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemLegendaActionPerformed(evt);
            }
        });
        jMenu2.add(menuItemLegenda);

        menuItemNivelEnsino.setText("Níveis de ensino");
        menuItemNivelEnsino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemNivelEnsinoActionPerformed(evt);
            }
        });
        jMenu2.add(menuItemNivelEnsino);

        jMenu4.setText("Metodologia");

        menuItemRecurso.setText("Recursos");
        menuItemRecurso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemRecursoActionPerformed(evt);
            }
        });
        jMenu4.add(menuItemRecurso);

        menuItemTecnica.setText("Técnica");
        menuItemTecnica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemTecnicaActionPerformed(evt);
            }
        });
        jMenu4.add(menuItemTecnica);

        menuItemInstrumentoAvaliacao.setText("Instrumentos de Avaliação");
        menuItemInstrumentoAvaliacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemInstrumentoAvaliacaoActionPerformed(evt);
            }
        });
        jMenu4.add(menuItemInstrumentoAvaliacao);

        jMenu2.add(jMenu4);

        menuArquivo.add(jMenu2);

        jMenu1.setText("Cadastros Gerais");

        menuItemCampus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/university-icon-25px.png"))); // NOI18N
        menuItemCampus.setText("Campi");
        jMenu1.add(menuItemCampus);

        menuItemCalendario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/calendar-image-png-25px.png"))); // NOI18N
        menuItemCalendario.setText("Calendário");
        jMenu1.add(menuItemCalendario);

        menuItemBibliografia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/library-icon-25px.png"))); // NOI18N
        menuItemBibliografia.setText("Bibliografia");
        jMenu1.add(menuItemBibliografia);

        menuArquivo.add(jMenu1);
        menuArquivo.add(separadorArquivo);

        menuItemSair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/exit-button-25px.png"))); // NOI18N
        menuItemSair.setLabel("Sair");
        menuArquivo.add(menuItemSair);

        menuPrincipal.add(menuArquivo);

        menuGerenciamento.setText("Gerenciamento");

        menuItemPlanoDeEnsino.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/plano-icon-25px.png"))); // NOI18N
        menuItemPlanoDeEnsino.setText("Plano de Ensino (Árvore)");
        menuItemPlanoDeEnsino.setActionCommand("PlanoEnsino");
        menuGerenciamento.add(menuItemPlanoDeEnsino);

        menuItemPlanoDeEnsinoLista.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/plano-icon-25px.png"))); // NOI18N
        menuItemPlanoDeEnsinoLista.setText("Plano de Ensino (Lista)");
        menuItemPlanoDeEnsinoLista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemPlanoDeEnsinoListaActionPerformed(evt);
            }
        });
        menuGerenciamento.add(menuItemPlanoDeEnsinoLista);

        menuPrincipal.add(menuGerenciamento);

        setJMenuBar(menuPrincipal);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 392, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 417, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuItemDocenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemDocenteActionPerformed
        try {
            addFrame(new FrameDocente());
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_menuItemDocenteActionPerformed

    private void menuItemLegendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemLegendaActionPerformed
        try {
            // TODO add your handling code here:
            addFrame(new FrameLegenda());
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_menuItemLegendaActionPerformed

    private void menuItemNivelEnsinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemNivelEnsinoActionPerformed
        try {
            // TODO add your handling code here:
            addFrame(new FrameNivelEnsino());
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_menuItemNivelEnsinoActionPerformed

    private void menuItemRecursoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemRecursoActionPerformed
        try {
            // TODO add your handling code here:
            addFrame(new FrameRecurso());
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_menuItemRecursoActionPerformed

    private void menuItemTecnicaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemTecnicaActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            addFrame(new FrameTecnica());
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_menuItemTecnicaActionPerformed

    private void menuItemInstrumentoAvaliacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemInstrumentoAvaliacaoActionPerformed
        try {
            // TODO add your handling code here:
            addFrame(new FrameInstrumentoAvaliacao());
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_menuItemInstrumentoAvaliacaoActionPerformed

    private void menuItemPlanoDeEnsinoListaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemPlanoDeEnsinoListaActionPerformed
        try {
            // TODO add your handling code here:
            addFrame(new ensino.planejamento.view.frame.FramePlanoDeEnsinoLista());
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_menuItemPlanoDeEnsinoListaActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(() -> {
//            MainFrame m = new MainFrame();
//            m.setLocationRelativeTo(null);
//            m.setVisible(true);
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu menuArquivo;
    private javax.swing.JMenu menuGerenciamento;
    private javax.swing.JMenuItem menuItemBibliografia;
    private javax.swing.JMenuItem menuItemCalendario;
    private javax.swing.JMenuItem menuItemCampus;
    private javax.swing.JMenuItem menuItemDocente;
    private javax.swing.JMenuItem menuItemInstrumentoAvaliacao;
    private javax.swing.JMenuItem menuItemLegenda;
    private javax.swing.JMenuItem menuItemNivelEnsino;
    private javax.swing.JMenuItem menuItemPlanoDeEnsino;
    private javax.swing.JMenuItem menuItemPlanoDeEnsinoLista;
    private javax.swing.JMenuItem menuItemRecurso;
    private javax.swing.JMenuItem menuItemSair;
    private javax.swing.JMenuItem menuItemTecnica;
    private javax.swing.JMenuBar menuPrincipal;
    private javax.swing.JPopupMenu.Separator separadorArquivo;
    // End of variables declaration//GEN-END:variables
}
