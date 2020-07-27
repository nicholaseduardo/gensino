/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.turma;

import ensino.components.GenJButton;
import ensino.components.GenJComboBox;
import ensino.components.GenJLabel;
import ensino.components.GenJSpinner;
import ensino.components.GenJTextField;
import ensino.configuracoes.model.Estudante;
import ensino.configuracoes.model.EstudanteFactory;
import ensino.configuracoes.model.Turma;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.DateHelper;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.ControllerFactory;
import ensino.util.types.AcoesBotoes;
import ensino.util.types.SituacaoEstudante;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

/**
 *
 * @author nicho
 */
public class TurmaFieldsPanelEstudante extends DefaultFieldsPanel {

    private Turma turma;

    private GenJTextField txtId;
    private GenJTextField txtNome;
    private GenJTextField txtRegistro;
    private GenJComboBox comboSituacao;
    private GenJSpinner spinData;

    private Component frame;

    public TurmaFieldsPanelEstudante(Turma turma) {
        super("Dados dos estudantes");
        this.turma = turma;
        initComponents();
    }

    public TurmaFieldsPanelEstudante() {
        this(null);
    }

    public void setFrame(Component frame) {
        this.frame = frame;
    }

    private void initComponents() {
        setName("estudante.cadastro");
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEtchedBorder());

        JPanel panel = createPanel(new BorderLayout());
        panel.add(createEstudanteFieldsPanel(), BorderLayout.PAGE_START);
        add(panel, BorderLayout.CENTER);
    }

    public JPanel createEstudanteFieldsPanel() {

        GenJLabel lblId = new GenJLabel("Código: ", JLabel.TRAILING);
        txtId = new GenJTextField(5, false);
        txtId.setEnabled(false);
        lblId.setLabelFor(txtId);

        GenJLabel lblRegistro = new GenJLabel("Número R.A.: ", JLabel.TRAILING);
        txtRegistro = new GenJTextField(10, false);
        lblRegistro.setLabelFor(txtRegistro);

        GenJLabel lblNome = new GenJLabel("Nome: ", JLabel.TRAILING);
        txtNome = new GenJTextField(30, false);
        lblNome.setLabelFor(txtNome);

        GenJLabel lblIngresso = new GenJLabel("Data de Ingresso: ", JLabel.TRAILING);
        Calendar cal = Calendar.getInstance();
        spinData = new GenJSpinner(new SpinnerDateModel(cal.getTime(),
                null, null, Calendar.DATE));
        spinData.setEditor(new JSpinner.DateEditor(spinData, "dd/MM/yyyy"));
        lblIngresso.setLabelFor(spinData);

        GenJLabel lblSituacao = new GenJLabel("Situação: ", JLabel.TRAILING);
        comboSituacao = new GenJComboBox(SituacaoEstudante.values());
        comboSituacao.setSelectedItem(SituacaoEstudante.EM_CURSO);
        lblSituacao.setLabelFor(comboSituacao);

        JPanel panelFields = createPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        int col = 0, row = 0;
        GridLayoutHelper.setRight(c, col++, row);
        panelFields.add(lblId, c);
        GridLayoutHelper.set(c, col++, row);
        panelFields.add(txtId, c);

        GridLayoutHelper.setRight(c, col++, row);
        panelFields.add(lblRegistro, c);
        GridLayoutHelper.set(c, col++, row++);
        c.fill = GridBagConstraints.HORIZONTAL;
        panelFields.add(txtRegistro, c);

        col = 0;
        GridLayoutHelper.setRight(c, col++, row);
        panelFields.add(lblNome, c);
        GridLayoutHelper.set(c, col++, row++, 3, 1, GridBagConstraints.LINE_START);
        panelFields.add(txtNome, c);

        col = 0;
        GridLayoutHelper.setRight(c, col++, row);
        panelFields.add(lblIngresso, c);
        GridLayoutHelper.set(c, col++, row);
        panelFields.add(spinData, c);

        GridLayoutHelper.setRight(c, col++, row);
        panelFields.add(lblSituacao, c);
        GridLayoutHelper.set(c, col++, row++);
        panelFields.add(comboSituacao, c);

        JPanel panel = createPanel(new BorderLayout());
        panel.add(panelFields, BorderLayout.CENTER);
        return panel;
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = new HashMap();
        map.put("id", txtId.getText());
        map.put("nome", txtNome.getText());
        map.put("registro", txtRegistro.getText());
        map.put("ingresso", DateHelper.dateToString((Date) spinData.getValue(), "dd/MM/yyyy"));
        map.put("turma", turma);
        map.put("situacao", comboSituacao.getSelectedItem());
        return map;
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {

    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof Estudante) {
            Estudante o = (Estudante) object;
            turma = o.getTurma();
            txtId.setText(o.getId().getId().toString());
            txtNome.setText(o.getNome());
            txtRegistro.setText(o.getRegistro());
            comboSituacao.setSelectedItem(o.getSituacaoEstudante());
            if (o.getIngresso() != null) {
                spinData.setValue(o.getIngresso());
            }
        }
    }

    @Override
    public boolean isValidated() {
        String msg = "O campo [%s] não foi informado!";
        String campo = "";
        if ("".equals(txtNome.getText())) {
            campo = "NOME";
            txtNome.requestFocusInWindow();
        } else if (spinData.getValue() == null) {
            campo = "DATA DE INGRESSO";
            spinData.requestFocusInWindow();
        } else if (comboSituacao.getSelectedItem() == null) {
            campo = "SITUAÇÃO";
            spinData.requestFocusInWindow();
        } else {
            return true;
        }
        showInformationMessage(String.format(msg, campo));
        return false;
    }

    @Override
    public void clearFields() {
        txtId.setText("");
        txtNome.setText("");
        txtRegistro.setText("");
        comboSituacao.setSelectedItem(SituacaoEstudante.EM_CURSO);
        Calendar cal = Calendar.getInstance();
        spinData.setValue(cal.getTime());
    }

    @Override
    public void enableFields(boolean active) {
        txtId.setEnabled(false);
        txtNome.setEnabled(active);
        txtRegistro.setEnabled(active);
        comboSituacao.setEnabled(active);
        spinData.setEnabled(active);

        Boolean status = "".equals(txtId.getText());
    }

    @Override
    public void initFocus() {
        txtNome.requestFocusInWindow();
    }

}
