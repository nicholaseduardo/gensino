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
import ensino.planejamento.controller.HorarioAulaController;
import ensino.planejamento.model.HorarioAula;
import ensino.planejamento.model.HorarioAulaFactory;
import ensino.planejamento.model.HorarioAulaId;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.models.HorarioAulaTableModel;
import ensino.planejamento.view.renderer.HorarioAulaCellRenderer;
import ensino.util.types.Turno;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author nicho
 */
public class PlanoDeEnsinoHorarioAulaPanel extends DefaultFieldsPanel {

    private PlanoDeEnsino planoDeEnsino;
    private GenJTextField txtId;
    private GenJComboBox comboDiaDaSemana;
    private GenJSpinner spinHorario;

    private GenJButton btAdd;
    private GenJButton btDel;
    private GenJButton btNew;
    private GenJButton btUpdate;

    private JTable horarioAulaTable;
    private HorarioAulaTableModel horarioAulaTableModel;

    public PlanoDeEnsinoHorarioAulaPanel() {
        super("Horário das Aulas");
        initComponents();
    }

    private void initComponents() {
        setName("horario.aulas");
        setLayout(new BorderLayout(5, 5));
        /**
         * A tabela foi criada antes por questões de processo lógico da
         * construção da ação dos botões.
         */
        add(createTablePane(), BorderLayout.CENTER);

        JPanel panelDados = new JPanel(new BorderLayout(5, 5));
        panelDados.add(createFields(), BorderLayout.CENTER);
        panelDados.add(createButtonPanel(), BorderLayout.PAGE_END);

        add(panelDados, BorderLayout.PAGE_START);
    }

    private JPanel createFields() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        txtId = new GenJTextField(5, false);
        txtId.setEnabled(false);
        comboDiaDaSemana = new GenJComboBox(DayOfWeek.values());

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
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));

        btAdd = new GenJButton("Adicionar", new ImageIcon(getClass().getResource(getImageSourceAdd())));
        btDel = new GenJButton("Remover", new ImageIcon(getClass().getResource(getImageSourceDel())));
        btNew = new GenJButton("Novo", new ImageIcon(getClass().getResource(getImageSourceNew())));
        btUpdate = new GenJButton("Atualizar", new ImageIcon(getClass().getResource(getImageSourceUpdate())));
        panel.add(btNew);
        panel.add(btAdd);
        panel.add(btUpdate);
        panel.add(btDel);

        ButtonAction buttonAction = new ButtonAction();
        btAdd.addActionListener(buttonAction);
        btNew.addActionListener(buttonAction);
        btUpdate.addActionListener(buttonAction);
        btDel.addActionListener(buttonAction);

        return panel;
    }

    private JScrollPane createTablePane() {
        horarioAulaTable = new JTable();
        ListSelectionModel cellSelectionModel = horarioAulaTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        horarioAulaTable.getSelectionModel().addListSelectionListener(new SelectionListener());

        setData(new ArrayList());

        JScrollPane planoAvaliacaoScroll = new JScrollPane();
        planoAvaliacaoScroll.setViewportView(horarioAulaTable);

        return planoAvaliacaoScroll;
    }

    private void setData(List<HorarioAula> data) {
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
    }

    @Override
    public void initFocus() {
        comboDiaDaSemana.requestFocusInWindow();
    }

    private class ButtonAction implements ActionListener {

        private HorarioAulaController col;

        public ButtonAction() {
            try {
                col = ControllerFactory.createHorarioAulaController();
            } catch (Exception ex) {
                Logger.getLogger(PlanoDeEnsinoHorarioAulaPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
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
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();

            int selectedRow = horarioAulaTable.getSelectedRow();
            if (source == btUpdate) {
                horarioAulaTableModel.updateRow(selectedRow, createHorarioAulaFromFields());
            } else if (source == btAdd && isValidated()) {
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
            } else if (source == btDel) {
                try {
                    if (selectedRow < 0) {
                        showWarningMessage("Nenhum horário foi selecionado para remoção");
                        return;
                    }
                    HorarioAula o = horarioAulaTableModel.getRow(selectedRow);
                    col.remover(o);
                    horarioAulaTableModel.removeRow(selectedRow);
                } catch (Exception ex) {
                    showErrorMessage(ex);
                }
            }
            clear();
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
