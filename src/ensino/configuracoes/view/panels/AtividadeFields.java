/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels;

import ensino.components.GenJComboBox;
import ensino.components.GenJFormattedTextField;
import ensino.components.GenJLabel;
import ensino.components.GenJTextField;
import ensino.configuracoes.controller.LegendaController;
import ensino.configuracoes.model.Atividade;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.Legenda;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.util.types.Periodo;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class AtividadeFields extends DefaultFieldsPanel {

    private Calendario calendario;
    private GenJTextField txtId;
    private GenJFormattedTextField txtDe;
    private GenJFormattedTextField txtAte;
    private GenJTextField txtDescricao;
    private GenJComboBox comboLegenda;

    public AtividadeFields() {
        super();
        initComponents();
    }

    private void initComponents() {
        try {
            JPanel fieldsPanel = new JPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            GenJLabel lblId = new GenJLabel("Código:", JLabel.TRAILING);
            GridLayoutHelper.setRight(c, 0, 0);
            fieldsPanel.add(lblId, c);
            txtId = new GenJTextField(10);
            txtId.setEnabled(false);
            lblId.setLabelFor(txtId);
            GridLayoutHelper.set(c, 1, 0);
            fieldsPanel.add(txtId, c);

            GenJLabel lblDe = new GenJLabel("De: ", JLabel.TRAILING);
            GridLayoutHelper.setRight(c, 0, 1);
            fieldsPanel.add(lblDe, c);
            txtDe = createFormattedField("##/##/####", 1);
            lblDe.setLabelFor(txtDe);
            GridLayoutHelper.set(c, 1, 1);
            c.fill = GridBagConstraints.HORIZONTAL;
            fieldsPanel.add(txtDe, c);

            GenJLabel lblAte = new GenJLabel("Até: ", JLabel.TRAILING);
            GridLayoutHelper.setRight(c, 2, 1);
            fieldsPanel.add(lblAte, c);
            txtAte = createFormattedField("##/##/####", 1);
            lblAte.setLabelFor(txtAte);
            GridLayoutHelper.set(c, 3, 1);
            c.fill = GridBagConstraints.HORIZONTAL;
            fieldsPanel.add(txtAte, c);

            GenJLabel lblDescricao = new GenJLabel("Descrição: ", JLabel.TRAILING);
            GridLayoutHelper.setRight(c, 0, 2);
            fieldsPanel.add(lblDescricao, c);
            txtDescricao = new GenJTextField(25);
            lblDescricao.setLabelFor(txtDescricao);
            GridLayoutHelper.set(c, 1, 2);
            c.gridwidth = 3;
            fieldsPanel.add(txtDescricao, c);

            GenJLabel lblLegenda = new GenJLabel("Legenda: ", JLabel.TRAILING);
            GridLayoutHelper.setRight(c, 0, 3);
            fieldsPanel.add(lblLegenda, c);
            LegendaController legendaCol = new LegendaController();
            comboLegenda = new GenJComboBox(legendaCol.listar().toArray());
            lblLegenda.setLabelFor(comboLegenda);
            GridLayoutHelper.set(c, 1, 3);
            c.gridwidth = 3;
            fieldsPanel.add(comboLegenda, c);

            add(fieldsPanel);
        } catch (ParseException | IOException ex) {
            Logger.getLogger(AtividadePanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(AtividadeFields.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Calendario getCalendario() {
        return calendario;
    }

    public void setCalendario(Calendario calendario) {
        this.calendario = calendario;
    }

    /**
     * Cria um campo formatado
     *
     * @param format Formato a ser mostrado
     * @param type Type = 0, para números e Type = 1 para outros.
     * @return
     * @throws ParseException
     */
    private GenJFormattedTextField createFormattedField(String format, Integer type) throws ParseException {
        DefaultFormatterFactory formatter = null;
        if (type == 0) {
            formatter = new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0.00")));
        } else {
            formatter = new DefaultFormatterFactory(new MaskFormatter(format));
        }
        GenJFormattedTextField textField = new GenJFormattedTextField();
        textField.setFormatterFactory(formatter);
        return textField;
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        try {
            HashMap<String, Object> map = new HashMap<>();

            map.put("id", ("".equals(txtId.getText()) ? null
                    : Integer.parseInt(txtId.getText())));
            map.put("periodo", new Periodo(txtDe.getText(), txtAte.getText()));
            map.put("descricao", txtDescricao.getText());
            map.put("legenda", comboLegenda.getSelectedItem());
            map.put("calendario", calendario);
            return map;
        } catch (ParseException ex) {
            Logger.getLogger(AtividadePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private void setFieldValues(Integer id, Periodo periodo,
            String descricao, Legenda legenda) {
        txtId.setText(id.toString());
        txtDe.setText(periodo.getDeText());
        txtAte.setText(periodo.getAteText());
        txtDescricao.setText(descricao);
        comboLegenda.setSelectedItem(legenda);
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {
        Integer id = (Integer) mapValues.get("id");
        setFieldValues(id,
                (Periodo) mapValues.get("periodo"),
                (String) mapValues.get("descricao"),
                (Legenda) mapValues.get("legenda"));
    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof Atividade) {
            Atividade at = (Atividade) object;
            setFieldValues(at.getId(), at.getPeriodo(),
                    at.getDescricao(), at.getLegenda());
        }
    }

    @Override
    public boolean isValidated() {
        try {
            txtDe.commitEdit();
            txtAte.commitEdit();
            return txtDe.getValue() != null
                    && txtAte.getValue() != null
                    && (!"".equals(txtDescricao.getText()))
                    && comboLegenda.getSelectedItem() != null;
        } catch (ParseException ex) {
            Logger.getLogger(AtividadePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public void clearFields() {
        txtId.setText("");
        txtDe.setText("");
        txtAte.setText("");
        txtDescricao.setText("");
        comboLegenda.setSelectedItem(null);
    }

    @Override
    public void enableFields(boolean active) {
        txtId.setEnabled(false);
        txtDe.setEnabled(active);
        txtAte.setEnabled(active);
        txtDescricao.setEnabled(active);
        comboLegenda.setEnabled(active);
    }

    @Override
    public void initFocus() {
        txtDe.requestFocusInWindow();
    }

}
