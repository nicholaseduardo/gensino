/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.config;

import ensino.components.GenJButton;
import ensino.components.GenJTextArea;
import ensino.components.renderer.TextAreaCellRenderer;
import ensino.configuracoes.model.ObjetivoUC;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.controller.ObjetivoController;
import ensino.planejamento.model.Objetivo;
import ensino.planejamento.model.ObjetivoFactory;
import ensino.planejamento.model.ObjetivoId;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.models.ObjetivoTableModel;
import ensino.reports.ChartsFactory;
import ensino.util.types.AcoesBotoes;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author nicho
 */
public class PlanoDeEnsinoObjetivoEspecifico extends DefaultFieldsPanel {

    private Integer sequencia;
    private GenJTextArea txtObjetivo;
    private GenJButton btAdd;
    private GenJButton btUpdate;
    private GenJButton btDel;
    private GenJButton btNew;
    private GenJButton btImport;
    private JTable objetivoTable;

    private ObjetivoTableModel objetivoTableModel;

    private PlanoDeEnsino planoDeEnsino;

    private Component frame;
    private ObjetivoController col;

    public PlanoDeEnsinoObjetivoEspecifico(Component frame) {
        super("Objetivos específicos");
        this.frame = frame;
        initComponents();
    }

    private void initComponents() {
        try {
            setName("plano.objetivos");
            setLayout(new BorderLayout(5, 5));
            setBorder(BorderFactory.createEtchedBorder());
            col = ControllerFactory.createObjetivoController();

            backColor = ChartsFactory.ligthGreen;
            foreColor = ChartsFactory.darkGreen;
            setBackground(backColor);

            GenJButton btClose = createButton(new ActionHandler(AcoesBotoes.CLOSE), backColor, foreColor);

            JPanel panelButton = createPanel(new FlowLayout(FlowLayout.RIGHT));
            panelButton.add(btClose);
            add(panelButton, BorderLayout.PAGE_END);

            add(createPanelEspecificos(), BorderLayout.PAGE_START);
            add(createPanelTabela(), BorderLayout.CENTER);
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    private JPanel createPanelEspecificos() {
        JPanel panel = createPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        txtObjetivo = new GenJTextArea(2, 50);
        JScrollPane objetivoScroll = new JScrollPane(txtObjetivo);
        objetivoScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        objetivoScroll.setBorder(createTitleBorder("Descrição do objetivo"));

        btAdd = createButton(new ActionHandler(AcoesBotoes.ADD), backColor, foreColor);
        btUpdate = createButton(new ActionHandler(AcoesBotoes.EDIT), backColor, foreColor);
        btDel = createButton(new ActionHandler(AcoesBotoes.DELETE), backColor, foreColor);
        btNew = createButton(new ActionHandler(AcoesBotoes.NEW), backColor, foreColor);
        btImport = createButton(new ActionHandler(AcoesBotoes.IMPORT), backColor, foreColor);
        btImport.setText("Importar Objetivos da U.C.");
        btImport.setToolTipText("Importar Objetivos da Unidade Curricular");

        JPanel panelButtons = createPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButtons.add(btNew);
        panelButtons.add(btAdd);
        panelButtons.add(btUpdate);
        panelButtons.add(btDel);
        panelButtons.add(btImport);

        GridLayoutHelper.set(c, 0, 0);
        panel.add(objetivoScroll, c);

        GridLayoutHelper.set(c, 0, 1);
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(panelButtons, c);

        return panel;
    }

    private JScrollPane createPanelTabela() {
        objetivoTable = new JTable();
        ListSelectionModel cellSelectionModel = objetivoTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cellSelectionModel.addListSelectionListener(new ObjetivoListSelectionListener());
        setData(new ArrayList());
        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(objetivoTable);

        return scroll;
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
        tcm.getColumn(0).setCellRenderer(new TextAreaCellRenderer());
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
            planoDeEnsino = (PlanoDeEnsino) object;
            sequencia = null;

            setData(planoDeEnsino.getObjetivos());

            enableLocalButtons(Boolean.TRUE);
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
        showInformationMessage(String.format(msg, campo));
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
        btImport.setEnabled(active && status);
        btNew.setEnabled(active && !status);
        btUpdate.setEnabled(active && !status);
        btDel.setEnabled(active && !status);
    }

    @Override
    public void initFocus() {
        txtObjetivo.requestFocusInWindow();
    }

    private void clear() {
        clearLocalFields();
        enableLocalButtons(Boolean.TRUE);
        initFocus();
    }

    private Objetivo createObjetivoFromFields() {

        Objetivo o = ObjetivoFactory.getInstance().createObject(
                sequencia, txtObjetivo.getText());
        o.getId().setPlanoDeEnsino(planoDeEnsino);
        try {
            /**
             * Grava os dados adicionados
             */
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
            clear();
        }
    }

    @Override
    public void onEditAction(ActionEvent e, Object o) {
        int selectedRow = objetivoTable.getSelectedRow();
        if (isValidated()) {
            objetivoTableModel.updateRow(selectedRow, createObjetivoFromFields());
        }
    }

    @Override
    public void onDelAction(ActionEvent e, Object o) {
        int selectedRow = objetivoTable.getSelectedRow();
        if (e.getSource() == btDel) {
            try {
                if (selectedRow == -1) {
                    showInformationMessage("Você não selecionou o Objetivo "
                            + "que será removido.\nFavor, clique sobre um Objetivo!");
                    return;
                }
                col.remover(objetivoTableModel.getRow(selectedRow));
                objetivoTableModel.removeRow(selectedRow);
                objetivoTable.repaint();
            } catch (Exception ex) {
                showErrorMessage(ex);
            }
        }
    }

    @Override
    public void onImportAction(ActionEvent e) {
        UnidadeCurricular uc = planoDeEnsino.getUnidadeCurricular();
        if (uc.getObjetivos().isEmpty()) {
            showWarningMessage("A U.C. vinculada ao plano de ensino não tem\n"
                    + "não tem objetivos cadastrados");
            return;
        }
        try {
            List<ObjetivoUC> lista = uc.getObjetivos();
            for (ObjetivoUC ouc : lista) {
                Objetivo obj = ObjetivoFactory.getInstance().createObject(
                        new ObjetivoId(null, planoDeEnsino),
                        ouc.getDescricao(), ouc);
                col.salvar(obj);
                objetivoTableModel.addRow(obj);
            }
            clear();
        } catch (Exception ex) {
            showErrorMessage(ex);
            ex.printStackTrace();
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
