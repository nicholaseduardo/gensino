/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.turma;

import ensino.components.GenJButton;
import ensino.components.GenJLabel;
import ensino.components.GenJSpinner;
import ensino.components.GenJTextField;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.Turma;
import ensino.configuracoes.model.TurmaFactory;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.ControllerFactory;
import ensino.reports.ChartsFactory;
import ensino.util.types.AcoesBotoes;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author nicho
 */
public class TurmaFields extends DefaultFieldsPanel {

    /**
     * Atributo utilizado para indicar quando um curso está pré-selecionado
     * através do componente TurmaSearch
     */
    private Curso selectedCurso;

    private GenJTextField txtId;
    private GenJTextField txtNome;
    private GenJSpinner spinAno;

    private JTabbedPane tabbedPane;
    private TurmaFieldsPanelEstudante tfpe;

    private Turma turma;
    private Component frame;

    public TurmaFields(Curso curso, Component frame) {
        this();
        this.selectedCurso = curso;
        this.frame = frame;
    }

    public TurmaFields() {
        super();
        initComponents();
    }

    private void initComponents() {
        setName("turma.cadastro");
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEtchedBorder());

        backColor = ChartsFactory.lightBlue;
        foreColor = ChartsFactory.ardoziaBlueColor;
        setBackground(backColor);

        URL urlTurma = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "classroom-50px.png"));

        GenJLabel lblTitulo = new GenJLabel("Ficha da Turma", new ImageIcon(urlTurma), JLabel.CENTER);
        lblTitulo.setVerticalTextPosition(JLabel.BOTTOM);
        lblTitulo.setHorizontalTextPosition(JLabel.CENTER);
        lblTitulo.resetFontSize(20);
        lblTitulo.setForeground(foreColor);
        lblTitulo.toBold();
        add(lblTitulo, BorderLayout.PAGE_START);

        GenJButton btSave = createButton(new ActionHandler(AcoesBotoes.SAVE), backColor, foreColor);
        GenJButton btClose = createButton(new ActionHandler(AcoesBotoes.CLOSE), backColor, foreColor);

        JPanel panelButton = createPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButton.add(btSave);
        panelButton.add(btClose);
        add(panelButton, BorderLayout.PAGE_END);

        tfpe = new TurmaFieldsPanelEstudante();
        tfpe.setBorder(createTitleBorder("Dados dos Estudantes"));

        JPanel panelInfo = createPanel(new BorderLayout());
        panelInfo.add(createIdentificacaoPanel(), BorderLayout.PAGE_START);
        panelInfo.add(tfpe, BorderLayout.CENTER);

        add(panelInfo, BorderLayout.CENTER);
    }

    private JPanel createIdentificacaoPanel() {
        int row = 0, col = 0;
        JPanel fieldsPanel = createPanel(new GridBagLayout());
        fieldsPanel.setBorder(createTitleBorder("Identificação"));
        GridBagConstraints c = new GridBagConstraints();

        GenJLabel lblId = new GenJLabel("Código:", JLabel.TRAILING);
        GridLayoutHelper.setRight(c, col++, row);
        fieldsPanel.add(lblId, c);
        txtId = new GenJTextField(5, false);
        txtId.setEnabled(false);
        lblId.setLabelFor(txtId);
        GridLayoutHelper.set(c, col++, row);
        fieldsPanel.add(txtId, c);

        GenJLabel lblNome = new GenJLabel("Nome da Turma:", JLabel.TRAILING);
        GridLayoutHelper.setRight(c, col++, row);
        fieldsPanel.add(lblNome, c);
        txtNome = new GenJTextField(10, true);
        lblNome.setLabelFor(txtNome);
        GridLayoutHelper.set(c, col++, row);
        c.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(txtNome, c);

        GenJLabel lblAno = new GenJLabel("Ano:", JLabel.TRAILING);
        GridLayoutHelper.setRight(c, col++, row);
        fieldsPanel.add(lblAno, c);
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        spinAno = new GenJSpinner(new SpinnerNumberModel(currentYear, 2000, currentYear + 1, 1));
        lblAno.setLabelFor(spinAno);
        GridLayoutHelper.set(c, col++, row);
        fieldsPanel.add(spinAno, c);

        JPanel panel = createPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel.add(fieldsPanel);

        return panel;
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("id", ("".equals(txtId.getText()) ? null
                : Integer.parseInt(txtId.getText())));
        map.put("nome", txtNome.getText());
        map.put("ano", (Integer) spinAno.getValue());
        map.put("curso", selectedCurso);
        map.putAll(tfpe.getFieldValues());
        return map;
    }

    private void setFieldValues(Integer id, String nome, Integer ano,
            Curso curso) {
        txtId.setText(id.toString());
        txtNome.setText(nome);
        spinAno.setValue(ano);
        selectedCurso = curso;
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {
        setFieldValues(
                (Integer) mapValues.get("id"),
                (String) mapValues.get("nome"),
                (Integer) mapValues.get("ano"),
                (Curso) mapValues.get("curso"));
        tfpe.setFieldValues(mapValues);
    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof Turma) {
            turma = (Turma) object;
            setFieldValues(
                    turma.getId().getId(),
                    turma.getNome(),
                    turma.getAno(),
                    turma.getId().getCurso());
        }
        tfpe.setFieldValues(object);
    }

    @Override
    public boolean isValidated() {
        String msg = "O campo [%s] não foi informado!", campo = "";
        if ("".equals(txtNome.getText())) {
            campo = "NOME DA TURMA";
        } else if (spinAno.getValue() == null) {
            campo = "ANO";
        } else {
            return true;
        }
        showInformationMessage(String.format(msg, campo));
        return false;
    }

    @Override
    public void clearFields() {
        txtId.setText("");
        Calendar cal = Calendar.getInstance();
        spinAno.setValue(cal.get(Calendar.YEAR));
        txtNome.setText("");
        tfpe.clearFields();
    }

    @Override
    public void enableFields(boolean active) {
        txtId.setEnabled(false);
        spinAno.setEnabled(active);
        txtNome.setEnabled(active);
        tfpe.enableFields(active);
    }

    @Override
    public void initFocus() {
        txtNome.requestFocusInWindow();
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

    @Override
    public void onSaveAction(ActionEvent e, Object o) {
        if (isValidated()) {
            if ("".equals(txtId.getText())) {
                turma = TurmaFactory.getInstance().getObject(getFieldValues());
            } else {
                TurmaFactory.getInstance().updateObject(turma, getFieldValues());
            }
            try {
                ControllerFactory.createTurmaController().salvar(turma);
                if ("".equals(txtId.getText())) {
                    txtId.setText(turma.getId().getId().toString());
                    selectedCurso.addTurma(turma);
                }
                onCloseAction(e);
            } catch (Exception ex) {
                showErrorMessage(ex);
            }
        }
    }

    @Override
    public void componentShown(ComponentEvent e) {
        super.componentShown(e);
        tabbedPane.setSelectedIndex(0);
    }
}
