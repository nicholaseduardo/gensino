/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.filters;

import ensino.components.GenJButton;
import ensino.components.GenJTextField;
import ensino.configuracoes.controller.ReferenciaBibliograficaController;
import ensino.configuracoes.controller.UnidadeCurricularController;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.view.panels.unidadeCurricular.UnidadeCurricularPanel;
import ensino.patterns.factory.ControllerFactory;
import java.awt.AWTEvent;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.DocumentListener;

/**
 *
 * @author nicho
 */
public class UnidadeCurricularSearch extends JPanel {

    private GenJTextField txtId;
    private GenJTextField txtNome;
    private GenJButton btSearch;

    private UnidadeCurricular objectValue;
    private Curso selectedCurso;

    public UnidadeCurricularSearch() {
        this(null);
    }

    /**
     * Construtor da classe. Exige que um curso seja informado para que o
     * componente funcione adequadamente.
     *
     * @param selectedCurso Objeto da classe <code>Curso</code>
     */
    public UnidadeCurricularSearch(Curso selectedCurso) {
        super();
        this.selectedCurso = selectedCurso;
        initComponents();
    }

    /**
     * Para que a busca da Unidade curricular funcione corretamente torna-se
     * necessário primeiro informar o campus. Logo, é possível criar o
     * componente, porém, ele funcionará somente quando o campus for informado.
     *
     * @param curso
     */
    public void setSelectedCurso(Curso curso) {
        this.selectedCurso = curso;
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

    public void setObjectValue(UnidadeCurricular unidade) {
        if (unidade != null) {
            try {
                // Busca as referencias bibliograficas
                if (unidade.getReferenciasBibliograficas().isEmpty()) {
                    ReferenciaBibliograficaController col = ControllerFactory.createReferenciaBibliograficaController();
                    unidade.setReferenciasBibliograficas(col.listar(unidade));
                    col.close();
                }
                this.objectValue = unidade;
                txtId.setText(unidade.getId().getId().toString());
                txtNome.setText(unidade.getNome());
                btSearch.requestFocusInWindow();
            } catch (Exception ex) {
                Logger.getLogger(UnidadeCurricularSearch.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            this.objectValue = null;
            txtId.setText("");
            txtNome.setText("");
        }
    }

    public UnidadeCurricular getObjectValue() {
        return this.objectValue;
    }

    public void onSearchListener(AWTEvent e) {
        try {
            if (selectedCurso == null) {
                JOptionPane.showMessageDialog(this,
                        "O Curso não foi selecionado. Entre em contato com o administrador!",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                setObjectValue(null);
                return;
            }

            String sid = txtId.getText();
            // nenhum codigo foi digitado
            if (e.getSource() == btSearch) {
                // abre o frame de busca
                setObjectValue(null);
                JDialog dialog = new JDialog();
                dialog.setTitle("Pesquisar Unidade Curricular");
                dialog.setModal(true);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

                UnidadeCurricularPanel bPanel = new UnidadeCurricularPanel(dialog, selectedCurso);
                bPanel.createSelectButton();
                dialog.getContentPane().add(bPanel);
                dialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        Object selected = bPanel.getSelectedObject();
                        if (selected != null) {
                            setObjectValue((UnidadeCurricular) selected);
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
                    Long id = Long.parseLong(sid);
                    UnidadeCurricularController col = ControllerFactory.createUnidadeCurricularController();
                    setObjectValue((UnidadeCurricular) col.buscarPor(id,
                            selectedCurso));
                    col.close();
                }
            } else {
                setObjectValue(null);
            }
        } catch (Exception ex) {
            Logger.getLogger(UnidadeCurricularSearch.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addDocumentListener(DocumentListener l) {
        txtNome.getDocument().addDocumentListener(l);
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
}
