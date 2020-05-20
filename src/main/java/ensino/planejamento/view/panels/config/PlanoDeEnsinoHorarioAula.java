/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.config;

import ensino.components.GenJButton;
import ensino.components.GenJComboBox;
import ensino.components.GenJLabel;
import ensino.components.GenJSpinner;
import ensino.components.GenJTextField;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.controller.DiarioController;
import ensino.planejamento.controller.HorarioAulaController;
import ensino.planejamento.model.Diario;
import ensino.planejamento.model.HorarioAula;
import ensino.planejamento.model.HorarioAulaFactory;
import ensino.planejamento.model.HorarioAulaId;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.models.HorarioAulaTableModel;
import ensino.planejamento.view.renderer.HorarioAulaCellRenderer;
import ensino.reports.ChartsFactory;
import ensino.util.types.AcoesBotoes;
import ensino.util.types.DiaDaSemana;
import ensino.util.types.Turno;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author nicho
 */
public class PlanoDeEnsinoHorarioAula extends DefaultFieldsPanel {

    private PlanoDeEnsino planoDeEnsino;
    private GenJTextField txtId;
    private GenJComboBox comboDiaDaSemana;
    private GenJSpinner spinHorario;

    private GenJButton btAdd;
    private GenJButton btDel;
    private GenJButton btNew;
    private GenJButton btUpdate;
    private GenJButton btGen;

    private GenJLabel lblStatus;

    private JTable horarioAulaTable;
    private HorarioAulaTableModel horarioAulaTableModel;
    private Component frame;
    private HorarioAulaController col;

    public PlanoDeEnsinoHorarioAula(Component frame) {
        super("Horário das Aulas");
        this.frame = frame;
        initComponents();
    }

    private void initComponents() {
        try {
            setName("horario.aulas");
            setLayout(new BorderLayout(5, 5));

            backColor = ChartsFactory.ligthGreen;
            foreColor = ChartsFactory.darkGreen;
            setBackground(backColor);
            col = ControllerFactory.createHorarioAulaController();

            GenJButton btSave = createButton(new ActionHandler(AcoesBotoes.SAVE), backColor, foreColor);
            GenJButton btClose = createButton(new ActionHandler(AcoesBotoes.CLOSE), backColor, foreColor);

            JPanel panelButton = createPanel(new FlowLayout(FlowLayout.RIGHT));
            panelButton.add(btSave);
            panelButton.add(btClose);
            add(panelButton, BorderLayout.PAGE_END);
            /**
             * A tabela foi criada antes por questões de processo lógico da
             * construção da ação dos botões.
             */
            add(createTablePane(), BorderLayout.CENTER);

            JPanel panelDados = createPanel(new BorderLayout(5, 5));
            panelDados.add(createFields(), BorderLayout.CENTER);
            panelDados.add(createButtonPanel(), BorderLayout.PAGE_END);

            add(panelDados, BorderLayout.PAGE_START);
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    private JPanel createFields() {
        JPanel panel = createPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        txtId = new GenJTextField(5, false);
        txtId.setEnabled(false);
        comboDiaDaSemana = new GenJComboBox(DiaDaSemana.values());

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 7);
        cal.set(Calendar.MINUTE, 0);
        Date firstDate = cal.getTime();
        spinHorario = new GenJSpinner(new SpinnerDateModel(firstDate,
                null, null, Calendar.DATE));
        spinHorario.setEditor(new JSpinner.DateEditor(spinHorario, "HH:mm"));

        int col = 0, row = 0;
        GridLayoutHelper.set(c, col++, row);
        panel.add(new GenJLabel("Id: "), c);
        GridLayoutHelper.set(c, col++, row);
        panel.add(new GenJLabel("Dia da Semana: "), c);
        GridLayoutHelper.set(c, col, row++);
        panel.add(new GenJLabel("Horario: "), c);

        col = 0;
        GridLayoutHelper.set(c, col++, row);
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(txtId, c);
        GridLayoutHelper.set(c, col++, row);
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(comboDiaDaSemana, c);
        GridLayoutHelper.set(c, col, row++);
        panel.add(spinHorario, c);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = createPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));

        btAdd = createButton(new ActionHandler(AcoesBotoes.ADD), backColor, foreColor);
        btUpdate = createButton(new ActionHandler(AcoesBotoes.EDIT), backColor, foreColor);
        btDel = createButton(new ActionHandler(AcoesBotoes.DELETE), backColor, foreColor);
        btNew = createButton(new ActionHandler(AcoesBotoes.NEW), backColor, foreColor);
        btGen = createButton(new ActionHandler(AcoesBotoes.GENERATE), backColor, foreColor);
        btGen.setText("Gerar Frequências");

        panel.add(btNew);
        panel.add(btAdd);
        panel.add(btUpdate);
        panel.add(btDel);
        panel.add(btGen);

        return panel;
    }

    private JPanel createTablePane() {
        horarioAulaTable = new JTable();
        ListSelectionModel cellSelectionModel = horarioAulaTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        horarioAulaTable.getSelectionModel().addListSelectionListener(new SelectionListener());

        setData(new ArrayList());

        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(horarioAulaTable);

        JPanel panel = createPanel(new BorderLayout(5, 5));
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(createFooter(), BorderLayout.PAGE_END);

        return panel;
    }

    private JPanel createFooter() {
        JPanel panel = createPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        lblStatus = new GenJLabel("Status");
        lblStatus.resetFontSize(12);
        lblStatus.setForeground(foreColor);
        panel.add(lblStatus);

        return panel;
    }

    private void updateStatus(Integer numeroDeAulas) {
        if (lblStatus != null) {
            Double carga = (numeroDeAulas.doubleValue() * 45) / 60;
            lblStatus.setText(String.format("Carga horária semanal: %.1fh | Número de aulas: %d",
                    carga, numeroDeAulas));
        }
    }

    private void setData(List<HorarioAula> data) {
        updateStatus(data.size());
        horarioAulaTableModel = new HorarioAulaTableModel(data);
        refreshHorarioAula();
    }

    private void refreshHorarioAula() {
        horarioAulaTable.setModel(horarioAulaTableModel);
        horarioAulaTable.getColumnModel().getColumn(0).setCellRenderer(new HorarioAulaCellRenderer());
        horarioAulaTable.repaint();
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = new HashMap();
        map.put("horarios", horarioAulaTableModel.getData());
        return map;
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {
        List<HorarioAula> listaHorarios = (List<HorarioAula>) mapValues.get("horarios");
        horarioAulaTableModel = new HorarioAulaTableModel(listaHorarios);
        refreshHorarioAula();
    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof PlanoDeEnsino) {
            planoDeEnsino = (PlanoDeEnsino) object;
            setData(planoDeEnsino.getHorarios());
            enableLocalButtons(Boolean.TRUE);
        }
    }

    @Override
    public boolean isValidated() {
        String msg = " ";
        if (comboDiaDaSemana.getSelectedItem() == null) {
            msg = "O campo Dia da Semana não foi selecionado.";
            comboDiaDaSemana.requestFocusInWindow();
        } else {
            return true;
        }
        showInformationMessage(msg);
        return false;
    }

    @Override
    public void clearFields() {
        clearLocalFields();
        setData(new ArrayList());
    }

    private void clearLocalFields() {
        txtId.setText("");
        comboDiaDaSemana.setSelectedItem(null);
        comboDiaDaSemana.repaint();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 7);
        cal.set(Calendar.MINUTE, 0);
        spinHorario.setValue(cal.getTime());
        horarioAulaTable.getSelectionModel().clearSelection();
    }

    @Override
    public void enableFields(boolean active) {
        comboDiaDaSemana.setEnabled(active);
        spinHorario.setEnabled(active);

        horarioAulaTable.setEnabled(active);
        enableLocalButtons(active);
    }

    private void enableLocalButtons(Boolean active) {
        Boolean status = "".equals(txtId.getText());

        btAdd.setEnabled(active && status);
        btNew.setEnabled(active && !status);
        btUpdate.setEnabled(active && !status);
        btDel.setEnabled(active && !status);
        btGen.setEnabled(active && !horarioAulaTableModel.isEmpty());
    }

    @Override
    public void initFocus() {
        comboDiaDaSemana.requestFocusInWindow();
    }

    private void clear() {
        clearLocalFields();
        enableLocalButtons(Boolean.TRUE);
        initFocus();
    }

    private HorarioAula createHorarioAulaFromFields() {
        String sId = txtId.getText();

        Calendar cal = Calendar.getInstance();
        Turno turno;
        cal.setTime((Date) spinHorario.getValue());
        int iHora = cal.get(Calendar.HOUR_OF_DAY);
        int iMinutos = cal.get(Calendar.MINUTE);
        if (iHora > 0 && iHora < 12) {
            turno = Turno.MATUTINO;
        } else if (iHora >= 12 && iHora < 18) {
            turno = Turno.VESPERTINO;
        } else {
            turno = Turno.NOTURNO;
        }
        String sHorario = String.format("%02d:%02d", iHora,
                iMinutos);

        HorarioAula o = HorarioAulaFactory.getInstance()
                .createObject(new HorarioAulaId(
                        Integer.parseInt(sId), planoDeEnsino),
                        comboDiaDaSemana.getSelectedItem(),
                        sHorario, turno);
        try {
            col.salvar(o);
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
        return o;
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
    public void onNewAction(ActionEvent e, Object o) {
        clear();
    }

    @Override
    public void onAddAction(ActionEvent e, Object o) {
        if (isValidated()) {
            int id = 1;
            if (!horarioAulaTableModel.isEmpty()) {
                /**
                 * Procedimento realizado para gerar a chave única de cada
                 * objetivo
                 */
                HorarioAula haTemp = horarioAulaTableModel.getMax(Comparator.comparing(a -> a.getId().getId()));
                id = haTemp.getId().getId() + 1;
            }

            txtId.setText(String.valueOf(id));
            horarioAulaTableModel.addRow(createHorarioAulaFromFields());
        }
    }

    @Override
    public void onEditAction(ActionEvent e, Object o) {
        int selectedRow = horarioAulaTable.getSelectedRow();
        horarioAulaTableModel.updateRow(selectedRow, createHorarioAulaFromFields());
    }

    @Override
    public void onDelAction(ActionEvent e, Object o) {
        int selectedRow = horarioAulaTable.getSelectedRow();
        try {
            if (selectedRow < 0) {
                showWarningMessage("Nenhum horário foi selecionado para remoção");
                return;
            }
            col.remover(horarioAulaTableModel.getRow(selectedRow));
            horarioAulaTableModel.removeRow(selectedRow);
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    @Override
    public void onGenarateAction(ActionEvent e) {
        try {
            DiarioController colDiario = ControllerFactory.createDiarioController();
            if (!planoDeEnsino.getDiarios().isEmpty()
                    && !confirmDialog("Já existem lançamentos de diários no sistema.\n"
                            + "Se confirmar, os dados já lançados serão perdidos.\n"
                            + "Caso contrário, os horários alterados não serão publicados.\n"
                            + "Deseja continuar?")) {
                return;
            }
            Iterator<Diario> it = planoDeEnsino.getDiarios().iterator();
            while (it.hasNext()) {
                Diario d = it.next();
                colDiario.remover(d);
            }

            planoDeEnsino.criarDiarios();
            it = planoDeEnsino.getDiarios().iterator();
            while (it.hasNext()) {
                Diario d = it.next();
                colDiario.salvar(d);
            }
            showInformationMessage("Diário criado com sucesso!");
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    public class SelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            int index = horarioAulaTable.getSelectedRow();
            if (horarioAulaTable.getRowSelectionAllowed()
                    && horarioAulaTable.isEnabled() && index >= 0) {
                HorarioAula horario = (HorarioAula) horarioAulaTableModel.getRow(index);
                txtId.setText(horario.getId().getId().toString());
                comboDiaDaSemana.setSelectedItem(horario.getDiaDaSemana());
                spinHorario.setValue(horario.getHorarioAsDate());
                enableLocalButtons(Boolean.TRUE);
                initFocus();
            }
        }

    }

}
