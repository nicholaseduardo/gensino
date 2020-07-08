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
import ensino.configuracoes.model.ObjetivoUC;
import ensino.configuracoes.model.PeriodoLetivo;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.configuracoes.view.models.DocenteComboBoxModel;
import ensino.configuracoes.view.models.PeriodoLetivoComboBoxListModel;
import ensino.configuracoes.view.models.PeriodoLetivoListModel;
import ensino.configuracoes.view.panels.filters.CalendarioSearch;
import ensino.configuracoes.view.panels.filters.TurmaSearch;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.model.Objetivo;
import ensino.planejamento.model.ObjetivoFactory;
import ensino.planejamento.model.ObjetivoId;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.model.PlanoDeEnsinoFactory;
import ensino.reports.ChartsFactory;
import ensino.util.types.AcoesBotoes;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
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
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEtchedBorder());

        backColor = ChartsFactory.ligthGreen;
        foreColor = ChartsFactory.darkGreen;
        setBackground(backColor);

        GenJButton btSave = createButton(new ActionHandler(AcoesBotoes.SAVE), backColor, foreColor);
        GenJButton btClose = createButton(new ActionHandler(AcoesBotoes.CLOSE), backColor, foreColor);

        JPanel panelButton = createPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButton.add(btSave);
        panelButton.add(btClose);
        add(panelButton, BorderLayout.PAGE_END);

        JPanel panel = createPanel(new GridBagLayout());
        add(panel, BorderLayout.LINE_START);
        int col = 0, row = 0;
        GridBagConstraints c = new GridBagConstraints();

        col = 0;
        GenJLabel lblId = new GenJLabel("Código: ");
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblId, c);

        txtId = new GenJTextField(5, false);
        txtId.setEnabled(false);
        lblId.setLabelFor(txtId);
        GridLayoutHelper.set(c, col, row++);
        panel.add(txtId, c);

        col = 0;
        GenJLabel lblAno = new GenJLabel("Calendário: ");
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblAno, c);

        Curso curso = selectedUnidadeCurricular.getCurso();
        compoCalendario = new CalendarioSearch();
        compoCalendario.setSelectedCampus(curso.getCampus());
        compoCalendario.addDocumentListener(new CompoSearchListener(compoCalendario));
        compoCalendario.setBackground(backColor);
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
        GenJLabel lblTurma = new GenJLabel("Turma: ");
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblTurma, c);

        compoTurma = new TurmaSearch();
        compoTurma.setSelectedCurso(curso);
        compoTurma.addDocumentListener(new CompoSearchListener(compoTurma));
        compoTurma.setBackground(backColor);
        GridLayoutHelper.set(c, col, row++);
        panel.add(compoTurma, c);

        col = 0;
        GenJLabel lblProfessor = new GenJLabel("Docente: ", JLabel.TRAILING);
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblProfessor, c);
        comboDocente = new GenJComboBox(new DocenteComboBoxModel());
        GridLayoutHelper.set(c, col, row++);
        panel.add(comboDocente, c);

        col = 0;
        txtObjetivo = new GenJTextArea(5, 50);
        JScrollPane objetivoScroll = new JScrollPane(txtObjetivo);
        objetivoScroll.setBorder(createTitleBorder("Objetivo geral"));
        objetivoScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        GridLayoutHelper.set(c, col, row++, 2, 1, GridBagConstraints.BASELINE);
        panel.add(objetivoScroll, c);

        col = 0;
        txtRecuperacao = new GenJTextArea(5, 50);
        JScrollPane recuperacaoScroll = new JScrollPane(txtRecuperacao);
        recuperacaoScroll.setBorder(createTitleBorder("Recuperação da aprendizagem"));
        recuperacaoScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        GridLayoutHelper.set(c, col, row++, 2, 1, GridBagConstraints.BASELINE);
        panel.add(recuperacaoScroll, c);
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
                planoDeEnsino = PlanoDeEnsinoFactory.getInstance()
                        .getObject(getFieldValues());
                selectedUnidadeCurricular.addPlanoDeEnsino(planoDeEnsino);
            } else {
                PlanoDeEnsinoFactory.getInstance()
                        .updateObject(planoDeEnsino, getFieldValues());
            }
            
        }
        try {
            ControllerFactory.createPlanoDeEnsinoController().salvar(planoDeEnsino);
            onCloseAction(e);
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
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
