/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.calendario;

import ensino.components.GenJButton;
import ensino.components.GenJComboBox;
import ensino.components.GenJFormattedTextField;
import ensino.components.GenJLabel;
import ensino.components.GenJTextField;
import ensino.configuracoes.controller.LegendaController;
import ensino.configuracoes.model.Atividade;
import ensino.configuracoes.model.AtividadeFactory;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.Legenda;
import ensino.configuracoes.view.models.AtividadeTableModel;
import ensino.configuracoes.view.panels.CalendarioPanel;
import ensino.configuracoes.view.renderer.AtividadeCellRenderer;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.ControllerFactory;
import ensino.util.types.Periodo;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author nicho
 */
public class CalendarioAtividadesPanel extends DefaultFieldsPanel {

    private GenJTextField txtId;
    private GenJFormattedTextField txtDe;
    private GenJFormattedTextField txtAte;
    private GenJTextField txtAtividade;
    private GenJComboBox comboLegenda;

    private GenJButton btAdd;
    private GenJButton btUpdate;
    private GenJButton btDel;
    private GenJButton btNew;

    private JTable atividadeTable;
    private AtividadeTableModel atividadeTableModel;

    private Calendario selectedCalendario;

    public CalendarioAtividadesPanel() {
        this(null);
    }

    public CalendarioAtividadesPanel(Calendario calendario) {
        super();
        this.selectedCalendario = calendario;
        initComponents();
    }

    private void initComponents() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        try {
            JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
            add(panel);
            JPanel panelLeft = new JPanel(new GridBagLayout());
            panel.add(panelLeft);
            GridBagConstraints c = new GridBagConstraints();

            GenJLabel lblId = new GenJLabel("Código:", JLabel.TRAILING);
            GridLayoutHelper.setRight(c, 0, 0);
            panelLeft.add(lblId, c);
            txtId = new GenJTextField(5, false);
            txtId.setEnabled(false);
            lblId.setLabelFor(txtId);
            GridLayoutHelper.set(c, 1, 0);
            panelLeft.add(txtId, c);

            GenJLabel lblDe = new GenJLabel("De: ", JLabel.TRAILING);
            GridLayoutHelper.setRight(c, 0, 1);
            panelLeft.add(lblDe, c);
            txtDe = GenJFormattedTextField.createFormattedField("##/##/####", 1);
            txtDe.setColumns(8);
            txtDe.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    if (txtDe.getValue() != null && txtAte.getValue() == null) {
                        try {
                            txtDe.commitEdit();
                            txtAte.setValue(txtDe.getValue());
                            txtAtividade.requestFocusInWindow();
                        } catch (ParseException ex) {
                            Logger.getLogger(CalendarioAtividadesPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

            });
            lblDe.setLabelFor(txtDe);
            GridLayoutHelper.set(c, 1, 1);
            c.fill = GridBagConstraints.HORIZONTAL;
            panelLeft.add(txtDe, c);

            GenJLabel lblAte = new GenJLabel("Até: ", JLabel.TRAILING);
            GridLayoutHelper.setRight(c, 2, 1);
            panelLeft.add(lblAte, c);
            txtAte = GenJFormattedTextField.createFormattedField("##/##/####", 1);
            txtAte.setColumns(8);
            lblAte.setLabelFor(txtAte);
            GridLayoutHelper.set(c, 3, 1);
            c.fill = GridBagConstraints.HORIZONTAL;
            panelLeft.add(txtAte, c);

            GenJLabel lblDescricao = new GenJLabel("Atividade: ", JLabel.TRAILING);
            GridLayoutHelper.setRight(c, 0, 2);
            panelLeft.add(lblDescricao, c);
            txtAtividade = new GenJTextField(20, true);
            lblDescricao.setLabelFor(txtAtividade);
            GridLayoutHelper.set(c, 1, 2);
            c.gridwidth = 3;
            c.fill = GridBagConstraints.HORIZONTAL;
            panelLeft.add(txtAtividade, c);

            GenJLabel lblLegenda = new GenJLabel("Legenda: ", JLabel.TRAILING);
            GridLayoutHelper.setRight(c, 0, 3);
            panelLeft.add(lblLegenda, c);
            LegendaController legendaCol = ControllerFactory.createLegendaController();
            comboLegenda = new GenJComboBox(legendaCol.listar().toArray());
            lblLegenda.setLabelFor(comboLegenda);
            GridLayoutHelper.set(c, 1, 3);
            c.gridwidth = 3;
            c.fill = GridBagConstraints.HORIZONTAL;
            panelLeft.add(comboLegenda, c);
            comboLegenda.setRenderer(new ListCellRenderer() {
                private DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

                @Override
                public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(
                            list, value, index, isSelected, cellHasFocus);

                    if (value instanceof Legenda) {
                        Legenda leg = (Legenda) value;
                        renderer.setForeground(leg.getCor());
                        Font fieldFont = renderer.getFont();
                        renderer.setFont(new Font(fieldFont.getFontName(),
                                Font.BOLD,
                                fieldFont.getSize()));
                    }

                    return renderer;
                }
            });

            JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            GridLayoutHelper.set(c, 0, 4, 4, 1, GridBagConstraints.BASELINE);
            c.fill = GridBagConstraints.HORIZONTAL;
            panelLeft.add(panelButtons, c);

            String source = String.format("/img/%s", "view-button-25px.png");
            btNew = new GenJButton("Novo", new ImageIcon(getClass().getResource(source)));

            source = String.format("/img/%s", "add-icon-png-25px.png");
            btAdd = new GenJButton("Adicionar", new ImageIcon(getClass().getResource(source)));

            source = String.format("/img/%s", "update-button-25px.png");
            btUpdate = new GenJButton("Alterar", new ImageIcon(getClass().getResource(source)));

            source = String.format("/img/%s", "del-button-png-25px.png");
            btDel = new GenJButton("Remover", new ImageIcon(getClass().getResource(source)));

            AtividadeActionListener atividadeListener = new AtividadeActionListener();
            btAdd.addActionListener(atividadeListener);
            btUpdate.addActionListener(atividadeListener);
            btDel.addActionListener(atividadeListener);
            btNew.addActionListener(atividadeListener);
            panelButtons.add(btNew);
            panelButtons.add(btAdd);
            panelButtons.add(btUpdate);
            panelButtons.add(btDel);

            atividadeTable = new JTable();
            ListSelectionModel cellSelectionModel = atividadeTable.getSelectionModel();
            cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            cellSelectionModel.addListSelectionListener(new AtividadeListSelectionListener());
            setData(new ArrayList());
            JScrollPane atividadeScroll = new JScrollPane();
            atividadeScroll.setViewportView(atividadeTable);
            atividadeScroll.setPreferredSize(new Dimension(400, 100));
            panel.add(atividadeScroll);

        } catch (Exception ex) {
            Logger.getLogger(CalendarioPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Recupera a lista de atividades atualizada (adicionadas/removidas)
     * Permissão de acesso para as classes do pacote.
     *
     * @return
     */
    public List<Atividade> getData() {
        return atividadeTableModel.getData();
    }

    /**
     * Inicializa a tabela de dados de atividades
     *
     * @param data
     */
    private void setData(List<Atividade> data) {
        atividadeTableModel = new AtividadeTableModel(data);
        atividadeTable.setModel(atividadeTableModel);
        resizeTableColumns();
    }

    private void resizeTableColumns() {
        TableColumnModel tcm = atividadeTable.getColumnModel();
        tcm.getColumn(0).setCellRenderer(new AtividadeCellRenderer());
        atividadeTable.repaint();
    }

    /**
     * Limpa os campos para novo preenchimento
     */
    @Override
    public void clearFields() {
        clearLocalFields();

        setData(new ArrayList());
    }

    private void clearLocalFields() {
        txtId.setText("");
        txtDe.setValue(null);
        txtAte.setValue(null);
        txtAtividade.setText("");
        comboLegenda.setSelectedItem(null);
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("atividades", getData());
        return map;
    }

    private void setFieldValues(Integer id, String descricao, Periodo periodo,
            Legenda legenda) {
        txtId.setText(id != null ? id.toString() : null);
        txtAtividade.setText(descricao);
        txtDe.setValue(periodo.getDeText());
        txtAte.setValue(periodo.getAteText());
        comboLegenda.setSelectedItem(legenda);
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {

    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof Calendario) {
            try {
                Calendario o = (Calendario) object;
                setData(o.getAtividades());
            } catch (Exception ex) {
                Logger.getLogger(CalendarioAtividadesPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Verifica se os campos referentes a atividade estão todos preenchidos
     *
     * @return
     */
    @Override
    public boolean isValidated() {
        boolean result = false;
        String msg = "O campo %s não foi informado!", campo = "";
        if (txtDe.getValue() == null) {
            campo = "Data de Início";
            txtDe.requestFocusInWindow();
        } else if (txtAte.getValue() == null) {
            campo = "Data Final";
            txtAte.requestFocusInWindow();
        } else if ("".equals(txtAtividade.getText())) {
            campo = "Descrição";
            txtAtividade.requestFocusInWindow();
        } else if (comboLegenda.getSelectedItem() == null) {
            campo = "Legenda";
            comboLegenda.requestFocusInWindow();
        } else {
            return true;
        }
        JOptionPane.showMessageDialog(getParent(), String.format(msg, campo),
                "Aviso", JOptionPane.WARNING_MESSAGE);
        return result;
    }

    @Override
    public void enableFields(boolean active) {
        txtDe.setEnabled(active);
        txtAte.setEnabled(active);
        txtAtividade.setEnabled(active);
        comboLegenda.setEnabled(active);
        enableLocalButtons(active);
        clearLocalFields();
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
        txtDe.requestFocusInWindow();
    }

    private class AtividadeActionListener implements ActionListener {

        private void clear() {
            clearLocalFields();
            enableLocalButtons(Boolean.TRUE);
            initFocus();
        }

        private Atividade createAtividadeFromFields() {
            try {
                String sid = txtId.getText();
                Atividade at = AtividadeFactory.getInstance().createObject(
                        sid.matches("\\d+") ? Integer.parseInt(sid) : null,
                        new Periodo(txtDe.getText(), txtAte.getText()),
                        txtAtividade.getText(),
                        (Legenda) comboLegenda.getSelectedItem(),
                        selectedCalendario
                );
                return at;
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(getParent(), "Erro ao adicionar Atividade: "
                        + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == btUpdate && isValidated()) {
                int selectedRow = atividadeTable.getSelectedRow();
                atividadeTableModel.updateRow(selectedRow, createAtividadeFromFields());
            } else if (source == btAdd && isValidated()) {
                int id = 1;
                if (!atividadeTableModel.isEmpty()) {
                    /**
                     * Procedimento realizado para gerar a chave única de cada
                     * atividade para cada calendário/campusll
                     */
                    Atividade atemp = atividadeTableModel.getMax(Comparator.comparing(a -> a.getId().getId()));
                    id = atemp.getId().getId() + 1;
                }
                /**
                 * atribui o valor do ID ao campo para reaproveitar o método de
                 * criação do objeto Atividade
                 */
                txtId.setText(String.valueOf(id));
                atividadeTableModel.addRow(createAtividadeFromFields());
            } else if (e.getSource() == btDel) {
                int selectedRow = atividadeTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(getParent(),
                            "Você não selecionou a Atividade que será removida.\nFavor, clique sobre uma Atividade!",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                atividadeTableModel.removeRow(selectedRow);
                atividadeTable.repaint();
            }
            clear();
        }
    }

    private class AtividadeListSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            int selectedRow = atividadeTable.getSelectedRow();
            if (selectedRow >= 0) {
                Atividade o = (Atividade) atividadeTableModel.getRow(selectedRow);
                setFieldValues(o.getId().getId(), o.getDescricao(),
                        o.getPeriodo(), o.getLegenda());
                enableLocalButtons(Boolean.TRUE);
                initFocus();
            }
        }

    }
}
