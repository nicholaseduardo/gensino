/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.config;

import ensino.components.GenJButton;
import ensino.components.GenJComboBox;
import ensino.components.GenJFormattedTextField;
import ensino.components.GenJLabel;
import ensino.components.GenJSpinner;
import ensino.components.GenJTextArea;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.controller.DiarioController;
import ensino.planejamento.model.Diario;
import ensino.planejamento.model.DiarioFactory;
import ensino.planejamento.model.DiarioId;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.models.DiarioTableModel;
import ensino.planejamento.view.renderer.DiarioCellRenderer;
import ensino.util.types.TipoAula;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
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
public class PlanoDeEnsinoConteudoPanel extends DefaultFieldsPanel {

    private PlanoDeEnsino planoDeEnsino;
    private Integer sequencia;
    private GenJTextArea txtConteudo;
    private GenJTextArea txtObservacao;
    private GenJComboBox comboTipoAula;
    private GenJFormattedTextField txtHorario;
    private GenJSpinner spinData;

    private GenJButton btAdd;
    private GenJButton btUpdate;
    private GenJButton btDel;
    private GenJButton btNew;

    private JTable conteudoTable;
    private DiarioTableModel conteudoTableModel;

    public PlanoDeEnsinoConteudoPanel() {
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
            txtConteudo = new GenJTextArea(3, 50);
            txtObservacao = new GenJTextArea(3, 50);
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
            JScrollPane conteudoScroll = new JScrollPane(txtConteudo);
            conteudoScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            panel.add(conteudoScroll, c);

            col = 0;
            GridLayoutHelper.set(c, col, row++, 3, 1, GridBagConstraints.LINE_START);
            panel.add(new GenJLabel("Observação: "), c);
            GridLayoutHelper.set(c, col, row++, 3, 1, GridBagConstraints.LINE_START);
            panel.add(txtObservacao, c);
            JScrollPane observacaoScroll = new JScrollPane(txtObservacao);
            observacaoScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            panel.add(observacaoScroll, c);
        } catch (ParseException ex) {
            Logger.getLogger(PlanoDeEnsinoConteudoPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));

        btAdd = new GenJButton("Adicionar", new ImageIcon(getClass().getResource(getImageSourceAdd())));
        btUpdate = new GenJButton("Alterar", new ImageIcon(getClass().getResource(getImageSourceUpdate())));
        btDel = new GenJButton("Remover", new ImageIcon(getClass().getResource(getImageSourceDel())));
        btNew = new GenJButton("Novo", new ImageIcon(getClass().getResource(getImageSourceNew())));

        panel.add(btNew);
        panel.add(btAdd);
        panel.add(btUpdate);
        panel.add(btDel);

        ButtonAction buttonAction = new ButtonAction();
        btNew.addActionListener(buttonAction);
        btUpdate.addActionListener(buttonAction);
        btAdd.addActionListener(buttonAction);
        btDel.addActionListener(buttonAction);

        return panel;
    }

    private JScrollPane createTablePane() {

        conteudoTable = new JTable();
        ListSelectionModel cellSelectionModel = conteudoTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cellSelectionModel.addListSelectionListener(new SelectionListener());

        setData(new ArrayList());

        JScrollPane planoAvaliacaoScroll = new JScrollPane();
        planoAvaliacaoScroll.setViewportView(conteudoTable);
//        planoAvaliacaoScroll.setPreferredSize(new Dimension(700, 200));
        return planoAvaliacaoScroll;
    }

    public void setData(List<Diario> data) {
        conteudoTableModel = new DiarioTableModel(data);
        refreshDiario();
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
            planoDeEnsino = (PlanoDeEnsino) object;
            sequencia = null;
            List l = Arrays.asList(planoDeEnsino.getDiarios().toArray());
            setData(l);
            enableLocalButtons(Boolean.TRUE);
        }
    }

    @Override
    public boolean isValidated() {
        String msg = "O campo %s não foi informado!", campo = "";
        if (spinData.getValue() == null) {
            campo = "DATA";
            spinData.requestFocusInWindow();
        } else if (comboTipoAula.getSelectedItem() == null) {
            campo = "TIPO";
            comboTipoAula.requestFocusInWindow();
        } else if ("".equals(txtConteudo.getText())) {
            campo = "CONTEUDO";
            txtConteudo.requestFocusInWindow();
        } else if (txtHorario.getValue() == null) {
            campo = "HORÁRIO";
            txtHorario.requestFocusInWindow();
        } else {
            return true;
        }
        showInformationMessage(String.format(msg, campo));
        return false;
    }

    @Override
    public void clearFields() {
        clearLocalFields();
        setData(new ArrayList());
    }

    private void clearLocalFields() {
        sequencia = null;
        comboTipoAula.setSelectedItem(null);
        comboTipoAula.repaint();
        txtConteudo.setText("");
        txtObservacao.setText("");
        txtHorario.setValue(null);
        txtHorario.repaint();
        Calendar cal = Calendar.getInstance();
        spinData.setValue(cal.getTime());
    }

    @Override
    public void enableFields(boolean active) {
        comboTipoAula.setEnabled(active);
        txtConteudo.setEnabled(active);
        txtObservacao.setEnabled(active);
        txtHorario.setEnabled(active);
        spinData.setEnabled(active);

        conteudoTable.setEnabled(active);
        enableLocalButtons(active);
    }

    private void enableLocalButtons(Boolean active) {
        Boolean status = sequencia == null;

        btAdd.setEnabled(active && status);
        btNew.setEnabled(active && !status);
        btUpdate.setEnabled(active && !status);
        btDel.setEnabled(active && !status);
    }

    @Override
    public void initFocus() {
        comboTipoAula.requestFocusInWindow();
    }

    private class ButtonAction implements ActionListener {

        private DiarioController col;

        public ButtonAction() {
            try {
                col = ControllerFactory.createDiarioController();
            } catch (Exception ex) {
                Logger.getLogger(PlanoDeEnsinoConteudoPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private void clear() {
            clearLocalFields();
            enableLocalButtons(Boolean.TRUE);
            initFocus();
        }

        private Diario createDiarioFromFields() {
            Diario o = DiarioFactory.getInstance()
                    .createObject(
                            new DiarioId(sequencia, planoDeEnsino),
                            spinData.getValue(),
                            txtHorario.getText(),
                            txtObservacao.getText(),
                            txtConteudo.getText(),
                            comboTipoAula.getSelectedItem()
                    );
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

            int selectedRow = conteudoTable.getSelectedRow();
            if (source == btUpdate && isValidated()) {
                conteudoTableModel.updateRow(selectedRow, createDiarioFromFields());
            } else if (source == btAdd && isValidated()) {
                if (TipoAula.NORMAL.equals(comboTipoAula.getSelectedItem())) {
                    showInformationMessage("Não é permitido adicionar Horário do TIPO NORMAL.");
                } else {
                    int id = 1;
                    if (!conteudoTableModel.isEmpty()) {
                        /**
                         * Procedimento realizado para gerar a chave única de
                         * cada objetivo
                         */
                        Diario otemp = conteudoTableModel.getMax(Comparator.comparing(a -> a.getId().getId()));
                        id = otemp.getId().getId() + 1;
                    }
                    /**
                     * atribui o valor do ID ao campo para reaproveitar o método
                     * de criação do objeto Diario
                     */
                    sequencia = id;
                    conteudoTableModel.addRow(createDiarioFromFields());
                }
            } else if (source == btDel) {
                try {
                    if (selectedRow < 0) {
                        showInformationMessage("Nenhum conteúdo foi selecionada para remoção");
                        return;
                    }
                    Diario d = conteudoTableModel.getRow(selectedRow);
                    if (d.getTipoAula().equals(TipoAula.NORMAL)) {
                        showWarningMessage("Não é possível remover aula do tipo NORMAL.");
                    } else {
                        col.remover(d);
                        conteudoTableModel.removeRow(selectedRow);
                    }
                } catch (Exception ex) {
                    showErrorMessage(ex);
                }
            }
        }

    }

    private class SelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (conteudoTable.getRowSelectionAllowed()
                    && conteudoTable.isEnabled()) {

                int index = conteudoTable.getSelectedRow();
                if (index >= 0) {
                    Diario diario = (Diario) conteudoTableModel.getRow(index);
                    sequencia = diario.getId().getId();
                    txtConteudo.setText(diario.getConteudo());
                    txtObservacao.setText(diario.getObservacoes());
                    txtHorario.setValue(diario.getHorario());
                    txtHorario.repaint();

                    spinData.setValue(diario.getData());
                    comboTipoAula.setSelectedItem(diario.getTipoAula());
                    comboTipoAula.repaint();

                    enableLocalButtons(Boolean.TRUE);
                    initFocus();
                }
            }
        }

    }

}
