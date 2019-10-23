/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.planoDeEnsino;

import ensino.components.GenJButton;
import ensino.components.GenJTextArea;
import ensino.components.renderer.TextAreaCellRenderer;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.planejamento.model.Objetivo;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.models.ObjetivoTableModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author nicho
 */
public class ObjetivoEspecificoPanel extends DefaultFieldsPanel {

    private GenJTextArea txtObjetivo;
    private GenJButton btAdd;
    private GenJButton btDel;
    private GenJButton btClear;
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

        source = String.format("/img/%s", "del-button-png-25px.png");
        btDel = new GenJButton("Remover", new ImageIcon(getClass().getResource(source)));

        source = String.format("/img/%s", "clear-icon-25px.png");
        btClear = new GenJButton("Limpar", new ImageIcon(getClass().getResource(source)));

        ObjetivoActionListener atividadeListener = new ObjetivoActionListener();
        btAdd.addActionListener(atividadeListener);
        btDel.addActionListener(atividadeListener);
        btClear.addActionListener(atividadeListener);
        panelButtons.add(btAdd);
        panelButtons.add(btDel);
        panelButtons.add(btClear);

        objetivoTable = new JTable();
        ListSelectionModel cellSelectionModel = objetivoTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
        objetivoTable.setModel(objetivoTableModel);
        resizeTableColumns();
    }

    private void resizeTableColumns() {
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
            PlanoDeEnsino plano = (PlanoDeEnsino) object;
            setData(plano.getObjetivos());
        }
    }

    @Override
    public boolean isValidated() {
        return true;
    }

    @Override
    public void clearFields() {
        txtObjetivo.setText("");
        setData(new ArrayList());
    }

    @Override
    public void enableFields(boolean active) {
        txtObjetivo.setEnabled(active);
        btAdd.setEnabled(active);
        btDel.setEnabled(active);
        btClear.setEnabled(active);
    }

    @Override
    public void initFocus() {
        txtObjetivo.requestFocusInWindow();
    }

    private class ObjetivoActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btAdd) {
                if (!"".equals(txtObjetivo.getText())) {
                    Objetivo objetivo = new Objetivo();
                    objetivo.setDescricao(txtObjetivo.getText());
                    objetivoTableModel.addRow(objetivo);
                    // simula o clique do botao limpar
                    btClear.doClick();
                } else {
                    JOptionPane.showMessageDialog(getParent(), "Informe a descrição do objetivo específico",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                }
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
                btClear.doClick();
            } else if (e.getSource() == btClear) {
                txtObjetivo.setText("");
                txtObjetivo.requestFocusInWindow();
            }
        }
    }

}
