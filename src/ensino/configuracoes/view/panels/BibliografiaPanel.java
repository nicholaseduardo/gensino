/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels;

import ensino.components.GenJLabel;
import ensino.components.GenJTextField;
import ensino.configuracoes.controller.BibliografiaController;
import ensino.configuracoes.model.Bibliografia;
import ensino.configuracoes.view.models.BibliografiaTableModel;
import ensino.defaults.DefaultFieldsPanel;
import ensino.defaults.DefaultFormPanel;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import ensino.components.GenJTextArea;
import ensino.configuracoes.view.renderer.BibliografiaCellRenderer;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.ControllerFactory;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class BibliografiaPanel extends DefaultFormPanel {

    private GenJTextField txtTitulo;
    private GenJTextField txtAutor;
    private JButton btSearch;
    
    private Bibliografia selectedBibliografia;
    
    public BibliografiaPanel(Component frame) {
        super(frame);
        try {
            setName("panel.bibliografia");
            setTitlePanel("Dados de Bibliografia");
            
            setController(ControllerFactory.createBibliografiaController());

            enableTablePanel();

            setFieldsPanel(new BibliografiaFields());
            showPanelInCard(CARD_LIST);
        } catch (IOException | ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(BibliografiaPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Bibliografia getSelectedObject() {
        return selectedBibliografia;
    }
    
    /**
     * Cria um botão para selecionar um curso na tabela e fecha 
     * a janela da bibliografia
     */
    public void createSelectButton() {
        JButton button = createButton("selection-button-50px.png", "Selecionar", 1);
        button.addActionListener((ActionEvent e) -> {
            JTable t = getTable();
            if (t.getRowCount() > 0) {
                int row = t.getSelectedRow();
                BibliografiaTableModel model = (BibliografiaTableModel) t.getModel();
                selectedBibliografia = (Bibliografia) model.getRow(row);
                JDialog dialog = (JDialog)getFrame();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(getParent(), 
                        "Não existem dados a serem selecionados.\nFavor, cadastrar um dado primeiro.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        addButtonToToolBar(button, true);
    }

    private void resizeTableColumns() {
        javax.swing.JTable table = getTable();
        javax.swing.table.TableColumnModel tcm = table.getColumnModel();
        TableColumn tc = tcm.getColumn(0);
        tc.setMinWidth(50);
        tc.setCellRenderer(new BibliografiaCellRenderer());
    }

    @Override
    public void reloadTableData() {
        BibliografiaTableModel model;
        BibliografiaController col = (BibliografiaController) getController();
        String sAutor = txtAutor.getText();
        String sTitulo = txtTitulo.getText();
        List<Bibliografia> list;
        if (!"".equals(sTitulo)) {
            list = col.listarPorTitulo(sTitulo);
        } else if (!"".equals(sAutor)) {
            list = col.listarPorAutor(sAutor);
        } else {
            list = col.listar();
        }
        model = new BibliografiaTableModel(list);
        setTableModel(model);
        resizeTableColumns();
    }

    @Override
    public void onSearchButton(ActionEvent e) {
        reloadTableData();
    }

    @Override
    public void addFiltersFields() {
        JPanel panel = getFilterPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        GenJLabel lblTitulo = new GenJLabel("Título: ", JLabel.TRAILING);
        GridLayoutHelper.setRight(c, 0, 0);
        panel.add(lblTitulo, c);
        
        txtTitulo = new GenJTextField(30, true);
        lblTitulo.setLabelFor(txtTitulo);
        GridLayoutHelper.set(c, 1, 0);
        panel.add(txtTitulo, c);
        
        GenJLabel lblAutor = new GenJLabel("Autor: ", JLabel.TRAILING);
        GridLayoutHelper.setRight(c, 0, 1);
        panel.add(lblAutor, c);
        
        txtAutor = new GenJTextField(30, true);
        lblAutor.setLabelFor(txtAutor);
        GridLayoutHelper.set(c, 1, 1);
        panel.add(txtAutor, c);
        
        btSearch = createButton("search", "Buscar", 0);
        GridLayoutHelper.set(c, 0, 2, 2, 1, GridBagConstraints.LINE_END);
        panel.add(btSearch, c);
    }

    private class BibliografiaFields extends DefaultFieldsPanel {

        private GenJTextField txtId;
        private GenJTextField txtTitulo;
        private GenJTextField txtAutor;
        private GenJTextArea txtReferencia;

        public BibliografiaFields() {
            super();
            initComponents();
        }

        private void initComponents() {
            JPanel fieldsPanel = new JPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            GenJLabel lblId = new GenJLabel("Código:", JLabel.TRAILING);
            GridLayoutHelper.setRight(c, 0, 0);
            fieldsPanel.add(lblId, c);
            txtId = new GenJTextField(10, false);
            txtId.setEnabled(false);
            lblId.setLabelFor(txtId);
            GridLayoutHelper.set(c, 1, 0);
            fieldsPanel.add(txtId, c);

            GenJLabel lblTitulo = new GenJLabel("Título:", JLabel.TRAILING);
            GridLayoutHelper.setRight(c, 0, 1);
            fieldsPanel.add(lblTitulo, c);
            txtTitulo = new GenJTextField(30, true);
            txtTitulo.setFocusable(true);
            lblTitulo.setLabelFor(txtTitulo);
            GridLayoutHelper.set(c, 1, 1);
            fieldsPanel.add(txtTitulo, c);

            GenJLabel lblAutor = new GenJLabel("Autor:", JLabel.TRAILING);
            GridLayoutHelper.setRight(c, 0, 2);
            fieldsPanel.add(lblAutor, c);
            txtAutor = new GenJTextField(30, true);
            txtAutor.setFocusable(true);
            lblAutor.setLabelFor(txtAutor);
            GridLayoutHelper.set(c, 1, 2);
            fieldsPanel.add(txtAutor, c);

            GenJLabel lblReferencia = new GenJLabel("Descrição da referência (Padrão ABNT)", JLabel.TRAILING);
            GridLayoutHelper.set(c, 0, 3, 2, 1, GridBagConstraints.LINE_END);
            c.anchor = GridBagConstraints.CENTER;
            fieldsPanel.add(lblReferencia, c);
            txtReferencia = new GenJTextArea(5, 50);
            lblReferencia.setLabelFor(txtReferencia);
            GridLayoutHelper.set(c, 0, 4, 2, 1, GridBagConstraints.LINE_END);
            c.fill = GridBagConstraints.HORIZONTAL;
            JScrollPane referenciaScroll = new JScrollPane(txtReferencia);
            referenciaScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            fieldsPanel.add(referenciaScroll, c);

            add(fieldsPanel);
        }

        @Override
        public HashMap<String, Object> getFieldValues() {
            HashMap<String, Object> map = new HashMap<>();

            map.put("id", ("".equals(txtId.getText()) ? null
                    : Integer.parseInt(txtId.getText())));
            map.put("titulo", txtTitulo.getText());
            map.put("autor", txtAutor.getText());
            map.put("referencia", txtReferencia.getText());
            return map;
        }

        private void setFieldValues(Integer codigo, String titulo, String autor, String referencia) {
            txtId.setText(codigo.toString());
            txtTitulo.setText(titulo);
            txtAutor.setText(autor);
            txtReferencia.setText(referencia);
        }

        @Override
        public void setFieldValues(HashMap<String, Object> mapValues) {
            Integer codigo = (Integer) mapValues.get("id");
            setFieldValues(codigo, (String) mapValues.get("titulo"),
                    (String) mapValues.get("autor"),
                    (String) mapValues.get("referencia"));
        }

        @Override
        public void setFieldValues(Object object) {
            if (object instanceof Bibliografia) {
                Bibliografia bibliografia = (Bibliografia) object;
                setFieldValues(bibliografia.getId(), bibliografia.getTitulo(),
                        bibliografia.getAutor(), bibliografia.getReferencia());
            }
        }

        @Override
        public boolean isValidated() {
            return !"".equals(txtTitulo.getText())
                    && !"".equals(txtAutor.getText())
                    && !"".equals(txtReferencia.getText());
        }

        @Override
        public void clearFields() {
            txtId.setText("");
            txtTitulo.setText("");
            txtAutor.setText("");
            txtReferencia.setText("");
        }

        @Override
        public void enableFields(boolean active) {
            txtId.setEnabled(false);
            txtTitulo.setEnabled(active);
            txtAutor.setEnabled(active);
            txtReferencia.setEnabled(active);
        }

        @Override
        public void initFocus() {
            txtTitulo.requestFocusInWindow();
        }

    }

}
