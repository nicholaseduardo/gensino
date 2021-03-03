/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.planoDeEnsino;

import ensino.components.GenJButton;
import ensino.components.GenJComboBox;
import ensino.components.GenJLabel;
import ensino.components.GenJSpinner;
import ensino.components.GenJTextField;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.planejamento.model.HorarioAula;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.models.HorarioAulaTableModel;
import ensino.planejamento.view.renderer.HorarioAulaCellRenderer;
import ensino.util.types.DiaDaSemana;
import ensino.util.types.Turno;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
public class HorarioAulaPanel extends DefaultFieldsPanel {

    private GenJTextField txtId;
    private GenJComboBox comboDiaDaSemana;
    private GenJSpinner spinHorario;

    private GenJButton btAdd;
    private GenJButton btDel;

    private JTable horarioAulaTable;
    private HorarioAulaTableModel horarioAulaTableModel;

    public HorarioAulaPanel() {
        super("Horário das Aulas");
        initComponents();
    }

    private void initComponents() {
        setName("horario.aulas");
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        /**
         * A tabela foi criada antes por questões de processo lógico da
         * construção da ação dos botões.
         */
        panel.add(createTablePane(), BorderLayout.CENTER);

        JPanel panelDados = new JPanel(new BorderLayout(5, 5));
        panelDados.add(createFields(), BorderLayout.CENTER);
        panelDados.add(createButtonPanel(), BorderLayout.PAGE_END);

        panel.add(panelDados, BorderLayout.PAGE_START);
        add(panel);
    }

    private JPanel createFields() {
        JPanel panel = new JPanel(new GridBagLayout());
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
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));

        btAdd = new GenJButton("Adicionar", new ImageIcon(getClass().getResource(getImageSourceAdd())));
        btDel = new GenJButton("Remover", new ImageIcon(getClass().getResource(getImageSourceDel())));
        panel.add(btDel);
        panel.add(btAdd);

        ButtonAction buttonAction = new ButtonAction();
        btAdd.addActionListener(buttonAction);
        btDel.addActionListener(buttonAction);

        return panel;
    }

    private JPanel createTablePane() {
        JPanel panel = new JPanel();
        horarioAulaTable = new JTable();
        ListSelectionModel cellSelectionModel = horarioAulaTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        horarioAulaTable.getSelectionModel().addListSelectionListener(new SelectionListener());

        horarioAulaTableModel = new HorarioAulaTableModel();
        refreshHorarioAula();

        JScrollPane planoAvaliacaoScroll = new JScrollPane();
        planoAvaliacaoScroll.setViewportView(horarioAulaTable);
        planoAvaliacaoScroll.setPreferredSize(new Dimension(500, 200));
        panel.add(planoAvaliacaoScroll);
        return panel;
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
            PlanoDeEnsino planoDeEnsino = (PlanoDeEnsino) object;
            horarioAulaTableModel = new HorarioAulaTableModel(planoDeEnsino.getHorarios());
            refreshHorarioAula();
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
        JOptionPane.showMessageDialog(this, msg, "Informação", JOptionPane.INFORMATION_MESSAGE);
        return false;
    }

    @Override
    public void clearFields() {
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

        btAdd.setEnabled(active);
        btDel.setEnabled(active);

        horarioAulaTable.setEnabled(active);
    }

    @Override
    public void initFocus() {
        comboDiaDaSemana.requestFocusInWindow();
    }

    private class ButtonAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();

            int selectedRow = horarioAulaTable.getSelectedRow();
            HorarioAula temp = horarioAulaTableModel.getRow(selectedRow);
            if (source == btAdd) {
                if (isValidated()) {
                    HorarioAula horarioAula = new HorarioAula();
                    String sId = txtId.getText();
                    if (sId.matches("\\d")) {
                        horarioAula.getId().setId(Integer.parseInt(sId));
                    }

                    Calendar cal = Calendar.getInstance();
                    cal.setTime((Date) spinHorario.getValue());
                    int iHora = cal.get(Calendar.HOUR_OF_DAY);
                    int iMinutos = cal.get(Calendar.MINUTE);
                    if (iHora > 0 && iHora < 12) {
                        horarioAula.setTurno(Turno.MATUTINO);
                    } else if (iHora >= 12 && iHora < 18) {
                        horarioAula.setTurno(Turno.VESPERTINO);
                    } else {
                        horarioAula.setTurno(Turno.NOTURNO);
                    }
                    String sHorario = String.format("%02d:%02d", iHora,
                            iMinutos);
                    horarioAula.setHorario(sHorario);
                    horarioAula.setDiaDaSemana((DiaDaSemana) comboDiaDaSemana.getSelectedItem());
                    
                    /**
                     * Se o horário já existir, ele deve ser atualizado
                     */
                    if (selectedRow < 0 || temp.getId().getId() == null) {
                        // cria um novo horário de aula
                        horarioAulaTableModel.addRow(horarioAula);
                    } else {
                        // atualiza o plano existente
                        horarioAulaTableModel.updateRow(selectedRow, horarioAula);
                    }

                    clearFields();
                }
            } else if (source == btDel) {
                if (selectedRow < 0) {
                    JOptionPane.showMessageDialog(horarioAulaTable,
                            "Nenhum horário foi selecionado para remoção",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                horarioAulaTableModel.removeRow(selectedRow);
            }
        }

    }
    
    public class SelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            int index = horarioAulaTable.getSelectedRow();
            if (horarioAulaTable.getRowSelectionAllowed() &&
                    horarioAulaTable.isEnabled() && index >= 0) {
                HorarioAula horario = (HorarioAula) horarioAulaTableModel.getRow(index);
                txtId.setText(horario.getId().toString());
                comboDiaDaSemana.setSelectedItem(horario.getDiaDaSemana());
                spinHorario.setValue(horario.getHorarioAsDate());
            }
        }
        
    }

}
