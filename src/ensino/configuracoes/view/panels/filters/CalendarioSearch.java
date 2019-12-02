/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.filters;

import ensino.components.GenJButton;
import ensino.components.GenJTextField;
import ensino.configuracoes.controller.CalendarioController;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.view.panels.CalendarioPanel;
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
public class CalendarioSearch extends JPanel {

    private GenJTextField txtAno;
    private GenJTextField txtDescricao;
    private GenJButton btSearch;

    private Calendario objectValue;
    private Campus selectedCampus;

    public CalendarioSearch() {
        this(null);
    }
    
    /**
     * Construtor da classe. Exige que um campus seja informado para que o
     * componente funcione adequadamente.
     * 
     * @param selectedCampus    Objeto da classe <code>Campus</code>
     */
    public CalendarioSearch(Campus selectedCampus) {
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
        txtAno = new GenJTextField(4, false);
        txtAno.addActionListener(searchListener);
        txtAno.addFocusListener(new SearchFocusListener());

        txtDescricao = new GenJTextField(20, false);
        txtDescricao.setEditable(false);

        String source = String.format("/img/%s", "search-button-25px.png");
        btSearch = new GenJButton(new ImageIcon(getClass().getResource(source)));
        btSearch.addActionListener(searchListener);

        add(txtAno);
        add(btSearch);
        add(txtDescricao);
    }

    /**
     * Atribui o foco no campo ID
     */
    public void requestFocusOnId() {
        txtAno.requestFocusInWindow();
    }

    public void setEnable(boolean enable) {
        txtAno.setEnabled(enable);
        txtDescricao.setEnabled(enable);
        btSearch.setEnabled(enable);
    }

    public void setObjectValue(Calendario calendario) {
        if (calendario != null) {
            this.objectValue = calendario;
            txtAno.setText(calendario.getAno().toString());
            txtDescricao.setText(calendario.getDescricao());
            btSearch.requestFocusInWindow();
        } else {
            this.objectValue = null;
            txtAno.setText("");
            txtDescricao.setText("");
        }
    }

    public Calendario getObjectValue() {
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
            
            CalendarioController col = new CalendarioController();
            String sid = txtAno.getText();
            // nenhum codigo foi digitado
            if (e.getSource() == btSearch) {
                // abre o frame de busca
                setObjectValue(null);
                JDialog dialog = new JDialog();
                dialog.setTitle("Pesquisar Calendario");
                dialog.setModal(true);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                
                CalendarioPanel bPanel = new CalendarioPanel(dialog, selectedCampus);
                bPanel.createSelectButton();
                dialog.getContentPane().add(bPanel);
                dialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        Object selected = bPanel.getSelectedObject();
                        if (selected != null) {
                            setObjectValue((Calendario) selected);
                        }
                    }
                });
                dialog.pack();
                dialog.setVisible(true);

            } else if (!"".equals(sid))  {
                if (!sid.matches("\\d+")) {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Identificador inválido", "Aviso", JOptionPane.WARNING_MESSAGE);
                    txtAno.requestFocusInWindow();
                    txtAno.selectAll();
                } else {
                    Integer id = Integer.parseInt(sid);
                    setObjectValue((Calendario) col.buscarPor(id, selectedCampus.getId()));
                }
            } else {
                setObjectValue(null);
            }
        } catch (IOException | ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(CalendarioSearch.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addDocumentListener(DocumentListener l) {
        txtDescricao.getDocument().addDocumentListener(l);
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
            if (e.getSource() == txtAno) {
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
}
