/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.planoDeEnsino;

import ensino.components.GenJButton;
import ensino.components.GenJTextArea;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.planejamento.model.Objetivo;
import ensino.planejamento.model.ObjetivoFactory;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.models.ObjetivoTableModel;
import ensino.planejamento.view.renderer.ObjetivoCellRenderer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author nicho
 */
public class ObjetivoEspecificoPanel extends DefaultFieldsPanel {

    private Integer sequencia;
    private GenJTextArea txtObjetivo;
    private GenJButton btAdd;
    private GenJButton btUpdate;
    private GenJButton btDel;
    private GenJButton btNew;
    private JTable objetivoTable;
    private ObjetivoTableModel objetivoTableModel;

    public ObjetivoEspecificoPanel() {
        super("Objetivos específicos");
        initComponents();
    }

    private void initComponents() {
        setName("plano.objetivos");
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        setBorder(BorderFactory.createEtchedBorder());

        add(panelEspecificos());
    }

    private JPanel panelEspecificos() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JPanel panelObjetivo = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panelObjetivo.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Descrição do objetivo",
                TitledBorder.LEFT, TitledBorder.TOP));

        GridLayoutHelper.set(c, 0, 0);
        panel.add(panelObjetivo, c);
        txtObjetivo = new GenJTextArea(2, 50);
        JScrollPane objetivoScroll = new JScrollPane(txtObjetivo);
        objetivoScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panelObjetivo.add(objetivoScroll);

        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        GridLayoutHelper.set(c, 0, 1);
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(panelButtons, c);
        String source = String.format("/img/%s", "add-icon-png-25px.png");
        btAdd = new GenJButton("Adicionar", new ImageIcon(getClass().getResource(source)));

        source = String.format("/img/%s", "update-button-25px.png");
        btUpdate = new GenJButton("Alterar", new ImageIcon(getClass().getResource(source)));

        source = String.format("/img/%s", "del-button-png-25px.png");
        btDel = new GenJButton("Remover", new ImageIcon(getClass().getResource(source)));

        source = String.format("/img/%s", "view-button-25px.png");
        btNew = new GenJButton("Novo", new ImageIcon(getClass().getResource(source)));

        ObjetivoActionListener atividadeListener = new ObjetivoActionListener();
        btAdd.addActionListener(atividadeListener);
        btUpdate.addActionListener(atividadeListener);
        btDel.addActionListener(atividadeListener);
        btNew.addActionListener(atividadeListener);
        panelButtons.add(btNew);
        panelButtons.add(btAdd);
        panelButtons.add(btUpdate);
        panelButtons.add(btDel);

        objetivoTable = new JTable();
        ListSelectionModel cellSelectionModel = objetivoTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cellSelectionModel.addListSelectionListener(new ObjetivoListSelectionListener());
        setData(new ArrayList());
        JScrollPane objetivoEspecificoScroll = new JScrollPane();
        objetivoEspecificoScroll.setViewportView(objetivoTable);
        objetivoEspecificoScroll.setPreferredSize(new Dimension(700, 200));
        GridLayoutHelper.set(c, 0, 3);
        panel.add(objetivoEspecificoScroll, c);

        return panel;
    }

    /**
     * Recupera a lista de atividades atualizada (adicionadas/removidas)
     *
     * @return
     */
    public List<Objetivo> getData() {
        return objetivoTableModel.getData();
    }

    /**
     * Inicializa a tabela de dados de atividades
     *
     * @param data
     */
    public void setData(List<Objetivo> data) {
        objetivoTableModel = new ObjetivoTableModel(data);
        refreshTable();
    }

    private void refreshTable() {
        objetivoTable.setModel(objetivoTableModel);

        TableColumnModel tcm = objetivoTable.getColumnModel();
        tcm.getColumn(0).setCellRenderer(new ObjetivoCellRenderer());
        objetivoTable.repaint();
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = new HashMap();
        map.put("objetivos", objetivoTableModel.getData());
        return map;
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {

    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof PlanoDeEnsino) {
            PlanoDeEnsino plano = (PlanoDeEnsino) object;
            setData(plano.getObjetivos());
        }
    }

    @Override
    public boolean isValidated() {
        boolean result = false;
        String msg = "O campo %s não foi informado!", campo = "";
        if ("".equals(txtObjetivo.getText())) {
            campo = "Descrição do objetivo";
            txtObjetivo.requestFocusInWindow();
        } else {
            return true;
        }
        JOptionPane.showMessageDialog(getParent(), String.format(msg, campo),
                "Aviso", JOptionPane.WARNING_MESSAGE);
        return result;
    }

    @Override
    public void clearFields() {
        clearLocalFields();
        setData(new ArrayList());
    }

    private void clearLocalFields() {
        sequencia = null;
        txtObjetivo.setText("");
    }

    private void setFieldValues(Integer sequencia, String descricao) {
        this.sequencia = sequencia;
        txtObjetivo.setText(descricao);
    }

    @Override
    public void enableFields(boolean active) {
        txtObjetivo.setEnabled(active);
        objetivoTable.setEnabled(active);
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
        txtObjetivo.requestFocusInWindow();
    }

    private class ObjetivoActionListener implements ActionListener {

        private void clear() {
            clearLocalFields();
            enableLocalButtons(Boolean.TRUE);
            initFocus();
        }

        private Objetivo createObjetivoFromFields() {
            Objetivo at = ObjetivoFactory.getInstance().createObject(
                    sequencia, txtObjetivo.getText());
            return at;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == btUpdate && isValidated()) {
                int selectedRow = objetivoTable.getSelectedRow();
                objetivoTableModel.updateRow(selectedRow, createObjetivoFromFields());
            } else if (source == btAdd && isValidated()) {
                int id = 1;
                if (!objetivoTableModel.isEmpty()) {
                    /**
                     * Procedimento realizado para gerar a chave única de cada
                     * objetivo
                     */
                    Objetivo otemp = objetivoTableModel.getMax(Comparator.comparing(a -> a.getId().getSequencia()));
                    id = otemp.getId().getSequencia() + 1;
                }
                /**
                 * atribui o valor do ID ao campo para reaproveitar o método de
                 * criação do objeto Atividade
                 */
                sequencia = id;
                objetivoTableModel.addRow(createObjetivoFromFields());
            } else if (e.getSource() == btDel) {
                int selectedRow = objetivoTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(getParent(),
                            "Você não selecionou o Objetivo que será removido.\nFavor, clique sobre um Objetivo!",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                objetivoTableModel.removeRow(selectedRow);
                objetivoTable.repaint();
            }
            clear();
        }
    }

    private class ObjetivoListSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            int selectedRow = objetivoTable.getSelectedRow();
            if (selectedRow >= 0) {
                Objetivo o = (Objetivo) objetivoTableModel.getRow(selectedRow);
                setFieldValues(o.getId().getSequencia(), o.getDescricao());
                enableLocalButtons(Boolean.TRUE);
                initFocus();
            }
        }
    }

}
