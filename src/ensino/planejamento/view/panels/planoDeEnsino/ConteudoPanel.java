/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.planoDeEnsino;

import ensino.components.GenJButton;
import ensino.components.GenJComboBox;
import ensino.components.GenJFormattedTextField;
import ensino.components.GenJLabel;
import ensino.components.GenJSpinner;
import ensino.components.GenJTextField;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.planejamento.model.Diario;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.models.DiarioTableModel;
import ensino.planejamento.view.renderer.DiarioCellRenderer;
import ensino.util.TipoAula;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Calendar;
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
import javax.swing.text.MaskFormatter;

/**
 *
 * @author nicho
 */
public class ConteudoPanel extends DefaultFieldsPanel {

    private GenJTextField txtConteudo;
    private GenJComboBox comboTipoAula;
    private GenJFormattedTextField txtHorario;
    private GenJSpinner spinData;

    private GenJButton btAdd;
    private GenJButton btDel;
    private GenJButton btClear;

    private JTable conteudoTable;
    private DiarioTableModel conteudoTableModel;

    public ConteudoPanel() {
        super("Conteúdo programático");
        initComponents();
    }

    private void initComponents() {
        setName("conteudo.programatico");
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
        try {
            GridBagConstraints c = new GridBagConstraints();
            comboTipoAula = new GenJComboBox(TipoAula.values());
            txtConteudo = new GenJTextField(40);
            Calendar cal = Calendar.getInstance();
            spinData = new GenJSpinner(new SpinnerDateModel(cal.getTime(),
                    null, null, Calendar.DATE));
            spinData.setEditor(new JSpinner.DateEditor(spinData, "dd/MM/yyyy"));
            
            MaskFormatter fHorario = new MaskFormatter("##:##");
            fHorario.setValidCharacters("0123456789");
            txtHorario = new GenJFormattedTextField(fHorario);
            txtHorario.setColumns(4);
            
            int col = 0, row = 0;
            GridLayoutHelper.set(c, col++, row);
            panel.add(new GenJLabel("Data: "), c);
            GridLayoutHelper.set(c, col++, row);
            panel.add(new GenJLabel("Horário: "), c);
            GridLayoutHelper.set(c, col, row++);
            panel.add(new GenJLabel("Tipo: "), c);
            col = 0;
            GridLayoutHelper.set(c, col++, row);
            panel.add(spinData, c);
            GridLayoutHelper.set(c, col++, row);
            c.fill = GridBagConstraints.HORIZONTAL;
            panel.add(txtHorario, c);
            GridLayoutHelper.set(c, col++, row++);
            c.fill = GridBagConstraints.HORIZONTAL;
            panel.add(comboTipoAula, c);
            col = 0;
            GridLayoutHelper.set(c, col, row++, 3, 1, GridBagConstraints.LINE_START);
            panel.add(new GenJLabel("Conteúdo ministrado: "), c);
            GridLayoutHelper.set(c, col, row++, 3, 1, GridBagConstraints.LINE_START);
            panel.add(txtConteudo, c);
        } catch (ParseException ex) {
            Logger.getLogger(ConteudoPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));

        btAdd = new GenJButton("Atualizar", new ImageIcon(getClass().getResource(getImageSourceAdd())));
        btDel = new GenJButton("Remover", new ImageIcon(getClass().getResource(getImageSourceDel())));
        btClear = new GenJButton("Limpar", new ImageIcon(getClass().getResource(getImageSourceClear())));

        panel.add(btClear);
        panel.add(btDel);
        panel.add(btAdd);

        ButtonAction buttonAction = new ButtonAction();
        btClear.addActionListener(buttonAction);
        btAdd.addActionListener(buttonAction);
        btDel.addActionListener(buttonAction);

        return panel;
    }

    private JPanel createTablePane() {
        JPanel panel = new JPanel();
        conteudoTable = new JTable();
        ListSelectionModel cellSelectionModel = conteudoTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        conteudoTable.getSelectionModel().addListSelectionListener(new SelectionListener());

        conteudoTableModel = new DiarioTableModel();
        refreshDiario();

        JScrollPane planoAvaliacaoScroll = new JScrollPane();
        planoAvaliacaoScroll.setViewportView(conteudoTable);
        planoAvaliacaoScroll.setPreferredSize(new Dimension(700, 200));
        panel.add(planoAvaliacaoScroll);
        return panel;
    }

    private void refreshDiario() {
        conteudoTable.setModel(conteudoTableModel);
        conteudoTable.getColumnModel().getColumn(0).setCellRenderer(new DiarioCellRenderer());
        conteudoTable.repaint();
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = new HashMap();
        map.put("conteudo", conteudoTableModel.getData());
        return map;
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {
        List<Diario> listDiarios = (List<Diario>) mapValues.get("diarios");
        conteudoTableModel = new DiarioTableModel(listDiarios);
        refreshDiario();
    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof PlanoDeEnsino) {
            PlanoDeEnsino planoDeEnsino = (PlanoDeEnsino) object;
            conteudoTableModel = new DiarioTableModel(planoDeEnsino.getDiarios());
            refreshDiario();
        }
    }

    @Override
    public boolean isValidated() {
        String msg = " ";
        if (spinData.getValue() == null) {
            msg = "O campo DATA não foi informada.";
            spinData.requestFocusInWindow();
        } else if (comboTipoAula.getSelectedItem() == null) {
            msg = "O campo TIPO não foi selecionado.";
            comboTipoAula.requestFocusInWindow();
        } else if ("".equals(txtConteudo.getText())) {
            msg = "O campo CONTEÚDO não foi preenchido.";
            txtConteudo.requestFocusInWindow();
        } else if (txtHorario.getValue() == null) {
            msg = "O horário não foi selecionado.";
            txtHorario.requestFocusInWindow();
        } else {
            return true;
        }
        JOptionPane.showMessageDialog(this, msg, "Informação",
                JOptionPane.INFORMATION_MESSAGE);
        return false;
    }

    @Override
    public void clearFields() {
        comboTipoAula.setSelectedItem(null);
        comboTipoAula.repaint();
        txtConteudo.setText("");
        txtHorario.setValue(null);
        txtHorario.repaint();
        Calendar cal = Calendar.getInstance();
        spinData.setValue(cal.getTime());
    }

    @Override
    public void enableFields(boolean active) {
        comboTipoAula.setEnabled(active);
        txtConteudo.setEnabled(active);
        txtHorario.setEnabled(active);
        spinData.setEnabled(active);

        btAdd.setEnabled(active);
        btDel.setEnabled(active);

        btClear.setEnabled(active);

        conteudoTable.setEnabled(active);
    }

    @Override
    public void initFocus() {
        comboTipoAula.requestFocusInWindow();
    }

    private class ButtonAction implements ActionListener {

        private int id;

        public ButtonAction() {
            Diario diario = (Diario) conteudoTableModel.getLast();
            if (diario != null) {
                id = diario.getId() + 1;
            } else {
                id = 1;
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();

            int selectedRow = conteudoTable.getSelectedRow();
            if (source == btClear) {
                clearFields();
            } else if (source == btAdd) {
                if (isValidated()) {
                    Diario diario = new Diario();

                    diario.setConteudo(txtConteudo.getText());
                    diario.setHorario((String) txtHorario.getValue());
                    diario.setData((Date) spinData.getValue());
                    diario.setTipoAula((TipoAula) comboTipoAula.getSelectedItem());

                    if (selectedRow < 0) {
                        // cria um novo plano
                        diario.setId(id++);
                        conteudoTableModel.addRow(diario);
                    } else {
                        // atualiza o plano existente
                        Diario old = (Diario) conteudoTableModel.getRow(selectedRow);
                        diario.setId(old.getId());
                        conteudoTableModel.updateRow(selectedRow, diario);
                    }
                    clearFields();
                }
            } else if (source == btDel) {
                if (selectedRow < 0) {
                    JOptionPane.showMessageDialog(conteudoTable,
                            "Nenhuma avaliação foi selecionada para remoção",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                conteudoTableModel.removeRow(selectedRow);
            }
        }

    }

    private class SelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (conteudoTable.getRowSelectionAllowed()
                    && conteudoTable.isEnabled()) {

                int index = conteudoTable.getSelectedRow();
                Diario diario = (Diario) conteudoTableModel.getRow(index);

                txtConteudo.setText(diario.getConteudo());
                txtHorario.setValue(diario.getHorario());
                txtHorario.repaint();

                spinData.setValue(diario.getData());
                comboTipoAula.setSelectedItem(diario.getTipoAula());
                comboTipoAula.repaint();

                refreshDiario();
            }
        }

    }

}
