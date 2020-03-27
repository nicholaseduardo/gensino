/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels;

import ensino.components.GenJLabel;
import ensino.defaults.DefaultFieldsPanel;
import ensino.defaults.DefaultFormPanel;
import ensino.defaults.SpringUtilities;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import ensino.components.GenJTextField;
import ensino.configuracoes.model.InstrumentoAvaliacao;
import ensino.configuracoes.view.models.InstrumentoAvaliacaoTableModel;
import ensino.configuracoes.view.renderer.MetodoCellRenderer;
import ensino.patterns.factory.ControllerFactory;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;

/**
 *
 * @author nicho
 */
public class InstrumentoAvaliacaoPanel extends DefaultFormPanel {

    private InstrumentoAvaliacao selectedInstrumentoAvaliacao;

    public InstrumentoAvaliacaoPanel(Component iframe) {
        super(iframe);
        try {
            super.setName("panel.instrumento");
            super.setTitlePanel("Dados do Instrumento de Avaliação");
            
            super.setController(ControllerFactory.createInstrumentoAvaliacaoController());
            super.enableTablePanel();

            super.setFieldsPanel(new InstrumentoAvaliacaoFields());

            super.showPanelInCard(CARD_LIST);
        } catch (Exception ex) {
            Logger.getLogger(InstrumentoAvaliacaoPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void resizeTableColumns() {
        javax.swing.JTable table = getTable();
        javax.swing.table.TableColumnModel tcm = table.getColumnModel();
        tcm.getColumn(0).setCellRenderer(new MetodoCellRenderer());
    }

    @Override
    public void onSearchButton(ActionEvent e) {

    }

    @Override
    public void addFiltersFields() {

    }

    @Override
    public void reloadTableData() {
        setTableModel(new InstrumentoAvaliacaoTableModel(getController().listar()));
        resizeTableColumns();
    }

    @Override
    public void createSelectButton() {
        JButton button = createButton("selection-button-50px.png", "Selecionar", 1);
        button.addActionListener((ActionEvent e) -> {
            JTable t = getTable();
            if (t.getRowCount() > 0) {
                int row = t.getSelectedRow();
                InstrumentoAvaliacaoTableModel model = (InstrumentoAvaliacaoTableModel) t.getModel();
                selectedInstrumentoAvaliacao = (InstrumentoAvaliacao) model.getRow(row);
                JDialog dialog = (JDialog) getFrame();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(getParent(),
                        "Não existem dados a serem selecionados.\nFavor, cadastrar um dado primeiro.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        addButtonToToolBar(button, true);
    }

    @Override
    public Object getSelectedObject() {
        return selectedInstrumentoAvaliacao;
    }

    private class InstrumentoAvaliacaoFields extends DefaultFieldsPanel {

        private GenJTextField txtId;
        private GenJTextField txtNome;

        public InstrumentoAvaliacaoFields() {
            super();
            initComponents();
        }

        private void initComponents() {
            SpringLayout layout = new SpringLayout();

            JPanel fieldsPanel = new JPanel(layout);
            GenJLabel lblId = new GenJLabel("Código:", JLabel.TRAILING);
            txtId = new GenJTextField(10, false);

            GenJLabel lblNome = new GenJLabel("Nome:", JLabel.TRAILING);
            txtNome = new GenJTextField(30, true);

            fieldsPanel.add(lblId);
            fieldsPanel.add(txtId);

            fieldsPanel.add(lblNome);
            fieldsPanel.add(txtNome);
            SpringUtilities.makeCompactGrid(fieldsPanel, 2, 2, 6, 6, 6, 6);
            add(fieldsPanel);
        }

        @Override
        public HashMap<String, Object> getFieldValues() {
            HashMap<String, Object> map = new HashMap<>();

            map.put("id", ("".equals(txtId.getText()) ? null
                    : Integer.parseInt(txtId.getText())));
            map.put("nome", txtNome.getText());
            return map;
        }

        private void setFieldValues(Integer codigo, String nome) {
            txtId.setText(codigo.toString());
            txtNome.setText(nome);
        }

        @Override
        public void setFieldValues(HashMap<String, Object> mapValues) {
            Integer codigo = (Integer) mapValues.get("id");
            setFieldValues(codigo, (String) mapValues.get("nome"));
        }

        @Override
        public void setFieldValues(Object object) {
            if (object instanceof InstrumentoAvaliacao) {
                InstrumentoAvaliacao instrumento = (InstrumentoAvaliacao) object;
                setFieldValues(instrumento.getId(), instrumento.getNome());
            }
        }

        @Override
        public boolean isValidated() {
            return !"".equals(txtNome.getText());
        }

        @Override
        public void clearFields() {
            txtId.setText("");
            txtNome.setText("");
        }

        @Override
        public void enableFields(boolean active) {
            txtId.setEnabled(false);
            txtNome.setEnabled(active);
        }

        @Override
        public void initFocus() {
            txtNome.requestFocusInWindow();
        }

    }
}
