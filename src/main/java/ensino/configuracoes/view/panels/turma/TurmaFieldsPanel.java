/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.turma;

import ensino.components.GenJComboBox;
import ensino.components.GenJLabel;
import ensino.components.GenJSpinner;
import ensino.components.GenJTextField;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.Turma;
import ensino.configuracoes.view.models.CampusComboBoxModel;
import ensino.configuracoes.view.models.CursoComboBoxListModel;
import ensino.configuracoes.view.models.CursoListModel;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.util.Calendar;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author nicho
 */
public class TurmaFieldsPanel extends DefaultFieldsPanel {

    /**
     * Atributo utilizado para indicar quando um curso está pré-selecionado
     * através do componente TurmaSearch
     */
    private Curso selectedCurso;

    private GenJTextField txtId;
    private GenJComboBox comboCampus;
    private GenJComboBox comboCurso;
    private GenJTextField txtNome;
    private GenJSpinner spinAno;
    
    private JTabbedPane tabbedPane;
    private TurmaFieldsPanelEstudante tfpe;

    public TurmaFieldsPanel(Curso curso) {
        this();
        this.selectedCurso = curso;
    }

    public TurmaFieldsPanel() {
        super();
        initComponents();
    }

    private void initComponents() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Identificação", createIdentificacaoPanel());
        tfpe = new TurmaFieldsPanelEstudante();
        tabbedPane.addTab("Estudantes", tfpe);
        add(tabbedPane);
    }

    private JPanel createIdentificacaoPanel() {
        int row = 0, col = 0;
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        GenJLabel lblId = new GenJLabel("Código:", JLabel.TRAILING);
        GridLayoutHelper.setRight(c, col++, row);
        fieldsPanel.add(lblId, c);
        txtId = new GenJTextField(10, false);
        lblId.setLabelFor(txtId);
        GridLayoutHelper.set(c, col, row++, 2, 1, GridBagConstraints.LINE_START);
        fieldsPanel.add(txtId, c);

        col = 0;
        GenJLabel lblCampus = new GenJLabel("Campus:", JLabel.TRAILING);
        GridLayoutHelper.setRight(c, col++, row);
        fieldsPanel.add(lblCampus, c);
        comboCampus = new GenJComboBox(new CampusComboBoxModel());
        comboCampus.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Campus campus = (Campus) e.getItem();
                CursoListModel cursoModel = (CursoListModel) comboCurso.getModel();
                cursoModel.setCampus(campus);
                cursoModel.refresh();
                comboCurso.setSelectedItem(selectedCurso);
            }
        });
        lblCampus.setLabelFor(comboCampus);
        GridLayoutHelper.set(c, col++, row);
        fieldsPanel.add(comboCampus, c);

        GenJLabel lblCurso = new GenJLabel("Curso:", JLabel.TRAILING);
        GridLayoutHelper.setRight(c, col++, row);
        fieldsPanel.add(lblCurso, c);
        comboCurso = new GenJComboBox(new CursoComboBoxListModel());
        lblCurso.setLabelFor(comboCurso);
        GridLayoutHelper.set(c, col, row++);
        fieldsPanel.add(comboCurso, c);

        col = 0;
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

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
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
        map.put("curso", comboCurso.getSelectedItem());
        map.putAll(tfpe.getFieldValues());
        return map;
    }

    private void setFieldValues(Integer id, String nome, Integer ano,
            Curso curso) {
        txtId.setText(id.toString());
        txtNome.setText(nome);
        spinAno.setValue(ano);

        Campus campus = curso.getId().getCampus();
        comboCampus.setSelectedItem(campus);
        CursoComboBoxListModel cursoModel = (CursoComboBoxListModel) comboCurso.getModel();
        cursoModel.setCampus(campus);
        cursoModel.setSelectedItem(curso);
        cursoModel.refresh();
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
            Turma turmna = (Turma) object;
            setFieldValues(
                    turmna.getId().getId(),
                    turmna.getNome(),
                    turmna.getAno(),
                    turmna.getId().getCurso());
        }
        tfpe.setFieldValues(object);
    }

    @Override
    public boolean isValidated() {
        return comboCurso.getSelectedItem() != null
                && !"".equals(txtNome.getText())
                && spinAno.getValue() != null;
    }

    @Override
    public void clearFields() {
        txtId.setText("");
        Calendar cal = Calendar.getInstance();
        spinAno.setValue(cal.get(Calendar.YEAR));
        txtNome.setText("");
        comboCurso.setSelectedItem(selectedCurso);
        comboCampus.setSelectedItem(selectedCurso == null ? selectedCurso
                : selectedCurso.getId().getCampus());
        tfpe.clearFields();
    }

    @Override
    public void enableFields(boolean active) {
        txtId.setEnabled(false);
        spinAno.setEnabled(active);
        txtNome.setEnabled(active);
        tfpe.enableFields(active);

        comboCurso.setEnabled(active && selectedCurso == null);
        comboCampus.setEnabled(active && selectedCurso == null);
    }

    @Override
    public void initFocus() {
        if (comboCampus.isEnabled()) {
            comboCampus.requestFocusInWindow();
        } else {
            txtNome.requestFocusInWindow();
        }
    }

    @Override
    public void componentShown(ComponentEvent e) {
        super.componentShown(e);
        tabbedPane.setSelectedIndex(0);
    }
}
