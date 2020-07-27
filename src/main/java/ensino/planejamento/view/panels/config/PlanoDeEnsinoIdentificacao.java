/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.config;

import ensino.components.GenJButton;
import ensino.components.GenJComboBox;
import ensino.components.GenJLabel;
import ensino.components.GenJTextArea;
import ensino.components.GenJTextField;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.PeriodoLetivo;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.configuracoes.view.models.DocenteComboBoxModel;
import ensino.configuracoes.view.models.PeriodoLetivoComboBoxListModel;
import ensino.configuracoes.view.models.PeriodoLetivoListModel;
import ensino.configuracoes.view.panels.filters.CalendarioSearch;
import ensino.configuracoes.view.panels.filters.TurmaSearch;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.planejamento.model.PlanoDeEnsino;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author nicho
 */
public class PlanoDeEnsinoIdentificacao extends DefaultFieldsPanel {

    private GenJTextField txtId;

    private CalendarioSearch compoCalendario;
    private GenJComboBox comboPeriodoLetivo;
    private GenJComboBox comboDocente;
    private TurmaSearch compoTurma;

    private GenJTextArea txtObjetivo;
    private GenJTextArea txtRecuperacao;
    private GenJButton btSalvar;

    private UnidadeCurricular selectedUnidadeCurricular;
    private PlanoDeEnsino planoDeEnsino;
    private Component frame;

    public PlanoDeEnsinoIdentificacao(Component frame) {
        this(null, frame);
    }

    public PlanoDeEnsinoIdentificacao(UnidadeCurricular unidadeCurricular,
            Component frame) {
        super();
        this.selectedUnidadeCurricular = unidadeCurricular;
        this.frame = frame;
        initComponents();
    }

    private void initComponents() {
        setName("plano.identificacao");
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEtchedBorder());

        int col = 0, row = 0;
        GridBagConstraints c = new GridBagConstraints();
        
        GenJLabel lblId = new GenJLabel("Código: ");
        txtId = new GenJTextField(5, false);
        txtId.setEnabled(false);
        lblId.setLabelFor(txtId);
        
        GenJLabel lblAno = new GenJLabel("Calendário: ");
        Curso curso = selectedUnidadeCurricular.getCurso();
        compoCalendario = new CalendarioSearch();
        compoCalendario.setSelectedCampus(curso.getCampus());
        compoCalendario.addDocumentListener(new CompoSearchListener(compoCalendario));
        compoCalendario.setBackground(backColor);
        lblAno.setLabelFor(compoCalendario);
        
        
        GenJLabel lblPeriodoLetivo = new GenJLabel("PeriodoLetivo: ");
        comboPeriodoLetivo = new GenJComboBox(new PeriodoLetivoComboBoxListModel());
        refreshComboPeriodoLetivo(compoCalendario.getObjectValue());
        lblPeriodoLetivo.setLabelFor(comboPeriodoLetivo);
        
        
        GenJLabel lblTurma = new GenJLabel("Turma: ");
        compoTurma = new TurmaSearch();
        compoTurma.setSelectedCurso(curso);
        compoTurma.addDocumentListener(new CompoSearchListener(compoTurma));
        compoTurma.setBackground(backColor);
        
        
        GenJLabel lblProfessor = new GenJLabel("Docente: ", JLabel.TRAILING);
        comboDocente = new GenJComboBox(new DocenteComboBoxModel());
                
        txtObjetivo = new GenJTextArea(3, 50);
        JScrollPane objetivoScroll = new JScrollPane(txtObjetivo);
        objetivoScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        objetivoScroll.setBorder(createTitleBorder("Descrição do objetivo geral"));
        
        txtRecuperacao = new GenJTextArea(3, 50);
        JScrollPane recuperacaoScroll = new JScrollPane(txtRecuperacao);
        recuperacaoScroll.setBorder(createTitleBorder("Recuperação da aprendizagem"));
        recuperacaoScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        JPanel panel = createPanel(new GridBagLayout());
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblId, c);
        GridLayoutHelper.set(c, col, row++);
        panel.add(txtId, c);

        col = 0;
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblAno, c);
        GridLayoutHelper.set(c, col, row++);
        panel.add(compoCalendario, c);

        col = 0;
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblPeriodoLetivo, c);
        GridLayoutHelper.set(c, col, row++);
        panel.add(comboPeriodoLetivo, c);

        col = 0;
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblTurma, c);
        GridLayoutHelper.set(c, col, row++);
        panel.add(compoTurma, c);

        col = 0;
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblProfessor, c);
        GridLayoutHelper.set(c, col, row++);
        panel.add(comboDocente, c);

        col = 0;
        GridLayoutHelper.set(c, col, row++, 2, 1, GridBagConstraints.LINE_START);
        panel.add(objetivoScroll, c);
        
        GridLayoutHelper.set(c, col, row++, 2, 1, GridBagConstraints.LINE_START);
        panel.add(recuperacaoScroll, c);
        
        add(panel, BorderLayout.CENTER);
    }

    private void refreshComboPeriodoLetivo(Calendario cal) {
        PeriodoLetivoListModel periodoLetivoModel = (PeriodoLetivoListModel) comboPeriodoLetivo.getModel();
        periodoLetivoModel.setCalendario(cal);
        periodoLetivoModel.refresh();
        comboPeriodoLetivo.setSelectedItem(null);
        comboPeriodoLetivo.repaint();
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = new HashMap();

        String sId = txtId.getText();
        map.put("id", sId.matches("\\d+") ? Integer.parseInt(sId) : null);
        map.put("docente", comboDocente.getSelectedItem());
        map.put("unidadeCurricular", this.selectedUnidadeCurricular);
        map.put("calendario", compoCalendario.getObjectValue());
        map.put("periodoLetivo", comboPeriodoLetivo.getSelectedItem());
        map.put("turma", compoTurma.getObjectValue());
        map.put("recuperacao", txtRecuperacao.getText());
        map.put("objetivoGeral", txtObjetivo.getText());

        return map;
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {

    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof PlanoDeEnsino) {
            planoDeEnsino = (PlanoDeEnsino) object;
            selectedUnidadeCurricular = planoDeEnsino.getUnidadeCurricular();
            Curso curso = selectedUnidadeCurricular.getCurso();
            compoCalendario.setSelectedCampus(curso.getCampus());

            txtId.setText(planoDeEnsino.getId().toString());
            comboDocente.setSelectedItem(planoDeEnsino.getDocente());

            PeriodoLetivo periodoLetivo = planoDeEnsino.getPeriodoLetivo();
            if (periodoLetivo != null) {
                compoCalendario.setObjectValue(periodoLetivo.getId().getCalendario());
            } else {
                compoCalendario.setObjectValue(null);
            }

            PeriodoLetivoComboBoxListModel periodoLetivoListModel = (PeriodoLetivoComboBoxListModel) comboPeriodoLetivo.getModel();
            periodoLetivoListModel.setSelectedItem(periodoLetivo);

            compoTurma.setSelectedCurso(curso);
            compoTurma.setObjectValue(planoDeEnsino.getTurma());
            txtObjetivo.setText(planoDeEnsino.getObjetivoGeral());
            txtRecuperacao.setText(planoDeEnsino.getRecuperacao());
        }
    }

    @Override
    public boolean isValidated() {
        String msg = "O campo [%s] não foi informado!",
                campo = "";
        if (compoCalendario.getObjectValue() == null) {
            campo = "Calendário";
            compoCalendario.requestFocusInWindow();
        } else if (comboPeriodoLetivo.getSelectedItem() == null) {
            campo = "Período Letivo";
            comboPeriodoLetivo.requestFocusInWindow();
        } else if (comboDocente.getSelectedItem() == null) {
            campo = "Docente";
            comboDocente.requestFocusInWindow();
        } else if (compoTurma.getObjectValue() == null) {
            campo = "Turma";
            compoTurma.requestFocusInWindow();
        } else if ("".equals(txtObjetivo.getText())) {
            campo = "Objetivo";
            txtObjetivo.requestFocusInWindow();
        } else if ("".equals(txtRecuperacao.getText())) {
            campo = "Recuperação da aprendizagem";
            txtRecuperacao.requestFocusInWindow();
        } else {
            return true;
        }
        showInformationMessage(String.format(msg, campo));
        return false;
    }

    @Override
    public void clearFields() {
        txtId.setText("");
        compoCalendario.setObjectValue(null);
        comboPeriodoLetivo.setSelectedItem(null);
        comboDocente.setSelectedItem(null);
        compoTurma.setObjectValue(null);
        txtObjetivo.setText("");
        txtRecuperacao.setText("");
    }

    @Override
    public void enableFields(boolean active) {
        compoCalendario.setEnable(active);
        comboPeriodoLetivo.setEnabled(active);
        comboDocente.setEnabled(active);
        compoTurma.setEnable(active);
        txtObjetivo.setEnabled(active);
    }

    @Override
    public void initFocus() {
        compoCalendario.requestFocusInWindow();
    }

    private class CompoSearchListener implements DocumentListener {

        private Component source;

        public CompoSearchListener(Component source) {
            this.source = source;
        }

        private void changedSource() {
            if (source == compoCalendario) {
                Calendario cal = (Calendario) compoCalendario.getObjectValue();

                PeriodoLetivoComboBoxListModel periodoLetivoListModel = (PeriodoLetivoComboBoxListModel) comboPeriodoLetivo.getModel();
                PeriodoLetivo pl = (PeriodoLetivo) periodoLetivoListModel.getSelectedItem();

                if ((pl == null) || (pl != null && !pl.getCalendario().equals(cal))) {
                    refreshComboPeriodoLetivo(cal);
                }
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
