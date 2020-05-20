/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author nicho
 */
public abstract class GenImportDialog extends JDialog {

    /**
     * Atributo utilizado para registrar os campos que serão importados do
     * arquivo de texto.
     */
    private String fields[];

    /**
     * Atributo utilizado para identificar o título que orientará o usuário no
     * processo de importação.
     */
    private String title;
    /**
     * Atributo utilizado para mostrar o texto que descreverá os passos
     * necessários para que o usuário realize a importação dos dados.
     */
    private String content;
    /**
     * Atributo utilizado para mostrar o endereço do arquivo selecionado pelo
     * usuário.
     */
    private GenJTextField txtFilePath;
    /**
     * Atributo utilizado para registrar o arquivo selecionado pelo usuário.
     */
    private File selectedFile;
    /**
     * Atributo utilizado para mostrar o progresso da importação dos dados.
     */
    private JProgressBar progressBar;
    /**
     * Atributo utilizado para armazenar o conteúdo do arquivo importado pelo
     * usuário.<br/>Neste caso, esse campo armazenará, para cada linha da lista,
     * o nome do campo como chave e o dado como um objeto do tipo texto.
     */
    private List<HashMap<String, Object>> data;
    /**
     * Atributo utilizado para identificar o botão responsável por realizar a
     * ação de buscar um arquivo.
     */
    private GenJButton btSelectFile;
    /**
     * Executa a ação de importação dos dados.
     */
    private GenJButton btImport;
    /**
     * Fecha a janela de importação.
     */
    private GenJButton btClose;
    /**
     * Atributo utilizado para identificar o separador de texto
     * <b>Formato de Vírgula (,) </b>do arquivo de texto.
     */
    protected GenJRadioButton radioVirgula;
    /**
     * Atributo utilizado para identificar o separador de texto
     * <b>Formato de Ponto e Vírgula (;) </b>do arquivo de texto.
     */
    protected GenJRadioButton radioPontoVirgula;

    /**
     * Cria um objeto da classe <code>GenImportDialog</code> de acordo com o
     * título e o conteúdo orientativo da importação<br/>
     * Essa classe importa somente arquivos do tipo <b>Texto</b>.
     *
     * @param title
     * @param content
     */
    public GenImportDialog(String title, String content) {
        super();
        super.setModal(true);
        super.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        super.setTitle("Importação de arquivo *.CSV");
        this.title = title;
        this.content = content;
        initComponents();
        super.pack();
    }

    /**
     * Cria um painel com o gerenciador de leiaute <code>FlowLayout</code>.
     *
     * @param component Qualquer componente que herde da classe
     * <code>JComponent</code>.
     * @param orietation Orientação de acordo com as opções definidas na classe
     * <code>FlowLayout</code>.<br/>
     * <ul>
     * <li>FlowLayout.LEFT</li>
     * <li>FlowLayout.CENTER</li>
     * <li>FlowLayout.RIGHT</li>
     * </ul>
     * @return
     */
    private JPanel createFlowLayoutPanel(JComponent component, int orietation) {
        JPanel panel = new JPanel(new FlowLayout(orietation, 5, 5));
        panel.add(component);
        return panel;
    }

    private void initComponents() {
        data = new ArrayList<>();
        setLayout(new BorderLayout(10, 10));

        GenJLabel lblTitle = new GenJLabel(this.title);
        lblTitle.resetFontSize(20);
        add(createFlowLayoutPanel(lblTitle, FlowLayout.CENTER),
                BorderLayout.PAGE_START);

        JPanel panelContent = new JPanel(new GridLayout(0, 1, 5, 5));
        Scanner s = new Scanner(content);
        /**
         * Verifica o conteúdo por linha e registra na tela.
         */
        while (s.hasNextLine()) {
            // se está dentro do limite, imprime a linha
            GenJLabel lblLine = new GenJLabel(s.nextLine());
            lblLine.resetFontSize(13);
            panelContent.add(createFlowLayoutPanel(lblLine, FlowLayout.LEFT));
        }

        radioVirgula = new GenJRadioButton("Por vírgula (,)");
        radioPontoVirgula = new GenJRadioButton("Por ponto e vírgula (;)");
        ButtonGroup bg = new ButtonGroup();
        bg.add(radioVirgula);
        bg.add(radioPontoVirgula);

        Border lineBorder = BorderFactory.createLineBorder(Color.black);
        Border titleBorder = BorderFactory.createTitledBorder(lineBorder,
                "Selecione o separador de texto", TitledBorder.LEFT,
                TitledBorder.TOP);

        JPanel panelRadio = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panelRadio.setBorder(titleBorder);
        panelRadio.add(radioVirgula);
        panelRadio.add(radioPontoVirgula);

        txtFilePath = new GenJTextField(30, true);
        txtFilePath.setEnabled(false);
        txtFilePath.setLabelFor("Endereço do arquivo selecionado");
        JPanel panelArquivo = createFlowLayoutPanel(txtFilePath,
                FlowLayout.LEFT);
        String source = "/img/directory-icon-25px.png";
        
        ButtonAction btAction = new ButtonAction();
        btSelectFile = new GenJButton("Selecionar arquivo");
        btSelectFile.addActionListener(btAction);
//        btSelectFile.setIcon(new ImageIcon(getClass().getResource(source)));
        panelArquivo.add(btSelectFile);
        JPanel panelProgress = new JPanel(new GridLayout(0, 1));
        panelProgress.add(panelRadio);
        panelProgress.add(panelArquivo);
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);
        panelProgress.add(progressBar);

        JPanel panelContentButton = new JPanel(new BorderLayout(5, 5));
        panelContentButton.add(panelContent, BorderLayout.CENTER);
        panelContentButton.add(panelProgress, BorderLayout.PAGE_END);
        add(panelContentButton, BorderLayout.CENTER);

        source = "/img/import-button-25px.png";
        btImport = new GenJButton("Importar");
        btImport.setIcon(new ImageIcon(getClass().getResource(source)));
        btImport.addActionListener(btAction);

        source = "/img/exit-button-25px.png";
        btClose = new GenJButton("Fechar");
        btClose.setIcon(new ImageIcon(getClass().getResource(source)));
        btClose.addActionListener(btAction);

        JPanel panelButtons = createFlowLayoutPanel(btImport, FlowLayout.RIGHT);
        panelButtons.add(btClose);
        add(panelButtons, BorderLayout.PAGE_END);
    }

    /**
     * Recupera a lista com os conteúdos importados do arquivo selecionado pelo
     * usuário.
     *
     * @return
     */
    public List<HashMap<String, Object>> getData() {
        return data;
    }

    /**
     * Método utilizado para garantir que o usuário realize a importação dos
     * dados somente quando um arquivo for selecionado.
     *
     * @return
     */
    private boolean validaCampos() {
        String msq = "O campo [%s] não foi informado!",
                campo = "";
        if ("".equals(txtFilePath.getText())) {
            txtFilePath.requestFocusInWindow();
            campo = "Selecionar arquivo";
        } else if (!radioPontoVirgula.isSelected()
                && !radioVirgula.isSelected()) {
            radioVirgula.requestFocusInWindow();
            campo = "Separador de colunas";
        } else {
            return true;
        }
        JOptionPane.showMessageDialog(this, String.format(msq, campo));
        return false;
    }

    protected void initProgressBar() {
        progressBar.setValue(0);
        progressBar.setString(String.format("0 Bytes / 0 %c", 37));
        progressBar.setVisible(true);
    }

    protected void finishProgressBar() {
        long fileSize = selectedFile.length();
        progressBar.setValue(100);
        progressBar.setString(String.format("%d Bytes / 100 %c", fileSize, 37));
    }

    /**
     * Setup para coleta de dados do arquivo. Este método deve determinar quais
     * serão os campos que devem ser importados do arquivo
     *
     * @param fields Vetor de <code>String</code> contendo a lista dos nomes dos
     * campos a serem importados.<br/>
     * A ordem de inclusão dos campos influencia no processo de importação dos
     * dados
     */
    public void setFileFields(String fields[]) {
        this.fields = fields;
    }

    private void importaDados() {
        try {
            // identifica o separador de colunas
            String sep = (radioVirgula.isSelected() ? "," : ";");
            int updatedSize = 0;
            long bytesReaded = 0;
            long fileSize = 0;
            try ( // Carrega os dados do arquivo
                    FileReader fileReader = new FileReader(selectedFile);
                    BufferedReader buffer = new BufferedReader(fileReader)) {
                /**
                 * Ignora a primeira linha considerando que ela representa os
                 * nomes dos campos
                 */
                String line = buffer.readLine();
                fileSize = selectedFile.length() - line.length();
                double lengthPerPercent = 100.0 / fileSize;
                while ((line = buffer.readLine()) != null) {
                    // Recupera os dados por linha de acordo com o separador
                    String aLine[] = line.split(sep);
                    // mapeia os dados e os armazena na lista
                    HashMap<String, Object> map = new HashMap();
                    for (int i = 0; i < fields.length; i++) {
                        map.put(fields[i], (i < aLine.length ? aLine[i] : ""));
                    }
                    data.add(map);
                    // atualiza a barra de progresso
                    bytesReaded += line.length();
                    updatedSize = (int) Math.round(bytesReaded * lengthPerPercent);
                    progressBar.setString(
                            String.format("%d Bytes / %d %c", bytesReaded,
                                    updatedSize, 37));
                    progressBar.setValue(updatedSize);
                }
            }
            finishProgressBar();
            JOptionPane.showMessageDialog(GenImportDialog.this,
                    "Dados importado com sucesso!", "Informação",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(GenImportDialog.this,
                    String.format("Erro ao tentar ler os dados do arquivo!\n[%s]",
                            ex.getMessage()),
                    "I/O Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private class ButtonAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == btSelectFile) {
                JFileChooser fc = new JFileChooser();
                fc.setFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        return f.isDirectory()
                                || f.getName().endsWith(".csv");
                    }

                    @Override
                    public String getDescription() {
                        return "Arquivos *.csv";
                    }
                });
                int returnVal = fc.showOpenDialog(GenImportDialog.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fc.getSelectedFile();
                    txtFilePath.setText(selectedFile.getName());
                }
            } else if (source == btImport && validaCampos()) {
                if (fields == null) {
                    JOptionPane.showMessageDialog(GenImportDialog.this,
                            "Os campos do arquivo de importação não foram informados!", "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                initProgressBar();
                Thread t = new Thread(() -> {
                    importaDados();
                });
                t.start();
            } else if (source == btClose) {
                dispose();
            }
        }

    }
}
