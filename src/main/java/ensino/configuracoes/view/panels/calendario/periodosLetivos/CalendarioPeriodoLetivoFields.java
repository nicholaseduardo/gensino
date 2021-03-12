/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.calendario.periodosLetivos;

import ensino.components.GenJFormattedTextField;
import ensino.components.GenJLabel;
import ensino.components.GenJTextField;
import ensino.configuracoes.model.PeriodoLetivo;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.SemanaLetiva;
import ensino.configuracoes.view.models.SemanaLetivaTableModel;
import ensino.configuracoes.view.renderer.SemanaLetivaCellRenderer;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.util.types.Periodo;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author nicho
 */
public class CalendarioPeriodoLetivoFields extends DefaultFieldsPanel {

    private GenJTextField txtId;
    private GenJFormattedTextField txtDe;
    private GenJFormattedTextField txtAte;
    private GenJTextField txtDescricao;

    private JTable semanaLetivaTable;
    private SemanaLetivaTableModel semanaLetivaTableModel;

    private Calendario selectedCalendario;

    public CalendarioPeriodoLetivoFields() {
        this(null);
    }

    public CalendarioPeriodoLetivoFields(Calendario calendario) {
        super("Controle de Períodos Letivos");
        this.selectedCalendario = calendario;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(5, 5));
        try {
            add(createPeriodoFieldsPanel(), BorderLayout.PAGE_START);
            add(createSemanaLetivaTablePanel(), BorderLayout.CENTER);
        } catch (ParseException ex) {
            showErrorMessage(ex);
        }
    }

    private JPanel createPeriodoFieldsPanel() throws ParseException {
        JPanel panel = createPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        GenJLabel lblId = new GenJLabel("Código:", JLabel.TRAILING);
        txtId = new GenJTextField(5, false);
        txtId.setEnabled(false);
        lblId.setLabelFor(txtId);

        txtDe = GenJFormattedTextField.createFormattedField("##/##/####", 1);
        txtDe.setColumns(8);
        txtAte = GenJFormattedTextField.createFormattedField("##/##/####", 1);
        txtAte.setColumns(8);
        JPanel panelPeriodo = createPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        panelPeriodo.setBorder(createTitleBorder("Período"));
        panelPeriodo.add(txtDe);
        panelPeriodo.add(new GenJLabel(" a "));
        panelPeriodo.add(txtAte);

        GenJLabel lblDescricao = new GenJLabel("Descrição: ", JLabel.TRAILING);
        txtDescricao = new GenJTextField(20, false);
        lblDescricao.setLabelFor(txtDescricao);

        int col = 0, row = 0;
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblId, c);
        GridLayoutHelper.set(c, col++, row);
        panel.add(txtId, c);

        GridLayoutHelper.setRight(c, col++, row++);
        panel.add(panelPeriodo, c);

        col = 0;
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblDescricao, c);
        GridLayoutHelper.set(c, col++, row++, 2, 1, GridBagConstraints.LINE_START);
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(txtDescricao, c);

        return panel;
    }

    private JScrollPane createSemanaLetivaTablePanel() {
        semanaLetivaTable = new JTable();
        ListSelectionModel cellSelectionModel = semanaLetivaTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setSemanaData(new ArrayList());

        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(semanaLetivaTable);
        scroll.setPreferredSize(new Dimension(400, 200));

        return scroll;
    }

    private void setSemanaData(List<SemanaLetiva> data) {
        semanaLetivaTableModel = new SemanaLetivaTableModel(data);
        semanaLetivaTable.setModel(semanaLetivaTableModel);
        resizeSemanaTableColumns();
    }

    private void resizeSemanaTableColumns() {
        TableColumnModel tcm = semanaLetivaTable.getColumnModel();
        tcm.getColumn(0).setCellRenderer(new SemanaLetivaCellRenderer());
        semanaLetivaTable.repaint();
    }

    /**
     * Limpa os campos para novo preenchimento
     */
    @Override
    public void clearFields() {
        txtId.setText("");
        txtDe.setValue(null);
        txtAte.setValue(null);
        txtDescricao.setText("");
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        try {
            HashMap<String, Object> map = new HashMap<>();
            map.put("numero", "".equals(txtId.getText()) ? null
                    : Integer.parseInt(txtId.getText()));
            map.put("descricao", txtDescricao.getText());
            map.put("periodo", new Periodo(txtDe.getText(), txtAte.getText()));
            map.put("calendario", selectedCalendario);
            return map;
        } catch (ParseException ex) {
            showErrorMessage(ex);
            return null;
        }
    }

    private void setFieldValues(Long id, String descricao, Periodo periodo) {
        txtId.setText(id != null ? id.toString() : null);
        txtDescricao.setText(descricao);
        txtDe.setValue(periodo.getDeText());
        txtAte.setValue(periodo.getAteText());
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {

    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof PeriodoLetivo) {
            PeriodoLetivo periodoLetivo = (PeriodoLetivo) object;
            setFieldValues(periodoLetivo.getId().getNumero(),
                    periodoLetivo.getDescricao(), periodoLetivo.getPeriodo());
            setSemanaData(periodoLetivo.getSemanasLetivas());
        }
    }

    /**
     * Verifica se os campos referentes a atividade estão todos preenchidos
     *
     * @return
     */
    @Override
    public boolean isValidated() {
        String msg = "O campo %s não foi informado!", campo = "";
        if (txtDe.getValue() == null) {
            campo = "Data de Início";
            txtDe.requestFocusInWindow();
        } else if (txtAte.getValue() == null) {
            campo = "Data Final";
            txtAte.requestFocusInWindow();
        } else if ("".equals(txtDescricao.getText())) {
            campo = "Descrição do período letivo";
            txtDescricao.requestFocusInWindow();
        } else {
            return true;
        }
        JOptionPane.showMessageDialog(getParent(), String.format(msg, campo),
                "Aviso", JOptionPane.WARNING_MESSAGE);
        return false;
    }

    @Override
    public void enableFields(boolean active) {
        txtDe.setEnabled(active);
        txtAte.setEnabled(active);
        txtDescricao.setEnabled(active);
    }

    @Override
    public void initFocus() {
        txtDe.requestFocusInWindow();
    }
}
