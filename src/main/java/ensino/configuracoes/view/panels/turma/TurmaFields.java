/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.turma;

import ensino.components.GenJLabel;
import ensino.components.GenJSpinner;
import ensino.components.GenJTextField;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.Turma;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
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

    private Turma turma;
    private Component frame;

    public TurmaFields(Curso curso, Component frame) {
        super();
        this.frame = frame;
        this.selectedCurso = curso;
        initComponents();
    }

    public TurmaFields() {
        this(null, null);
    }

    public void setFrame(Component frame) {
        this.frame = frame;
    }

    private void initComponents() {
        setName("turma.cadastro");
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEtchedBorder());

        URL urlTurma = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "classroom-50px.png"));

        GenJLabel lblTitulo = new GenJLabel("Ficha da Turma", new ImageIcon(urlTurma), JLabel.CENTER);
        lblTitulo.setVerticalTextPosition(JLabel.BOTTOM);
        lblTitulo.setHorizontalTextPosition(JLabel.CENTER);
        lblTitulo.resetFontSize(20);
        lblTitulo.setForeground(foreColor);
        lblTitulo.toBold();

        JPanel panel = createPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(lblTitulo);

        add(panel, BorderLayout.PAGE_START);
        add(createIdentificacaoPanel(), BorderLayout.CENTER);
    }

    private JPanel createIdentificacaoPanel() {
        GenJLabel lblId = new GenJLabel("Código:", JLabel.TRAILING);
        txtId = new GenJTextField(5, false);
        txtId.setEnabled(false);
        lblId.setLabelFor(txtId);

        GenJLabel lblNome = new GenJLabel("Nome da Turma:", JLabel.TRAILING);
        txtNome = new GenJTextField(30, true);
        lblNome.setLabelFor(txtNome);

        GenJLabel lblAno = new GenJLabel("Ano:", JLabel.TRAILING);
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        spinAno = new GenJSpinner(new SpinnerNumberModel(currentYear, 2000, currentYear + 1, 1));
        lblAno.setLabelFor(spinAno);

        int row = 0, col = 0;
        JPanel fieldsPanel = createPanel(new GridBagLayout());
        fieldsPanel.setBorder(createTitleBorder("Identificação"));
        GridBagConstraints c = new GridBagConstraints();

        GridLayoutHelper.setRight(c, col++, row);
        fieldsPanel.add(lblId, c);
        GridLayoutHelper.set(c, col, row++);
        fieldsPanel.add(txtId, c);

        col = 0;
        GridLayoutHelper.setRight(c, col++, row);
        fieldsPanel.add(lblNome, c);
        GridLayoutHelper.set(c, col, row++);
        fieldsPanel.add(txtNome, c);

        col = 0;
        GridLayoutHelper.setRight(c, col++, row);
        fieldsPanel.add(lblAno, c);
        GridLayoutHelper.set(c, col, row);
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
    }

    @Override
    public boolean isValidated() {
        String msg = "O campo [%s] não foi informado!", campo = "";
        if ("".equals(txtNome.getText())) {
            campo = "NOME DA TURMA";
            txtNome.requestFocusInWindow();
        } else if (spinAno.getValue() == null) {
            campo = "ANO";
            spinAno.requestFocusInWindow();
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
    }

    @Override
    public void enableFields(boolean active) {
        spinAno.setEnabled(active);
        txtNome.setEnabled(active);
    }

    @Override
    public void initFocus() {
        txtNome.requestFocusInWindow();
    }
}
