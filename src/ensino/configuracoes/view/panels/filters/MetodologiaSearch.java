/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.filters;

import ensino.components.GenJButton;
import ensino.components.GenJTextField;
import ensino.configuracoes.controller.RecursoController;
import ensino.configuracoes.controller.TecnicaController;
import ensino.configuracoes.view.panels.InstrumentoAvaliacaoPanel;
import ensino.configuracoes.view.panels.RecursoPanel;
import ensino.configuracoes.view.panels.TecnicaPanel;
import ensino.defaults.DefaultFormPanel;
import ensino.patterns.AbstractController;
import ensino.patterns.BaseObject;
import ensino.util.types.TipoMetodo;
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
public class MetodologiaSearch extends JPanel {

    private GenJTextField txtId;
    private GenJTextField txtNome;
    private GenJButton btSearch;

    private BaseObject objectValue;
    private TipoMetodo tipoMetodo;

    public MetodologiaSearch() {
        this(TipoMetodo.TECNICA);
    }
    
    /**
     * Construtor da classe. Exige que um tipo de metodo seja informado para que
     * o componente funcione adequadamente
     * @param tipoMetodo 
     */
    public MetodologiaSearch(TipoMetodo tipoMetodo) {
        super();
        this.tipoMetodo = tipoMetodo;
        initComponents();
    }
    
    /**
     * Para que a busca da metodologia funcione corretamente, torna-se necessário
     * primeiro informar o tipo do método. Logo, é possível criar o componente.
     * Porém, ele funcionará somente quando o tipo for informado
     * @param tipoMetodo 
     */
    public void setTipoMetodo(TipoMetodo tipoMetodo) {
        this.tipoMetodo = tipoMetodo;
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

    public void setObjectValue(BaseObject object) {
        if (object != null) {
            this.objectValue = object;
            txtId.setText(object.getId().toString());
            txtNome.setText(object.getNome());
            btSearch.requestFocusInWindow();
        } else {
            this.objectValue = null;
            txtId.setText("");
            txtNome.setText("");
        }
    }

    public BaseObject getObjectValue() {
        return this.objectValue;
    }

    public void onSearchListener(AWTEvent e) {
        try {
            if (tipoMetodo == null) {
                JOptionPane.showMessageDialog(this,
                        "O tipo do método não foi selecionado. Entre em contato com o administrador!",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                setObjectValue(null);
                return;
            }

            AbstractController col;
            DefaultFormPanel bPanel;
            JDialog dialog = new JDialog();
            switch (tipoMetodo) {
                default:
                case TECNICA:
                    col = new TecnicaController();
                    bPanel = new TecnicaPanel(dialog);
                    break;
                case RECURSO:
                    col = new RecursoController();
                    bPanel = new RecursoPanel(dialog);
                    break;
                case INSTRUMENTO:
                    col = new RecursoController();
                    bPanel = new InstrumentoAvaliacaoPanel(dialog);
                    break;
            }
            String sid = txtId.getText();
            // nenhum codigo foi digitado
            if (e.getSource() == btSearch) {
                // abre o frame de busca
                setObjectValue(null);
                dialog.setTitle(String.format("Pesquisar %s", tipoMetodo.toString()));
                dialog.setModal(true);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

                bPanel.createSelectButton();
                dialog.getContentPane().add(bPanel);
                dialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        BaseObject selected = (BaseObject)bPanel.getSelectedObject();
                        if (selected != null) {
                            setObjectValue(selected);
                        }
                    }
                });
                dialog.pack();
                dialog.setVisible(true);

            } else if (!"".equals(sid)) {
                if (!sid.matches("\\d+")) {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Identificador inválido", "Aviso", JOptionPane.WARNING_MESSAGE);
                    txtId.requestFocusInWindow();
                    txtId.selectAll();
                } else {
                    Integer id = Integer.parseInt(sid);
                    setObjectValue((BaseObject) col.buscarPorId(id));
                }
            } else {
                setObjectValue(null);
            }
        } catch (IOException | ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(MetodologiaSearch.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private class SearchFocusListener implements FocusListener {

        @Override
        public void focusGained(FocusEvent e) {
            if (e.getSource() instanceof GenJTextField) {
                GenJTextField source = (GenJTextField) e.getSource();
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
}
