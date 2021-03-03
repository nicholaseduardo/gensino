/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.nivelEnsino;

import ensino.components.GenJButton;
import ensino.components.GenJLabel;
import ensino.components.GenJPanel;
import ensino.components.GenJTextField;
import ensino.components.renderer.GenCellRenderer;
import ensino.configuracoes.model.EtapaEnsino;
import ensino.configuracoes.model.NivelEnsino;
import ensino.configuracoes.view.models.EtapaEnsinoTableModel;
import ensino.defaults.DefaultFieldsPanel;
import ensino.defaults.DefaultTableModel;
import ensino.helpers.GridLayoutHelper;
import ensino.util.types.AcoesBotoes;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author santos
 */
public class NivelEnsinoFields extends DefaultFieldsPanel {

    private NivelEnsino nivelEnsino;

    private GenJTextField txtId;
    private GenJTextField txtNome;

    private JTable etapaEnsinoTable;
    private EtapaEnsinoTableModel etapaEnsinoTableModel;

    private GenJButton btNew;

    public NivelEnsinoFields() {
        super();
        nivelEnsino = new NivelEnsino();
        initComponents();
    }

    private void initComponents() {
        setName("plano.etapaEnsinos");
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEtchedBorder());

        add(createNivelFields(), BorderLayout.PAGE_START);
        add(createEtapaTablePanel(), BorderLayout.CENTER);
    }

    private JPanel createNivelFields() {
        GenJLabel lblId = new GenJLabel("Código:", JLabel.TRAILING);
        GenJLabel lblNome = new GenJLabel("Nome:", JLabel.TRAILING);
        txtId = new GenJTextField(10, false);
        txtNome = new GenJTextField(30, false);

        JPanel panel = createPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        int col = 0, row = 0;
        GridLayoutHelper.set(c, col++, row);
        panel.add(lblId, c);
        GridLayoutHelper.set(c, col++, row++);
        panel.add(txtId, c);

        col = 0;
        GridLayoutHelper.set(c, col++, row);
        panel.add(lblNome, c);
        GridLayoutHelper.set(c, col, row++);
        panel.add(txtNome, c);

        return panel;
    }

    private JPanel createEtapaTablePanel() {
        btNew = createButton(new ActionHandler(AcoesBotoes.NEW));
        btNew.setText("Criar uma nova Etapa de Ensino");
        JPanel panelButton = createPanel(new FlowLayout(FlowLayout.LEFT));
        panelButton.add(btNew);

        etapaEnsinoTable = new JTable();

        ListSelectionModel cellSelectionModel = etapaEnsinoTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setData(nivelEnsino.getEtapas());
        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(etapaEnsinoTable);
        scroll.setAutoscrolls(true);
        scroll.setPreferredSize(new Dimension(480, 240));

        JPanel panel = createPanel(new BorderLayout());
        panel.add(panelButton, BorderLayout.PAGE_START);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Inicializa a tabela de dados de atividades
     *
     * @param data
     */
    public void setData(List<EtapaEnsino> data) {
        etapaEnsinoTableModel = new EtapaEnsinoTableModel(data);
        refreshTable();
    }

    private GenCellRenderer getCellRenderer() {
        return new GenCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                if (isSelected) {
                    setColors(new Color(table.getSelectionForeground().getRGB()),
                            new Color(table.getSelectionBackground().getRGB()));
                } else {
                    setColors(new Color(table.getForeground().getRGB()),
                            row % 2 == 0
                                    ? new Color(table.getBackground().getRGB())
                                    : new Color(240, 240, 240));
                }
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                EtapaEnsino base = (EtapaEnsino) model.getRow(row);

                GenJLabel label = createLabel(String.format("[%d] %s", base.getId().getId(),
                        base.getNome()));
                label.setForeground(getFore());

                JPanel panel = createPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
                panel.add(label);
                panel.setBackground(getBack());
                panel.setOpaque(true);
                table.setRowHeight(row, panel.getPreferredSize().height);

                if (table.getColumnCount() > 1 && table.getRowHeight(row) < 50) {
                    table.setRowHeight(row, 55);
                }

                return panel;
            }
        };
    }

    private void refreshTable() {
        etapaEnsinoTable.setModel(etapaEnsinoTableModel);

        TableColumnModel tcm = etapaEnsinoTable.getColumnModel();
        TableColumn col0 = tcm.getColumn(0);
        col0.setCellRenderer(getCellRenderer());

        EnumSet enumSet = EnumSet.of(AcoesBotoes.EDIT, AcoesBotoes.DEL);

        TableColumn col1 = tcm.getColumn(1);
        col1.setCellRenderer(new ButtonsRenderer(null, enumSet));
        col1.setCellEditor(new GenJPanel.ButtonsEditor(etapaEnsinoTable, null, enumSet));

        etapaEnsinoTable.repaint();
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("id", "".equals(txtId.getText()) ? null
                : Integer.parseInt(txtId.getText()));
        map.put("nome", txtNome.getText());
        map.put("etapas", etapaEnsinoTableModel.getData());
        return map;
    }

    private void setFieldValues(Integer codigo, String nome,
            List<EtapaEnsino> l) {
        txtId.setText(codigo.toString());
        txtNome.setText(nome);
        setData(l);
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {
        Integer codigo = (Integer) mapValues.get("id");
        setFieldValues(codigo, (String) mapValues.get("nome"),
                (List<EtapaEnsino>) mapValues.get("etapas"));
    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof NivelEnsino) {
            nivelEnsino = (NivelEnsino) object;
            setFieldValues(nivelEnsino.getId(),
                    nivelEnsino.getNome(),
                    nivelEnsino.getEtapas());
        }
    }

    @Override
    public boolean isValidated() {
        String msg = "O campo %s não foi informado!", campo = "";
        if ("".equals(txtNome.getText())) {
            campo = "NOME";
            txtNome.requestFocusInWindow();
        } else {
            return true;
        }
        showInformationMessage(String.format(msg, campo));
        return false;
    }

    @Override
    public void clearFields() {
        txtId.setText("");
        txtNome.setText("");
        nivelEnsino = new NivelEnsino();
        setData(new ArrayList());
    }

    @Override
    public void enableFields(boolean active) {
        txtId.setEnabled(false);
        txtNome.setEnabled(active);
        etapaEnsinoTable.setEnabled(active);
        btNew.setEnabled(active);
    }

    @Override
    public void initFocus() {
        txtNome.requestFocusInWindow();
    }

    private void createDialog(EtapaEnsino etapaEnsino) {
        JDialog dialog = new JDialog();
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setModal(true);

        NivelEnsinoEtapaFields neef = new NivelEnsinoEtapaFields(nivelEnsino, dialog);
        if (etapaEnsino != null) {
            neef.setFieldValues(etapaEnsino);
        }
        dialog.add(neef);
        dialog.pack();
        dialog.setVisible(true);

        setData(nivelEnsino.getEtapas());
    }

    @Override
    public void onNewAction(ActionEvent e, Object o) {
        createDialog(null);
    }

    @Override
    public void onEditAction(ActionEvent e, Object o) {
        if (o instanceof JTable) {
            Object obj = getObjectFromTable((JTable) o);
            if (obj instanceof EtapaEnsino) {
                createDialog((EtapaEnsino) obj);
            }
        }
    }

    @Override
    public void onDelAction(ActionEvent e, Object o) {
        if (o instanceof JTable) {
            Object obj = getObjectFromTable((JTable) o);
            if (obj instanceof EtapaEnsino) {
                nivelEnsino.removeEtapaEnsino((EtapaEnsino) obj);
                setData(nivelEnsino.getEtapas());
            }
        }
    }
}
