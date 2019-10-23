/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.planoDeEnsino;

import ensino.components.GenJComboBox;
import ensino.components.GenJLabel;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.configuracoes.view.models.CampusComboBoxModel;
import ensino.configuracoes.view.models.DocenteComboBoxModel;
import ensino.configuracoes.view.models.PeriodoLetivoComboBoxListModel;
import ensino.configuracoes.view.models.PeriodoLetivoListModel;
import ensino.configuracoes.view.panels.filters.CalendarioSearch;
import ensino.configuracoes.view.panels.filters.CursoSearch;
import ensino.configuracoes.view.panels.filters.TurmaSearch;
import ensino.configuracoes.view.panels.filters.UnidadeCurricularSearch;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.planejamento.model.PlanoDeEnsino;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author nicho
 */
public class IdentificacaoPanel extends DefaultFieldsPanel {

    private Integer planoId;

    private CursoSearch compoCurso;
    private GenJComboBox comboCampus;
    private UnidadeCurricularSearch compoUnidadeCurricular;

    private CalendarioSearch compoCalendario;
    private GenJComboBox comboPeriodoLetivo;
    private GenJComboBox comboDocente;
    private TurmaSearch compoTurma;

    private UnidadeCurricular selectedUnidadeCurricular;

    public IdentificacaoPanel() {
        this(null);
    }

    public IdentificacaoPanel(UnidadeCurricular unidadeCurricular) {
        super();
        this.selectedUnidadeCurricular = unidadeCurricular;
        initComponents();
    }

    private void initComponents() {
        setName("plano.identificacao");
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        setBorder(BorderFactory.createEtchedBorder());
        
        JPanel panel = new JPanel(new GridBagLayout());
        add(panel);
        int col = 0, row = 0;
        GridBagConstraints c = new GridBagConstraints();

        GenJLabel lblCampus = new GenJLabel("Campus: ");
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblCampus, c);

        comboCampus = new GenJComboBox(new CampusComboBoxModel());
        comboCampus.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Campus campus = (Campus) e.getItem();

                // atualização do componente do curso
                compoCurso.setSelectedCampus(campus);
                compoCurso.setObjectValue(null);

                // atualização do componente do calendario
                compoCalendario.setSelectedCampus(campus);
                compoCalendario.setObjectValue(null);
            }
        });
        GridLayoutHelper.set(c, col, row++, 3, 1, GridBagConstraints.BASELINE_LEADING);
        panel.add(comboCampus, c);

        col = 0;
        GenJLabel lblAno = new GenJLabel("Ano: ");
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblAno, c);

        compoCalendario = new CalendarioSearch();
        compoCalendario.addDocumentListener(new CompoSearchListener(compoCalendario));
        lblAno.setLabelFor(compoCalendario);
        GridLayoutHelper.set(c, col, row++);
        panel.add(compoCalendario, c);

        col = 0;
        GenJLabel lblPeriodoLetivo = new GenJLabel("PeriodoLetivo: ");
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblPeriodoLetivo, c);

        comboPeriodoLetivo = new GenJComboBox(new PeriodoLetivoComboBoxListModel());
        refreshComboPeriodoLetivo(compoCalendario.getObjectValue());
        lblPeriodoLetivo.setLabelFor(comboPeriodoLetivo);
        GridLayoutHelper.set(c, col, row++);
        panel.add(comboPeriodoLetivo, c);

        col = 0;
        GenJLabel lblCurso = new GenJLabel("Curso: ");
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblCurso, c);

        compoCurso = new CursoSearch();
        compoCurso.addDocumentListener(new CompoSearchListener(compoCurso));
        GridLayoutHelper.set(c, col, row++);
        panel.add(compoCurso, c);

        col = 0;
        GenJLabel lblUnidade = new GenJLabel("Unidade Curricular: ");
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblUnidade, c);

        compoUnidadeCurricular = new UnidadeCurricularSearch();
        compoUnidadeCurricular.addDocumentListener(new CompoSearchListener(compoUnidadeCurricular));
        GridLayoutHelper.set(c, col, row++);
        panel.add(compoUnidadeCurricular, c);

        col = 0;
        GenJLabel lblTurma = new GenJLabel("Turma: ");
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblTurma, c);

        compoTurma = new TurmaSearch();
        compoTurma.addDocumentListener(new CompoSearchListener(compoTurma));
        GridLayoutHelper.set(c, col, row++);
        panel.add(compoTurma, c);

        col = 0;
        GenJLabel lblProfessor = new GenJLabel("Professor: ", JLabel.TRAILING);
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblProfessor, c);
        comboDocente = new GenJComboBox(new DocenteComboBoxModel());
        GridLayoutHelper.set(c, col, row++);
        panel.add(comboDocente, c);
    }

    private void refreshComboPeriodoLetivo(Calendario cal) {
        PeriodoLetivoListModel periodoLetivoModel = (PeriodoLetivoListModel) comboPeriodoLetivo.getModel();
        periodoLetivoModel.setCalendario(cal);
        periodoLetivoModel.refresh();
        comboPeriodoLetivo.setSelectedItem(null);
        comboPeriodoLetivo.repaint();
    }

    private void loadUnidadeCurricularDados() {
        if (selectedUnidadeCurricular != null) {
            Curso curso = selectedUnidadeCurricular.getCurso();

            comboCampus.setSelectedItem(curso.getCampus());
            compoCurso.setObjectValue(curso);
            compoUnidadeCurricular.setObjectValue(selectedUnidadeCurricular);
        }
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = new HashMap();

        map.put("id", planoId);
        map.put("docente", comboDocente.getSelectedItem());
        map.put("unidadeCurricular", compoUnidadeCurricular.getObjectValue());
        map.put("calendario", compoCalendario.getObjectValue());
        map.put("periodoLetivo", comboPeriodoLetivo.getSelectedItem());
        map.put("turma", compoTurma.getObjectValue());

        return map;
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {

    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof PlanoDeEnsino) {
            PlanoDeEnsino plano = (PlanoDeEnsino) object;
            planoId = plano.getId();
            comboDocente.setSelectedItem(plano.getDocente());

            Curso curso = plano.getUnidadeCurricular().getCurso();
            comboCampus.setSelectedItem(curso.getCampus());
            compoCalendario.setObjectValue(plano.getCalendario());
            comboPeriodoLetivo.setSelectedItem(plano.getPeriodoLetivo());
            compoTurma.setObjectValue(plano.getTurma());
            compoCurso.setObjectValue(curso);
            compoUnidadeCurricular.setObjectValue(plano.getUnidadeCurricular());
            loadUnidadeCurricularDados();
        }
    }

    @Override
    public boolean isValidated() {
        if (compoCalendario.getObjectValue() == null) {
            compoCalendario.requestFocusInWindow();
        } else if (comboPeriodoLetivo.getSelectedItem() == null) {
            comboPeriodoLetivo.requestFocusInWindow();
        } else if (comboDocente.getSelectedItem() == null) {
            comboDocente.requestFocusInWindow();
        } else if (compoTurma.getObjectValue() == null) {
            compoTurma.requestFocusInWindow();
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void clearFields() {
        comboCampus.setSelectedItem(null);
        compoCalendario.setObjectValue(null);
        comboPeriodoLetivo.setSelectedItem(null);
        compoCurso.setObjectValue(null);
        compoUnidadeCurricular.setObjectValue(null);
        comboDocente.setSelectedItem(null);
        planoId = null;
    }

    @Override
    public void enableFields(boolean active) {
        boolean bSelectedUnidade = this.selectedUnidadeCurricular == null
                && active;
        comboCampus.setEnabled(bSelectedUnidade);
        compoCurso.setEnable(bSelectedUnidade);
        compoUnidadeCurricular.setEnable(bSelectedUnidade);
        compoCalendario.setEnable(active);
        comboPeriodoLetivo.setEnabled(active);
        comboDocente.setEnabled(active);
        compoTurma.setEnable(active);
    }

    @Override
    public void initFocus() {
        if (selectedUnidadeCurricular != null) {
            compoCalendario.requestFocusInWindow();
        } else {
            comboCampus.requestFocusInWindow();
        }
    }

    private class CompoSearchListener implements DocumentListener {

        private Component source;

        public CompoSearchListener(Component source) {
            this.source = source;
        }

        private void changedSource() {
            if (source == compoCurso) {
                compoUnidadeCurricular.setSelectedCurso(compoCurso.getObjectValue());
                compoTurma.setSelectedCurso(compoCurso.getObjectValue());
            } else if (source == compoUnidadeCurricular) {
                loadUnidadeCurricularDados();
            } else if (source == compoCalendario) {
                Calendario cal = (Calendario) compoCalendario.getObjectValue();
                refreshComboPeriodoLetivo(cal);
            }
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            changedSource();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            changedSource();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }
    }
}
