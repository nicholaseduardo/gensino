/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.permanenciaEstudantil;

import ensino.components.GenJButton;
import ensino.components.GenJFormattedTextField;
import ensino.components.GenJLabel;
import ensino.components.GenJList;
import ensino.components.GenJSpinner;
import ensino.components.GenJTextArea;
import ensino.components.GenJTextField;
import ensino.configuracoes.model.Estudante;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.DateHelper;
import ensino.helpers.GridLayoutHelper;
import ensino.planejamento.model.AtendimentoEstudante;
import ensino.planejamento.model.AtendimentoEstudanteFactory;
import ensino.planejamento.model.AtendimentoEstudanteId;
import ensino.planejamento.model.PermanenciaEstudantil;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.models.AtendimentoEstudanteListModel;
import ensino.planejamento.view.renderer.AtendimentoEstudanteListCellRenderer;
import ensino.reports.ChartsFactory;
import ensino.util.types.Presenca;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;

/**
 *
 * @author nicho
 */
public class PermanenciaEstudantilFieldsPanel extends DefaultFieldsPanel {

    private PlanoDeEnsino planoDeEnsino;
    private List<AtendimentoEstudante> listaAtendimentoEstudantes;

    private GenJLabel lblStatus;

    private GenJTextField txtId;
    private GenJTextArea txtDescricao;
    private GenJFormattedTextField txtData;
    private GenJSpinner spinHora;

    private GenJButton btMarcar;
    private GenJButton btDesmarcar;

    private GenJList atendimentoEstudanteList;
    private AtendimentoEstudanteListModel atendimentoEstudanteListModel;

    public PermanenciaEstudantilFieldsPanel(PlanoDeEnsino planoDeEnsino) {
        super();
        this.planoDeEnsino = planoDeEnsino;
        
        /**
         * Pega os estudantes do plano de ensino e adiciona automaticamente na
         * tabela
         */
        List<Estudante> l = planoDeEnsino.getTurma().getEstudantes();
        listaAtendimentoEstudantes = new ArrayList();
        Integer seq = 1;
        for (Estudante e : l) {
            AtendimentoEstudante ae = AtendimentoEstudanteFactory.getInstance().
                    createObject(new AtendimentoEstudanteId(seq++, null, e),
                            Presenca.FALTA);
            listaAtendimentoEstudantes.add(ae);
        }
        
        initComponents();
    }

    private void initComponents() {
        try {
            setLayout(new BorderLayout(5, 5));

            add(createHeaderPanel(), BorderLayout.PAGE_START);
            add(createIdentificacaoPanel(), BorderLayout.CENTER);
            add(createListPanel(), BorderLayout.PAGE_END);

        } catch (ParseException ex) {
            showErrorMessage(ex);
        }
    }

    private JPanel createHeaderPanel() {
        GenJLabel lblTitle = new GenJLabel("Atendimento Estudantil");
        lblTitle.resetFontSize(20);
        JPanel panelTitle = createPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelTitle.add(lblTitle);

        GenJLabel lblDisciplina = new GenJLabel(
                String.format("Disciplina: %s",
                        planoDeEnsino.getUnidadeCurricular().getNome()));
        lblDisciplina.resetFontSize(16);
        JPanel panelDisc = createPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelDisc.add(lblDisciplina);

        GenJLabel lblTurmaPlano = new GenJLabel(
                String.format("Plano de Ensino: %d - Turma: %s - Período: %s",
                        planoDeEnsino.getId(),
                        planoDeEnsino.getTurma().getNome(),
                        planoDeEnsino.getPeriodoLetivo().getDescricao())
        );
        lblTurmaPlano.resetFontSize(12);
        JPanel panelTurma = createPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelTurma.add(lblTurmaPlano);

        JPanel panel = createPanel(new GridLayout(0, 1));
        panel.add(panelTitle);
        panel.add(panelDisc);
        panel.add(panelTurma);

        return panel;
    }

    private JPanel createIdentificacaoPanel() throws ParseException {
        JPanel fieldsPanel = createPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        GenJLabel lblId = new GenJLabel("Código:", JLabel.TRAILING);
        txtId = new GenJTextField(10, false);
        lblId.setLabelFor(txtId);

        txtDescricao = new GenJTextArea(3, 50);
        JScrollPane scrollDescricao = new JScrollPane(txtDescricao);
        scrollDescricao.setAutoscrolls(true);
        scrollDescricao.setBorder(createTitleBorder("Descrição do conteúdo programático do atendimento"));

        GenJLabel lblData = new GenJLabel("Data:");
        txtData = GenJFormattedTextField.createFormattedField("##/##/####", 1);
        txtData.setColumns(8);
        lblData.setLabelFor(txtData);

        GenJLabel lblHorario = new GenJLabel("Horario:", JLabel.TRAILING);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 7);
        cal.set(Calendar.MINUTE, 0);
        Date firstDate = cal.getTime();
        spinHora = new GenJSpinner(new SpinnerDateModel(firstDate,
                null, null, Calendar.DATE));
        spinHora.setEditor(new JSpinner.DateEditor(spinHora, "HH:mm"));
        lblHorario.setLabelFor(spinHora);

        JPanel panelAtendimento = createPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panelAtendimento.setBorder(createTitleBorder("Data e Hora do Atendimento"));
        panelAtendimento.add(lblData);
        panelAtendimento.add(txtData);
        panelAtendimento.add(lblHorario);
        panelAtendimento.add(spinHora);

        int row = 0, col = 0;
        GridLayoutHelper.setRight(c, col++, row);
        fieldsPanel.add(lblId, c);
        GridLayoutHelper.set(c, col, row++);
        fieldsPanel.add(txtId, c);

        col = 0;
        GridLayoutHelper.set(c, col, row++, 2, 1, GridBagConstraints.LINE_START);
        fieldsPanel.add(scrollDescricao, c);

        GridLayoutHelper.set(c, col, row, 2, 1, GridBagConstraints.LINE_START);
        fieldsPanel.add(panelAtendimento, c);

        JPanel panel = createPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel.add(fieldsPanel);

        return panel;
    }

    private void updateStatus() {
        int ausentes = 0, presentes = 0;
        for (int i = 0; i < atendimentoEstudanteListModel.getSize(); i++) {
            AtendimentoEstudante o = atendimentoEstudanteListModel.getElementAt(i);
            if (!o.getId().getEstudante().isDesligado()) {
                if (o.isPresente()) {
                    presentes++;
                } else {
                    ausentes++;
                }
            }
            lblStatus.setText(String.format(
                    "[ Presentes: %d ] | [ Ausentes: %d ]",
                    presentes, ausentes));
        }
    }

    private JPanel createListPanel() {
        ButtonAction btAction = new ButtonAction();

        btMarcar = new GenJButton("Marcar todos");
        btDesmarcar = new GenJButton("Desmarcar todos");
        btMarcar.addActionListener(btAction);
        btDesmarcar.addActionListener(btAction);
        JPanel panelButtons = createPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panelButtons.add(btMarcar);
        panelButtons.add(btDesmarcar);

        lblStatus = new GenJLabel();
        lblStatus.setForeground(Color.WHITE);
        JPanel panelStatus = createPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        panelStatus.setBackground(Color.DARK_GRAY);
        panelStatus.add(lblStatus);

        atendimentoEstudanteList = new GenJList();
        atendimentoEstudanteList.addMouseListener(new PermanenciaEstudantilListMouseListener());
        atendimentoEstudanteList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        atendimentoEstudanteList.setLayoutOrientation(JList.VERTICAL);
        atendimentoEstudanteList.setVisibleRowCount(10);
        atendimentoEstudanteList.setCellRenderer(new AtendimentoEstudanteListCellRenderer());
        atendimentoEstudanteList.clearSelection();
        setData(listaAtendimentoEstudantes);

        JScrollPane scroll = new JScrollPane(atendimentoEstudanteList);
        scroll.setAutoscrolls(true);

        JPanel panel = createPanel(new BorderLayout(5, 5));
        panel.setBorder(createTitleBorder("Lista de Presença"));
        panel.add(panelButtons, BorderLayout.PAGE_START);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(panelStatus, BorderLayout.PAGE_END);

        return panel;
    }

    private void setData(List<AtendimentoEstudante> data) {
        atendimentoEstudanteListModel = new AtendimentoEstudanteListModel(data);
        refreshAtendimentoEstudante();
    }

    private void refreshAtendimentoEstudante() {
        atendimentoEstudanteList.setModel(atendimentoEstudanteListModel);
        atendimentoEstudanteList.repaint();
        updateStatus();
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        try {
            HashMap<String, Object> map = new HashMap<>();

            map.put("id", ("".equals(txtId.getText()) ? null
                    : Integer.parseInt(txtId.getText())));
            map.put("descricao", txtDescricao.getText());
            map.put("dataAtendimento", DateHelper.stringToDate(txtData.getText(), "dd/MM/yyyy"));
            map.put("horaAtendimento", (Date) spinHora.getValue());
            map.put("planoDeEnsino", planoDeEnsino);

            map.put("atendimentos", atendimentoEstudanteListModel.getData());

            return map;
        } catch (ParseException ex) {
            showErrorMessage(ex);
            return null;
        }
    }

    private void setFieldValues(Integer id, String descricao, Date data,
            Date hora) {
        txtId.setText(id.toString());
        txtDescricao.setText(descricao);
        txtData.setValue(DateHelper.dateToString(data, "dd/MM/yyyy"));
        spinHora.setValue(hora);
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {
        setFieldValues(
                (Integer) mapValues.get("id"),
                (String) mapValues.get("descricao"),
                (Date) mapValues.get("dataAtendimento"),
                (Date) mapValues.get("horaAtendimento"));
    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof PermanenciaEstudantil) {
            PermanenciaEstudantil permanenciaEstudantil = (PermanenciaEstudantil) object;

            setFieldValues(
                    permanenciaEstudantil.getId().getSequencia(),
                    permanenciaEstudantil.getDescricao(),
                    permanenciaEstudantil.getDataAtendimento(),
                    permanenciaEstudantil.getHoraAtendimento());

            setData(permanenciaEstudantil.getAtendimentos());
        }
    }

    @Override
    public boolean isValidated() {
        String msg = "O campo %s não foi informado.", campo = "";
        if ("".equals(txtDescricao.getText())) {
            msg = "CONTEÚDO DISCUTIDO";
            txtDescricao.requestFocusInWindow();
        } else if (txtData.getValue() == null) {
            msg = "DATA";
            txtData.requestFocusInWindow();
        } else if (spinHora.getValue() == null) {
            msg = "HORA";
            spinHora.requestFocusInWindow();
        } else {
            return true;
        }
        showInformationMessage(String.format(msg, campo));
        return false;
    }

    @Override
    public void clearFields() {
        txtId.setText("");
        txtDescricao.setText("");

        Calendar cal = Calendar.getInstance();
        txtData.setText(DateHelper.dateToString(cal.getTime(), "dd/MM/yyyy"));
        spinHora.setValue(cal.getTime());
        
        setData(listaAtendimentoEstudantes);
    }

    @Override
    public void enableFields(boolean active) {
        txtId.setEnabled(false);
        txtDescricao.setEnabled(active);
        txtData.setEnabled(active);
        spinHora.setEnabled(active);

        btDesmarcar.setEnabled(active);
        btMarcar.setEnabled(active);
        atendimentoEstudanteList.setEnabled(active);
    }

    @Override
    public void initFocus() {
        txtDescricao.requestFocusInWindow();
    }

    private class PermanenciaEstudantilListMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            AtendimentoEstudante o = (AtendimentoEstudante) atendimentoEstudanteList.getSelectedValue();
            if (o != null) {
                if (o.isPresente()) {
                    o.setPresenca(Presenca.FALTA);
                } else {
                    o.setPresenca(Presenca.PRESENTE);
                }
                updateStatus();
            }
        }

    }

    private class ButtonAction implements ActionListener {

        private void alterar(Boolean check) {
            for (int i = 0; i < atendimentoEstudanteListModel.getSize(); i++) {
                AtendimentoEstudante o = atendimentoEstudanteListModel.getElementAt(i);
                if (!o.getId().getEstudante().isDesligado()) {
                    if (check) {
                        o.setPresenca(Presenca.PRESENTE);
                    } else {
                        o.setPresenca(Presenca.FALTA);
                    }
                }
            }
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            Object source = ae.getSource();
            if (source == btMarcar) {
                alterar(Boolean.TRUE);
            } else if (source == btDesmarcar) {
                alterar(Boolean.FALSE);
            }
            updateStatus();
            atendimentoEstudanteList.repaint();
        }

    }
}
