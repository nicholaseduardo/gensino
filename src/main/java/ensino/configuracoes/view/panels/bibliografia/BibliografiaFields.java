/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.bibliografia;

import ensino.components.GenJLabel;
import ensino.components.GenJTextArea;
import ensino.components.GenJTextField;
import ensino.configuracoes.model.Bibliografia;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author santos
 */
public class BibliografiaFields extends DefaultFieldsPanel {

    private GenJTextField txtId;
    private GenJTextField txtTitulo;
    private GenJTextField txtAutor;
    private GenJTextArea txtReferencia;

    public BibliografiaFields() {
        super();
        initComponents();
    }

    private void initComponents() {
        JPanel fieldsPanel = createPanel(new GridBagLayout());
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

        map.put("id", "".equals(txtId.getText()) ? null
                : Integer.parseInt(txtId.getText()));
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
        String msg = "O campo [%s] não foi informado.",
                campo = "";
        if ("".equals(txtTitulo.getText())) {
            campo = "TITULO";
            txtTitulo.requestFocusInWindow();
        } else if ("".equals(txtAutor.getText())) {
            campo = "AUTOR";
            txtAutor.requestFocusInWindow();
        } else if ("".equals(txtReferencia.getText())) {
            campo = "REFERÊNCIA";
            txtReferencia.requestFocusInWindow();
        } else {
            return true;
        }
        showInformationMessage(String.format(msg, campo));
        return false;
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
