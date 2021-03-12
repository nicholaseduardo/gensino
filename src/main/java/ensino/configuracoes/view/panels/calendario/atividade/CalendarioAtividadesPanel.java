/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.calendario;

import ensino.components.GenJButton;
import ensino.components.GenJFormattedTextField;
import ensino.components.GenJLabel;
import ensino.components.GenJList;
import ensino.components.GenJTextField;
import ensino.components.renderer.GenListCellRenderer;
import ensino.configuracoes.model.Atividade;
import ensino.configuracoes.model.AtividadeFactory;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.Legenda;
import ensino.configuracoes.view.models.AtividadeTableModel;
import ensino.configuracoes.view.models.LegendaListModel;
import ensino.configuracoes.view.renderer.AtividadeCellRenderer;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.util.types.Periodo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
    private GenJList listLegenda;

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
        super("Controle de atividades");
        this.selectedCalendario = calendario;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        try {
            add(createAtividadeFieldsPanel(), BorderLayout.PAGE_START);
            add(createTablePanel(), BorderLayout.CENTER);
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    private ListCellRenderer getCellRenderer() {
        return new GenListCellRenderer() {
            private DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (isSelected) {
                    setColors(new Color(list.getSelectionForeground().getRGB()),
                            new Color(list.getSelectionBackground().getRGB()));
                } else {
                    setColors(new Color(list.getForeground().getRGB()),
                            new Color(255, 255, 255)
                    );
                }
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

                if (value instanceof Legenda) {
                    Legenda leg = (Legenda) value;
                    GenJLabel renderer = new GenJLabel(leg.getNome());
                    renderer.toBold();
                    renderer.resetFontSize(16);

                    if (isSelected) {
                        renderer.setIcon(new ImageIcon(getClass().getResource("/img/check-white-15.png")));
                    }
                    renderer.setForeground(Color.WHITE);
                    panel.setBackground(leg.getCor());

                    panel.add(renderer);
                }

                panel.setOpaque(true);

                return panel;
            }
        };
    }

    private JPanel createAtividadeFieldsPanel() throws ParseException {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        GenJLabel lblId = new GenJLabel("Código:", JLabel.TRAILING);
        txtId = new GenJTextField(5, false);
        txtId.setEnabled(false);
        lblId.setLabelFor(txtId);

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

        txtAte = GenJFormattedTextField.createFormattedField("##/##/####", 1);
        txtAte.setColumns(8);

        JPanel panelPeriodo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        panelPeriodo.setBorder(createTitleBorder("Período da atividade"));
        panelPeriodo.add(txtDe);
        panelPeriodo.add(new GenJLabel(" a "));
        panelPeriodo.add(txtAte);

        GenJLabel lblDescricao = new GenJLabel("Atividade: ", JLabel.TRAILING);
        txtAtividade = new GenJTextField(20, false);
        lblDescricao.setLabelFor(txtAtividade);

        listLegenda = new GenJList(new LegendaListModel());
        listLegenda.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listLegenda.setLayoutOrientation(JList.VERTICAL);
        listLegenda.setVisibleRowCount(4);
        JScrollPane scrollLegenda = new JScrollPane(listLegenda);
        scrollLegenda.setBorder(createTitleBorder("Legenda"));
        scrollLegenda.setAutoscrolls(true);

        listLegenda.setCellRenderer(getCellRenderer());

        int col = 0, row = 0;
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblId, c);
        GridLayoutHelper.set(c, col++, row);
        panel.add(txtId, c);

        GridLayoutHelper.set(c, col++, row);
        panel.add(panelPeriodo, c);

        GridLayoutHelper.set(c, col, row++, 1, 3, GridBagConstraints.LINE_START);
        panel.add(scrollLegenda, c);

        col = 0;
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblDescricao, c);
        GridLayoutHelper.set(c, col++, row++, 2, 1, GridBagConstraints.LINE_START);
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(txtAtividade, c);

        col = 0;
        GridLayoutHelper.set(c, col, row, 3, 1, GridBagConstraints.LINE_START);
        panel.add(createButtonsPanel(), c);

        return panel;
    }

    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btNew = new GenJButton("Novo", new ImageIcon(getClass().getResource(getImageSourceNew())));
        btAdd = new GenJButton("Adicionar", new ImageIcon(getClass().getResource(getImageSourceAdd())));
        btUpdate = new GenJButton("Alterar", new ImageIcon(getClass().getResource(getImageSourceUpdate())));
        btDel = new GenJButton("Remover", new ImageIcon(getClass().getResource(getImageSourceDel())));

        AtividadeActionListener atividadeListener = new AtividadeActionListener();
        btAdd.addActionListener(atividadeListener);
        btUpdate.addActionListener(atividadeListener);
        btDel.addActionListener(atividadeListener);
        btNew.addActionListener(atividadeListener);

        panel.add(btNew);
        panel.add(btAdd);
        panel.add(btUpdate);
        panel.add(btDel);

        return panel;
    }

    private JScrollPane createTablePanel() {
        atividadeTable = new JTable();

        ListSelectionModel cellSelectionModel = atividadeTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cellSelectionModel.addListSelectionListener(new AtividadeListSelectionListener());

        setData(new ArrayList());
        JScrollPane atividadeScroll = new JScrollPane();
        atividadeScroll.setViewportView(atividadeTable);
        atividadeScroll.setPreferredSize(new Dimension(400, 200));

        return atividadeScroll;
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
        listLegenda.clearSelection();
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("atividades", getData());
        return map;
    }

    private void setFieldValues(Long id, String descricao, Periodo periodo,
            Legenda legenda) {
        txtId.setText(id != null ? id.toString() : null);
        txtAtividade.setText(descricao);
        txtDe.setValue(periodo.getDeText());
        txtAte.setValue(periodo.getAteText());
        listLegenda.setSelectedValue(legenda);
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
        } else if (listLegenda.getSelectedValue() == null) {
            campo = "Legenda";
            listLegenda.requestFocusInWindow();
        } else {
            return true;
        }
        showInformationMessage(String.format(msg, campo));
        return result;
    }

    @Override
    public void enableFields(boolean active) {
        txtDe.setEnabled(active);
        txtAte.setEnabled(active);
        txtAtividade.setEnabled(active);
        listLegenda.setEnabled(active);

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
                        (Legenda) listLegenda.getSelectedValue(),
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
                Long id = 1L;
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
            } else if (source == btDel) {
                int selectedRow = atividadeTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(getParent(),
                            "Você não selecionou a Atividade que será removida.\nFavor, clique sobre uma Atividade!",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                atividadeTableModel.removeRow(selectedRow);
                atividadeTable.repaint();

                clear();
            }
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
