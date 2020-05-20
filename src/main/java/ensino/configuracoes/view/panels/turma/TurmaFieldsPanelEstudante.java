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
import ensino.configuracoes.model.EstudanteId;
import ensino.configuracoes.model.Turma;
import ensino.configuracoes.view.models.EstudanteTableModel;
import ensino.configuracoes.view.renderer.EstudanteCellRenderer;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.reports.ChartsFactory;
import ensino.util.types.AcoesBotoes;
import ensino.util.types.SituacaoEstudante;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;

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

    private GenJButton btAdicionar;
    private GenJButton btRemover;
    private GenJButton btNovo;
    private GenJButton btImportar;
    private GenJButton btAtualizar;

    private JTable estudanteTable;
    private EstudanteTableModel estudanteTableModel;

    public TurmaFieldsPanelEstudante() {
        super("Dados dos estudantes");
        initComponents();
    }

    private void initComponents() {
        setName("estudante.cadastro");
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEtchedBorder());

        backColor = ChartsFactory.lightBlue;
        foreColor = ChartsFactory.ardoziaBlueColor;
        setBackground(backColor);

        add(createEstudanteFieldsPanel(), BorderLayout.PAGE_START);
        add(createEstudanteListaPanel(), BorderLayout.CENTER);
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
        GridLayoutHelper.set(c, col++, row);
        c.fill = GridBagConstraints.HORIZONTAL;
        panelFields.add(txtRegistro, c);

        GridLayoutHelper.setRight(c, col++, row);
        panelFields.add(lblSituacao, c);
        GridLayoutHelper.set(c, col, row++);
        panelFields.add(comboSituacao, c);

        col = 0;
        GridLayoutHelper.setRight(c, col++, row);
        panelFields.add(lblNome, c);
        GridLayoutHelper.set(c, col++, row, 3, 1, GridBagConstraints.LINE_START);
        panelFields.add(txtNome, c);
        GridLayoutHelper.setRight(c, col+=2, row);
        panelFields.add(lblIngresso, c);
        GridLayoutHelper.set(c, ++col, row);
        panelFields.add(spinData, c);

        btNovo = createButton(new ActionHandler(AcoesBotoes.NEW), backColor, foreColor);
        btAdicionar = createButton(new ActionHandler(AcoesBotoes.ADD), backColor, foreColor);
        btRemover = createButton(new ActionHandler(AcoesBotoes.DELETE), backColor, foreColor);
        btImportar = createButton(new ActionHandler(AcoesBotoes.IMPORT), backColor, foreColor);
        btAtualizar = createButton(new ActionHandler(AcoesBotoes.EDIT), backColor, foreColor);

        JPanel panelButton = createPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButton.add(btNovo);
        panelButton.add(btAdicionar);
        panelButton.add(btAtualizar);
        panelButton.add(btRemover);
        panelButton.add(btImportar);
        
        JPanel panel = createPanel(new BorderLayout());
        panel.add(panelFields, BorderLayout.CENTER);
        panel.add(panelButton, BorderLayout.PAGE_END);

        return panel;
    }

    private JScrollPane createEstudanteListaPanel() {

        estudanteTable = new JTable(estudanteTableModel);
        ListSelectionModel cellSelectionModel = estudanteTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cellSelectionModel.addListSelectionListener(new EstudanteListSelectionListener());

        setData(new ArrayList());
        JScrollPane scrollEstudante = new JScrollPane(estudanteTable);
        scrollEstudante.setBorder(createTitleBorder("Lista de estudantes"));
        return scrollEstudante;
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = new HashMap();
        map.put("estudantes", estudanteTableModel.getData());
        return map;
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {
        List<Estudante> estudantes = (List<Estudante>) mapValues.get("estudantes");
        setData(estudantes);
    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof Turma) {
            turma = (Turma) object;
            setData(turma.getEstudantes());
            clear();
        } else if (object instanceof Estudante) {
            Estudante o = (Estudante) object;
            txtId.setText(o.getId().getId().toString());
            txtNome.setText(o.getNome());
            txtRegistro.setText(o.getRegistro());
            comboSituacao.setSelectedItem(o.getSituacaoEstudante());
            if (o.getIngresso() != null) {
                spinData.setValue(o.getIngresso());
            }
        }
    }

    private void setData(List<Estudante> data) {
        data.sort(Comparator.comparing(Estudante::getNome));

        estudanteTableModel = new EstudanteTableModel(data);
        estudanteTable.setModel(estudanteTableModel);
        estudanteTable.repaint();

        TableColumnModel tcm = estudanteTable.getColumnModel();
        tcm.getColumn(0).setCellRenderer(new EstudanteCellRenderer());
    }

    @Override
    public boolean isValidated() {
        String msg = "O campo [%s] não foi informado!";
        String campo = "";
        if ("".equals(txtNome.getText())) {
            campo = "NOME";
            txtNome.requestFocusInWindow();
        }
        if (spinData.getValue() == null) {
            campo = "DATA DE INGRESSO";
            spinData.requestFocusInWindow();
        } else {
            return true;
        }
        showInformationMessage(String.format(msg, campo));
        return false;
    }

    @Override
    public void clearFields() {
        clearLocalFields();
        setData(new ArrayList());
    }

    private void clearLocalFields() {
        txtId.setText("");
        txtNome.setText("");
        txtRegistro.setText("");
        comboSituacao.setSelectedItem(null);
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
        estudanteTable.setEnabled(active);

        enableLocalButtons(active);
        clearLocalFields();
    }

    private void enableLocalButtons(Boolean active) {
        Boolean status = "".equals(txtId.getText());

        btAdicionar.setEnabled(active && status);
        btNovo.setEnabled(active && !status);
        btImportar.setEnabled(active && status);
        btAtualizar.setEnabled(active && !status);
        btRemover.setEnabled(active && !status);
    }

    @Override
    public void initFocus() {
        txtNome.requestFocusInWindow();
    }

    private void clear() {
        clearLocalFields();
        enableLocalButtons(Boolean.TRUE);
        initFocus();
    }

    private Estudante createEstudanteFromFields() {
        Integer id = null;
        String sId = txtId.getText();
        if (sId.matches("\\d+")) {
            id = Integer.parseInt(sId);
        }
        Estudante estudante = EstudanteFactory.getInstance()
                .createObject(new EstudanteId(id, turma),
                        txtNome.getText(),
                        txtRegistro.getText(),
                        comboSituacao.getSelectedItem(),
                        spinData.getValue());
        return estudante;
    }

    @Override
    public void onImportAction(ActionEvent e) {
        TurmaFieldsPanelEstudanteImportar dialog = new TurmaFieldsPanelEstudanteImportar();
        List<HashMap<String, Object>> dadosImportados = dialog.getData();
        if (!dadosImportados.isEmpty()) {
            setData(new ArrayList());
            int id = 1;
            for (int i = 0; i < dadosImportados.size(); i++) {
                HashMap<String, Object> mapValue = dadosImportados.get(i);

                Estudante estudante = EstudanteFactory.getInstance()
                        .getObject(mapValue);
                /**
                 * Na importação não vem a identificação da turma, logo ela será
                 * adicionada aqui.
                 */
                estudante.getId().setTurma(turma);

                estudanteTableModel.addRow(estudante);
            }
            clear();
        }
    }

    @Override
    public void onNewAction(ActionEvent e, Object o) {
        clear();
    }

    @Override
    public void onAddAction(ActionEvent e, Object o) {
        if (isValidated()) {
            Integer id = 1;

            if (!estudanteTableModel.isEmpty()) {
                /**
                 * Procedimento realizado para gerar a chave única de cada
                 * estudante para cada turma
                 */
                Estudante eTemp = estudanteTableModel.getMax(Comparator.comparing(est -> est.getId().getId()));
                id = eTemp.getId().getId() + 1;
            }
            /**
             * atribui o valor do ID ao campo para reaproveitar o método de
             * criação do objeto Estudante
             */
            txtId.setText(id.toString());
            estudanteTableModel.addRow(createEstudanteFromFields());
            clear();
        }
    }

    @Override
    public void onEditAction(ActionEvent e, Object o) {
        if (isValidated()) {
            int selectedRow = estudanteTable.getSelectedRow();
            estudanteTableModel.updateRow(selectedRow, createEstudanteFromFields());
            clear();
        }
    }

    @Override
    public void onDelAction(ActionEvent e, Object o) {
        int selectedRow = estudanteTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(getParent(),
                    "Você não selecionou o Estudante que será removida.\n"
                    + "Favor, clique sobre um Estudante!",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        estudanteTableModel.removeRow(selectedRow);
        estudanteTable.repaint();
        clear();
    }

    private class EstudanteListSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            int selectedRow = estudanteTable.getSelectedRow();
            if (selectedRow >= 0) {
                Estudante o = (Estudante) estudanteTableModel.getRow(selectedRow);
                setFieldValues(o);
                enableLocalButtons(Boolean.TRUE);
                initFocus();
            }
        }

    }

}
