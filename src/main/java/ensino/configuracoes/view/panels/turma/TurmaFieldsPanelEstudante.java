/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.turma;

import ensino.components.GenJButton;
import ensino.components.GenJComboBox;
import ensino.components.GenJLabel;
import ensino.components.GenJPanel;
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
import ensino.patterns.factory.ControllerFactory;
import ensino.reports.ChartsFactory;
import ensino.util.types.AcoesBotoes;
import ensino.util.types.SituacaoEstudante;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.table.TableColumn;
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

    private GenJButton btSalvar;
    private GenJButton btNovo;
    private GenJButton btImportar;

    private JTable estudanteTable;
    private EstudanteTableModel estudanteTableModel;

    private Component frame;

    public TurmaFieldsPanelEstudante(Turma turma) {
        super("Dados dos estudantes");
        this.turma = turma;
        initComponents();
    }

    public void setFrame(Component frame) {
        this.frame = frame;
    }

    private void initComponents() {
        setName("estudante.cadastro");
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEtchedBorder());

        backColor = ChartsFactory.lightBlue;
        foreColor = ChartsFactory.ardoziaBlueColor;
        setBackground(backColor);

        URL urlTurma = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "classroom-50px.png"));

        GenJLabel lblTitulo = new GenJLabel("Estudantes "
                + (turma != null ? " - " + turma.getNome() : ""), new ImageIcon(urlTurma), JLabel.CENTER);
        lblTitulo.setVerticalTextPosition(JLabel.BOTTOM);
        lblTitulo.setHorizontalTextPosition(JLabel.CENTER);
        lblTitulo.resetFontSize(20);
        lblTitulo.setForeground(foreColor);
        lblTitulo.toBold();
        add(lblTitulo, BorderLayout.PAGE_START);

        GenJButton btClose = createButton(new ActionHandler(AcoesBotoes.CLOSE), backColor, foreColor);

        JPanel panelButton = createPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButton.add(btClose);
        add(panelButton, BorderLayout.PAGE_END);

        JPanel panel = createPanel(new BorderLayout());
        panel.add(createEstudanteFieldsPanel(), BorderLayout.PAGE_START);
        panel.add(createEstudanteListaPanel(), BorderLayout.CENTER);
        add(panel, BorderLayout.CENTER);

        enableFields(true);
        initFocus();
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
        GridLayoutHelper.setRight(c, col += 2, row);
        panelFields.add(lblIngresso, c);
        GridLayoutHelper.set(c, ++col, row);
        panelFields.add(spinData, c);

        btNovo = createButton(new ActionHandler(AcoesBotoes.NEW), backColor, foreColor);
        btSalvar = createButton(new ActionHandler(AcoesBotoes.SAVE), backColor, foreColor);
        btImportar = createButton(new ActionHandler(AcoesBotoes.IMPORT), backColor, foreColor);

        JPanel panelButton = createPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButton.add(btNovo);
        panelButton.add(btSalvar);
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

        refreshTable();
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

    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof Turma) {
            turma = (Turma) object;
            refreshTable();
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
        initFocus();
        enableLocalButtons(Boolean.TRUE);
    }

    private void refreshTable() {
        try {
            List<Estudante> lestudantes = new ArrayList();
            if (turma != null) {
                turma = ControllerFactory.createTurmaController().buscarPorId(turma.getId());
                lestudantes = turma.getEstudantes();
            }
            estudanteTableModel = new EstudanteTableModel(lestudantes);
            estudanteTableModel.activateButtons();
            estudanteTable.setModel(estudanteTableModel);
            
            TableColumnModel tcm = estudanteTable.getColumnModel();
            TableColumn col0 = tcm.getColumn(0);
            col0.setCellRenderer(new EstudanteCellRenderer());

            EnumSet enumSet = EnumSet.of(AcoesBotoes.DELETE, AcoesBotoes.EDIT);

            TableColumn col1 = tcm.getColumn(1);
            col1.setCellRenderer(new ButtonsRenderer(null, enumSet));
            col1.setCellEditor(new GenJPanel.ButtonsEditor(estudanteTable, null, enumSet));

            estudanteTable.repaint();
        } catch (Exception ex) {
            showErrorMessage(ex);
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
        clearLocalFields();
    }

    private void clearLocalFields() {
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
        estudanteTable.setEnabled(active);

        enableLocalButtons(active);
        clearLocalFields();
    }

    private void enableLocalButtons(Boolean active) {
        Boolean status = "".equals(txtId.getText());

        btSalvar.setEnabled(active);
        btNovo.setEnabled(active && !status);
        btImportar.setEnabled(active && status);
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

    @Override
    public void onImportAction(ActionEvent e) {
        TurmaFieldsPanelEstudanteImportar dialog = new TurmaFieldsPanelEstudanteImportar();
        List<HashMap<String, Object>> dadosImportados = dialog.getData();
        if (!dadosImportados.isEmpty()) {
            try {
                int id = 1;
                for (int i = 0; i < dadosImportados.size(); i++) {
                    HashMap<String, Object> mapValue = dadosImportados.get(i);

                    Estudante estudante = EstudanteFactory.getInstance()
                            .getObject(mapValue);
                    /**
                     * Na importação não vem a identificação da turma, logo ela
                     * será adicionada aqui.
                     */
                    estudante.getId().setTurma(turma);
                    ControllerFactory.createEstudanteController().salvar(estudante);
                }
                refreshTable();
            } catch (Exception ex) {
                showErrorMessage(ex);
            }
            clear();
        }
    }

    @Override
    public void onNewAction(ActionEvent e, Object o) {
        clear();
    }

    @Override
    public void onSaveAction(ActionEvent e, Object o) {
        if (isValidated()) {
            try {
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
                ControllerFactory.createEstudanteController().salvar(estudante);
                refreshTable();
                clear();
            } catch (Exception ex) {
                showErrorMessage(ex);
            }
        }
    }

    @Override
    public void onEditAction(ActionEvent e, Object o) {
        if (o instanceof JTable) {
            Object obj = getObjectFromTable((JTable) o);
            setFieldValues(obj);
            enableLocalButtons(Boolean.TRUE);
            initFocus();
        }
    }

    @Override
    public void onDelAction(ActionEvent e, Object o) {
        try {
            if (o instanceof JTable) {
                Object obj = getObjectFromTable((JTable) o);
                Estudante estudante = (Estudante) obj;
                ControllerFactory.createEstudanteController().remover(estudante);
            }
            refreshTable();
            clear();
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    @Override
    public void onCloseAction(ActionEvent e) {
        if (frame instanceof JInternalFrame) {
            JInternalFrame f = (JInternalFrame) frame;
            f.dispose();
        } else if (frame instanceof JDialog) {
            JDialog d = (JDialog) frame;
            d.dispose();
        } else {
            JFrame f = (JFrame) frame;
            f.dispose();
        }
    }

}
