/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.filters;

import ensino.components.GenJButton;
import ensino.components.GenJTextField;
import ensino.configuracoes.controller.CursoController;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.view.panels.CursoPanel;
import java.awt.AWTEvent;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.DocumentListener;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class CursoSearch extends JPanel {

    private GenJTextField txtId;
    private GenJTextField txtNome;
    private GenJButton btSearch;

    private Curso objectValue;
    private Campus selectedCampus;

    public CursoSearch() {
        this(null);
    }
    
    /**
     * Construtor da classe. Exige que um campus seja informado para que o
     * componente funcione adequadamente.
     * 
     * @param selectedCampus    Objeto da classe <code>Campus</code>
     */
    public CursoSearch(Campus selectedCampus) {
        super();
        this.selectedCampus = selectedCampus;
        initComponents();
    }
    
    /**
     * Para que a busca do curso funcione corretamente torna-se necessário
     * primeiro informar o campus. Logo, é possível criar o componente, porém,
     * ele funcionará somente quando o campus for informado.
     * @param campus 
     */
    public void setSelectedCampus(Campus campus) {
        this.selectedCampus = campus;
    }

    private void initComponents() {
        SearchListener searchListener = new SearchListener();

        setLayout(new FlowLayout(FlowLayout.LEFT));
        txtId = new GenJTextField(4, false);
        txtId.addActionListener(searchListener);
        txtId.addFocusListener(new SearchFocusListener());

        txtNome = new GenJTextField(20, false);
        txtNome.setEditable(false);

        String source = String.format("/img/%s", "search-button-25px.png");
        btSearch = new GenJButton(new ImageIcon(getClass().getResource(source)));
        btSearch.addActionListener(searchListener);

        add(txtId);
        add(btSearch);
        add(txtNome);
    }

    /**
     * Atribui o foco no campo ID
     */
    public void requestFocusOnId() {
        txtId.requestFocusInWindow();
    }

    public void setEnable(boolean enable) {
        txtId.setEnabled(enable);
        txtNome.setEnabled(enable);
        btSearch.setEnabled(enable);
    }

    public void setObjectValue(Curso curso) {
        if (curso != null) {
            this.objectValue = curso;
            txtId.setText(curso.getId().toString());
            txtNome.setText(curso.getNome());
            btSearch.requestFocusInWindow();
        } else {
            this.objectValue = null;
            txtId.setText("");
            txtNome.setText("");
        }
    }

    public Curso getObjectValue() {
        return this.objectValue;
    }

    public void onSearchListener(AWTEvent e) {
        try {
            if (selectedCampus == null) {
                JOptionPane.showMessageDialog(this, 
                        "O Campus não foi selecionado. Entre em contato com o administrador!", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                setObjectValue(null);
                return;
            }
            
            CursoController col = new CursoController();
            String sid = txtId.getText();
            // nenhum codigo foi digitado
            if (e.getSource() == btSearch) {
                // abre o frame de busca
                setObjectValue(null);
                JDialog dialog = new JDialog();
                dialog.setTitle("Pesquisar Curso");
                dialog.setModal(true);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                
                CursoPanel bPanel = new CursoPanel(dialog, selectedCampus);
                bPanel.createSelectButton();
                dialog.getContentPane().add(bPanel);
                dialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        Object selected = bPanel.getSelectedObject();
                        if (selected != null) {
                            setObjectValue((Curso) selected);
                        }
                    }
                });
                dialog.pack();
                dialog.setVisible(true);

            } else if (!"".equals(sid))  {
                if (!sid.matches("\\d+")) {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Identificador inválido", "Aviso", JOptionPane.WARNING_MESSAGE);
                    txtId.requestFocusInWindow();
                    txtId.selectAll();
                } else {
                    Integer id = Integer.parseInt(sid);
                    setObjectValue((Curso) col.buscarPor(id, selectedCampus.getId()));
                }
            } else {
                setObjectValue(null);
            }
        } catch (IOException | ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(CursoSearch.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private class SearchFocusListener implements FocusListener {

        @Override
        public void focusGained(FocusEvent e) {
            if (e.getSource() instanceof GenJTextField) {
                GenJTextField source = (GenJTextField)e.getSource();
                source.selectAll();
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (e.getSource() == txtId) {
                onSearchListener(e);
            }
        }

    }

    private class SearchListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            onSearchListener(e);
        }

    }
    
    public void addDocumentListener(DocumentListener l) {
        txtNome.getDocument().addDocumentListener(l);
    }

//    public static void main(String args[]) {
//        JFrame frame = new JFrame();
//        CursoSearch c = new CursoSearch();
//        c.setSelectedCampus(new Campus(1, "Teste"));
//        c.addDocumentListener(new DocumentListener() {
//            @Override
//            public void insertUpdate(DocumentEvent e) {
//                System.out.println("Inserido ");
//                System.out.println(c.getObjectValue().getNome());
//            }
//
//            @Override
//            public void removeUpdate(DocumentEvent e) {
//                System.out.println("removido");
//            }
//
//            @Override
//            public void changedUpdate(DocumentEvent e) {
//                System.out.println("alterado");
//            }
//        });
//        JPanel panel = new JPanel();
//        panel.add(c);
//        panel.add(new JTextField(10));
//        frame.getContentPane().add(panel);
//        frame.pack();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setVisible(true);
//    }
}
