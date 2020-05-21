/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.docente;

import ensino.components.GenJLabel;
import ensino.components.GenJTextField;
import ensino.configuracoes.model.Docente;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author santos
 */
public class DocenteFieldsPanel extends DefaultFieldsPanel {

    private GenJTextField txtId;
    private GenJTextField txtNome;

    public DocenteFieldsPanel() {
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
        txtId.setFocusable(true);
        lblId.setLabelFor(txtId);
        GridLayoutHelper.set(c, 1, 0);
        fieldsPanel.add(txtId, c);

        GenJLabel lblNome = new GenJLabel("Nome:", JLabel.TRAILING);
        GridLayoutHelper.setRight(c, 0, 1);
        fieldsPanel.add(lblNome, c);
        txtNome = new GenJTextField(30, true);
        lblNome.setLabelFor(txtNome);
        GridLayoutHelper.set(c, 1, 1);
        fieldsPanel.add(txtNome, c);

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
        if (object instanceof Docente) {
            Docente campus = (Docente) object;
            setFieldValues(campus.getId(), campus.getNome());
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
