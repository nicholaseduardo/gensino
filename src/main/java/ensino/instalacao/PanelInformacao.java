/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.instalacao;

import ensino.components.GenJButton;
import ensino.components.GenJCheckBox;
import ensino.components.GenJLabel;
import ensino.components.GenJTextArea;
import ensino.configuracoes.controller.BibliografiaController;
import ensino.configuracoes.controller.InstrumentoAvaliacaoController;
import ensino.configuracoes.controller.LegendaController;
import ensino.configuracoes.controller.RecursoController;
import ensino.configuracoes.controller.TecnicaController;
import ensino.configuracoes.view.panels.campus.CampusFields;
import ensino.configuracoes.view.panels.docente.DocenteFields;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.ControllerFactory;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author nicho
 */
public class PanelInformacao extends JFrame {

    private CardLayout layout;
    private GenJCheckBox check;
    private GenJButton btFechar;
    private GenJButton btProximo;
    private GenJButton btAnterior;
    private JPanel cardPanel;
    private String selectedCard;

    public PanelInformacao() {
        super();
        initComponents();
    }

    private void initComponents() {
        setTitle("Inicialização do Sistema GESTINO");
        setLayout(new BorderLayout(5, 5));
        layout = new CardLayout(5, 5);
        cardPanel = new JPanel(layout);
        add(cardPanel, BorderLayout.CENTER);

        /**
         * Título do painel de informação
         */
        GenJLabel lblTitulo = new GenJLabel("Configuração inicial do Sistema de Gestão de Planos de Ensino");
        lblTitulo.resetFontSize(18);
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        cardPanel.add(infoPanel(), "panel.info");
        cardPanel.add(docentePanel(), "panel.docente");
        cardPanel.add(new CampusFields(null), "panel.campus");
        cardPanel.add(loadingPanel(), "panel.loading");
        selectedCard = "panel.info";
        layout.show(cardPanel, selectedCard);

        URL urlBack = getClass().getResource("/img/backward-icon-25px.png");
        URL urlFor = getClass().getResource("/img/forward-icon-25px.png");
        URL urlClose = getClass().getResource("/img/close-icon-25px.png");
        
        ButtonAction action = new ButtonAction();
        btAnterior = new GenJButton("Anterior", new ImageIcon(urlBack));
        btAnterior.setEnabled(false);
        btAnterior.addActionListener(action);

        btProximo = new GenJButton("Próximo", new ImageIcon(urlFor));
        btProximo.setEnabled(false);
        btProximo.addActionListener(action);

        btFechar = new GenJButton("Fechar", new ImageIcon(urlClose));
        btFechar.addActionListener(action);

        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelButton.add(btAnterior);
        panelButton.add(btProximo);
        panelButton.add(btFechar);
        add(panelButton, BorderLayout.SOUTH);
    }

    private JPanel loadingPanel() {
        URL url = getClass().getResource("/img/loading.gif");
        GenJLabel label = new GenJLabel("Importando dados de base", new ImageIcon(url), JLabel.CENTER);
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.BOTTOM);
        label.resetFontSize(20);
        JPanel panel = new JPanel();
        panel.add(label);
        return panel;
    }

    private JPanel infoPanel() {
        GenJTextArea areaTexto = new GenJTextArea(40, 10);
        areaTexto.setEditable(false);
        areaTexto.setText(loadInfoFile());

        JScrollPane scrollPanel = new JScrollPane();
        scrollPanel.setViewportView(areaTexto);
        scrollPanel.setPreferredSize(new Dimension(300, 250));

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(scrollPanel, BorderLayout.CENTER);

        check = new GenJCheckBox("Aceita os termos?");
        check.addActionListener(new ButtonAction());
        panel.add(check, BorderLayout.SOUTH);

        return panel;
    }

    private String loadInfoFile() {
        InputStreamReader reader = null;
        try {
            InputStream is = getClass().getResourceAsStream("/txt/install-orientation.txt");
            reader = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(reader);
            StringBuilder sb = new StringBuilder();
            while (br.ready()) {
                sb.append(br.readLine() + "\n");
            }
            br.close();
            return sb.toString();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PanelInformacao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PanelInformacao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(PanelInformacao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    private JPanel docentePanel() {
        return new DocenteFields(null);
    }
    
    /**
     * Método utilizado para importar os dados de legenda, recursos, técnicas e
     * instrumentos de avaliação direto do ambiente de desenvolvimento:
     * github.com
     */
    private void importarDados() {
        if (isOnline()) {
            try {
                InputStream is = getClass().getResourceAsStream("/txt/url-imports.txt");
                Scanner s = new Scanner(is);
                AbstractController localController = null, onlineController = null;
                while (s.hasNext()) {
                    String sUrl = s.nextLine();
                    URL url = new URL(sUrl);
                    if (sUrl.matches(".+(legenda.xml).*")) {
                        onlineController = new LegendaController(url);
                        localController = ControllerFactory.createLegendaController();
                    } else if (sUrl.matches(".+(recurso.xml).*")) {
                        onlineController = new RecursoController(url);
                        localController = ControllerFactory.createRecursoController();
                    } else if (sUrl.matches(".+(tecnica.xml).*")) {
                        onlineController = new TecnicaController(url);
                        localController = ControllerFactory.createTecnicaController();
                    } else if (sUrl.matches(".+(instrumentoAvaliacao.xml).*")) {
                        onlineController = new InstrumentoAvaliacaoController(url);
                        localController = ControllerFactory.createInstrumentoAvaliacaoController();
                    } else if (sUrl.matches(".+(bibliografia.xml).*")) {
                        onlineController = new BibliografiaController(url);
                        localController = ControllerFactory.createBibliografiaController();
                    }

                    if (onlineController != null && localController != null) {
                        List list = onlineController.listar();
                        for (int i = 0; i < list.size(); i++) {
                            Object o = list.get(i);
                            localController.salvar(o);
                        }
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro no sistema. Contate o desenvolvedor relatando o erro: " + ex.getMessage()
                        + ex.getCause() != null ? "\nCausa: " + ex.getCause().getMessage() : "",
                        "Erro de sistema", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Boolean isOnline() {
        String errorMessage = "";
        while (true) {
            try {
                URL mandarMail = new URL("https://www.github.com");
                URLConnection conn = mandarMail.openConnection();

                HttpURLConnection httpConn = (HttpURLConnection) conn;
                httpConn.connect();
                int x = httpConn.getResponseCode();
                if (x == 200) {
                    return true;
                }
            } catch (MalformedURLException ex) {
                errorMessage = ex.getMessage();
            } catch (IOException ex) {
                errorMessage = ex.getMessage();
            }
            if (!"".equals(errorMessage)) {
                JOptionPane.showMessageDialog(this,
                        "Não é possível importar os dados básicos do sistema.\n"
                        + "Motivo: sem acesso a internet.",
                        "Aviso de sistema",
                        JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
        }
    }

    private class ButtonAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == btFechar) {
                PanelInformacao.this.dispose();
            } else if (source == check) {
                btProximo.setEnabled(check.isSelected());
            } else if (source == btAnterior) {
                if ("panel.docente".equals(selectedCard)) {
                    selectedCard = "panel.info";
                    layout.show(cardPanel, selectedCard);
                    btAnterior.setEnabled(false);
                } else if ("panel.campus".equals(selectedCard)) {
                    selectedCard = "panel.docente";
                    layout.show(cardPanel, selectedCard);
                }
            } else if (source == btProximo) {
                if ("panel.info".equals(selectedCard)) {
                    selectedCard = "panel.docente";
                    layout.show(cardPanel, selectedCard);
                    btAnterior.setEnabled(true);
//                    txtNome.requestFocusInWindow();
                } else if ("panel.docente".equals(selectedCard)) {
                    selectedCard = "panel.campus";
                    layout.show(cardPanel, selectedCard);
                } else {
                    selectedCard = "panel.loading";
                    layout.show(cardPanel, selectedCard);
//                    if ("".equals(txtNome.getText())) {
//                        JOptionPane.showMessageDialog(PanelInformacao.this,
//                                "O nome não foi informado. Favor, preencha o seu nome",
//                                "Aviso",
//                                JOptionPane.INFORMATION_MESSAGE);
//                        txtNome.requestFocusInWindow();
//                        return;
//                    }
//                    /**
//                     * Salva o nome digitado como sendo o nome do docente
//                     */
//                    salvarNome();
                    /**
                     * Importa os dados padrões do sistema
                     */
                    importarDados();
                    JOptionPane.showMessageDialog(PanelInformacao.this, "Inicialização completa.\n"
                            + "Agora você pode utilizar o sistema de gestão\n"
                            + "de planos de ensino.\n\n"
                            + "Atenciosamente,\n"
                            + "O desenvolvedor");
                    dispose();
                }
            }
        }

    }

    public static void main(String args[]) {
        PanelInformacao info = new PanelInformacao();
        info.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        info.setLocationRelativeTo(null);
        info.pack();
        info.setVisible(true);
    }

}
