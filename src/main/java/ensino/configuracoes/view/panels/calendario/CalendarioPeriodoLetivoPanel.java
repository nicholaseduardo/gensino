/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.calendario;

import ensino.components.GenJButton;
import ensino.components.GenJFormattedTextField;
import ensino.components.GenJLabel;
import ensino.components.GenJTextField;
import ensino.configuracoes.model.PeriodoLetivo;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.PeriodoLetivoFactory;
import ensino.configuracoes.view.models.PeriodoLetivoTableModel;
import ensino.configuracoes.view.panels.CalendarioPanel;
import ensino.configuracoes.view.renderer.PeriodoLetivoCellRenderer;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.util.types.Periodo;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
public class CalendarioPeriodoLetivoPanel extends DefaultFieldsPanel {

    private GenJTextField txtId;
    private GenJFormattedTextField txtDe;
    private GenJFormattedTextField txtAte;
    private GenJTextField txtPeriodoLetivo;

    private GenJButton btAdd;
    private GenJButton btUpdate;
    private GenJButton btDel;
    private GenJButton btNew;

    private JTable periodoLetivoTable;
    private PeriodoLetivoTableModel periodoLetivoTableModel;

    private Calendario selectedCalendario;

    public CalendarioPeriodoLetivoPanel() {
        this(null);
    }

    public CalendarioPeriodoLetivoPanel(Calendario calendario) {
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

            GenJLabel lblDescricao = new GenJLabel("Periodo Letivo: ", JLabel.TRAILING);
            GridLayoutHelper.setRight(c, 0, 2);
            panelLeft.add(lblDescricao, c);
            txtPeriodoLetivo = new GenJTextField(20, true);
            lblDescricao.setLabelFor(txtPeriodoLetivo);
            GridLayoutHelper.set(c, 1, 2);
            c.gridwidth = 3;
            c.fill = GridBagConstraints.HORIZONTAL;
            panelLeft.add(txtPeriodoLetivo, c);

            JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            GridLayoutHelper.set(c, 0, 4, 4, 1, GridBagConstraints.BASELINE);
            c.fill = GridBagConstraints.HORIZONTAL;
            panelLeft.add(panelButtons, c);

            String source = String.format("/img/%s", "add-icon-png-25px.png");
            btAdd = new GenJButton("Adicionar", new ImageIcon(getClass().getResource(source)));

            source = String.format("/img/%s", "view-button-25px.png");
            btNew = new GenJButton("Novo", new ImageIcon(getClass().getResource(source)));

            source = String.format("/img/%s", "del-button-png-25px.png");
            btDel = new GenJButton("Remover", new ImageIcon(getClass().getResource(source)));

            source = String.format("/img/%s", "update-button-25px.png");
            btUpdate = new GenJButton("Alterar", new ImageIcon(getClass().getResource(source)));

            PeriodoLetivoActionListener atividadeListener = new PeriodoLetivoActionListener();
            btAdd.addActionListener(atividadeListener);
            btUpdate.addActionListener(atividadeListener);
            btDel.addActionListener(atividadeListener);
            btNew.addActionListener(atividadeListener);

            panelButtons.add(btNew);
            panelButtons.add(btAdd);
            panelButtons.add(btUpdate);
            panelButtons.add(btDel);

            periodoLetivoTable = new JTable();
            ListSelectionModel cellSelectionModel = periodoLetivoTable.getSelectionModel();
            cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            cellSelectionModel.addListSelectionListener(new PeriodoLetivoListSelectionListener());
            setData(new ArrayList());
            JScrollPane atividadeScroll = new JScrollPane();
            atividadeScroll.setViewportView(periodoLetivoTable);
            atividadeScroll.setPreferredSize(new Dimension(400, 100));
            panel.add(atividadeScroll);

        } catch (ParseException ex) {
            Logger.getLogger(CalendarioPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Recupera a lista de atividades atualizada (adicionadas/removidas) Acesso
     * limitado ao pacote
     *
     * @return
     */
    List<PeriodoLetivo> getData() {
        return periodoLetivoTableModel.getData();
    }

    /**
     * Inicializa a tabela de dados de atividades
     *
     * @param data
     */
    private void setData(List<PeriodoLetivo> data) {
        periodoLetivoTableModel = new PeriodoLetivoTableModel(data);
        periodoLetivoTable.setModel(periodoLetivoTableModel);
        resizeTableColumns();
    }

    private void resizeTableColumns() {
        TableColumnModel tcm = periodoLetivoTable.getColumnModel();
        tcm.getColumn(0).setCellRenderer(new PeriodoLetivoCellRenderer());
        periodoLetivoTable.repaint();
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
        txtPeriodoLetivo.setText("");
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("periodosLetivos", getData());
        return map;
    }

    private void setFieldValues(Integer id, String descricao, Periodo periodo) {
        txtId.setText(id != null ? id.toString() : null);
        txtPeriodoLetivo.setText(descricao);
        txtDe.setValue(periodo.getDeText());
        txtAte.setValue(periodo.getAteText());
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {

    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof Calendario) {
            try {
                Calendario calendario = (Calendario) object;
                setData(calendario.getPeriodosLetivos());
            } catch (Exception ex) {
                Logger.getLogger(CalendarioPeriodoLetivoPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (object instanceof PeriodoLetivo) {
            PeriodoLetivo o = (PeriodoLetivo) object;
            setFieldValues(o.getId().getNumero(), 
                    o.getDescricao(), 
                    o.getPeriodo());
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
        } else if ("".equals(txtPeriodoLetivo.getText())) {
            campo = "Descrição do período letivo";
            txtPeriodoLetivo.requestFocusInWindow();
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
        txtPeriodoLetivo.setEnabled(active);
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
        txtDe.requestFocusInWindow();
    }

    private class PeriodoLetivoActionListener implements ActionListener {

        private void clear() {
            clearLocalFields();
            enableLocalButtons(Boolean.TRUE);
            initFocus();
        }

        private PeriodoLetivo createPeriodoLetivoFromFields() {
            try {
                String sid = txtId.getText();
                PeriodoLetivo o = PeriodoLetivoFactory.getInstance()
                        .createObject((sid.matches("\\d+") ? Integer.parseInt(sid) : null),
                                txtPeriodoLetivo.getText(),
                                new Periodo(txtDe.getText(), txtAte.getText()));
                o.getId().setCalendario(selectedCalendario);
                return o;
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(getParent(), "Erro ao adicionar Período Letivo: "
                        + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == btUpdate && isValidated()) {
                int selectedRow = periodoLetivoTable.getSelectedRow();
                periodoLetivoTableModel.updateRow(selectedRow, createPeriodoLetivoFromFields());
            } else if (source == btAdd && isValidated()) {
                int id = 1;
                if (!periodoLetivoTableModel.isEmpty()) {
                    /**
                     * Procedimento realizado para gerar a chave única
                     * de cada atividade para cada calendário/campusll
                     */
                    PeriodoLetivo otemp = periodoLetivoTableModel.getMax(Comparator.comparing(o -> o.getId().getNumero()));
                    id = otemp.getId().getNumero() + 1;
                }
                /**
                 * atribui o valor do ID ao campo para reaproveitar o método de
                 * criação do objeto Atividade
                 */
                txtId.setText(String.valueOf(id));
                periodoLetivoTableModel.addRow(createPeriodoLetivoFromFields());
            } else if (e.getSource() == btDel) {
                int selectedRow = periodoLetivoTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(getParent(),
                            "Você não selecionou o Periodo Letivo que será removido.\nFavor, clique sobre um Periodo Letivo!",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                periodoLetivoTableModel.removeRow(selectedRow);
                periodoLetivoTable.repaint();
            }
            clear();
        }
    }

    private class PeriodoLetivoListSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            int selectedRow = periodoLetivoTable.getSelectedRow();
            if (selectedRow >= 0) {
                setFieldValues(periodoLetivoTableModel.getRow(selectedRow));
                enableLocalButtons(Boolean.TRUE);
                initFocus();
            }
        }

    }
}
