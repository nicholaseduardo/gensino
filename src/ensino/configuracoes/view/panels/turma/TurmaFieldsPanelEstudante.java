/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.turma;

import ensino.components.GenJButton;
import ensino.components.GenJLabel;
import ensino.components.GenJTextField;
import ensino.configuracoes.model.Estudante;
import ensino.configuracoes.model.Turma;
import ensino.configuracoes.view.models.EstudanteTableModel;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author nicho
 */
public class TurmaFieldsPanelEstudante extends DefaultFieldsPanel {

    private GenJTextField txtId;
    private GenJTextField txtNome;
    private GenJTextField txtRegistro;

    private GenJButton btAdicionar;
    private GenJButton btRemover;
    private GenJButton btLimpar;
    private GenJButton btImportar;

    private JTable estudanteTable;
    private EstudanteTableModel estudanteTableModel;

    public TurmaFieldsPanelEstudante() {
        super("Dados dos estudantes");
        initComponents();
    }

    private void initComponents() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        add(createEstudantePanel());
    }

    public JPanel createEstudantePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        int col = 0, row = 0;
        GenJLabel lblId = new GenJLabel("Código: ", JLabel.TRAILING);
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblId, c);

        txtId = new GenJTextField(5);
        lblId.setLabelFor(txtId);
        GridLayoutHelper.set(c, col++, row);
        panel.add(txtId, c);

        GenJLabel lblRegistro = new GenJLabel("Número R.A.: ", JLabel.TRAILING);
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblRegistro, c);

        txtRegistro = new GenJTextField(10);
        lblRegistro.setLabelFor(txtRegistro);
        GridLayoutHelper.set(c, col, row++);
        panel.add(txtRegistro, c);

        col = 0;
        GenJLabel lblNome = new GenJLabel("Nome: ", JLabel.TRAILING);
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblNome, c);

        txtNome = new GenJTextField(30);
        lblNome.setLabelFor(txtNome);
        GridLayoutHelper.set(c, col, row++, 3, 1, GridBagConstraints.LINE_START);
        panel.add(txtNome, c);

        btLimpar = new GenJButton("Limpar", new ImageIcon(getClass().getResource(getImageSourceClear())));
        btAdicionar = new GenJButton("Adicionar", new ImageIcon(getClass().getResource(getImageSourceAdd())));
        btRemover = new GenJButton("Remover", new ImageIcon(getClass().getResource(getImageSourceDel())));
        btImportar = new GenJButton("Importar", new ImageIcon(getClass().getResource(getImageSourceImport())));

        ButtonAction btAction = new ButtonAction(this);
        btAdicionar.addActionListener(btAction);
        btRemover.addActionListener(btAction);
        btLimpar.addActionListener(btAction);
        btImportar.addActionListener(btAction);

        col = 0;
        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButton.add(btLimpar);
        panelButton.add(btAdicionar);
        panelButton.add(btRemover);
        panelButton.add(btImportar);
        GridLayoutHelper.set(c, col, row++, 4, 1, GridBagConstraints.LINE_START);
        panel.add(panelButton, c);

        GridLayoutHelper.set(c, col, row, 4, 1, GridBagConstraints.LINE_START);
        panel.add(createEstudanteListaPanel(), c);

        return panel;
    }

    private JPanel createEstudanteListaPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel.setBorder(createTitleBorder("Lista de estudantes"));

        estudanteTableModel = new EstudanteTableModel();
        estudanteTable = new JTable(estudanteTableModel);
        JScrollPane scrollEstudante = new JScrollPane(estudanteTable);
        scrollEstudante.setPreferredSize(new Dimension(500, 200));
        panel.add(scrollEstudante);
        return panel;
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = new HashMap();
        map.put("estudantes", estudanteTableModel.getData());
        return map;
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {
        List<Estudante> estudantes = (List<Estudante>) mapValues.get("estudantes");
        estudanteTableModel = new EstudanteTableModel(estudantes);
        refreshTable();
    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof Turma) {
            Turma turma = (Turma) object;
            estudanteTableModel = new EstudanteTableModel(turma.getEstudantes());
            refreshTable();
        }
    }

    private void refreshTable() {
        estudanteTable.setModel(estudanteTableModel);
        estudanteTable.repaint();

        TableColumnModel tcm = estudanteTable.getColumnModel();
        tcm.getColumn(0).setMaxWidth(50);
        tcm.getColumn(1).setMinWidth(100);
        tcm.getColumn(2).setMaxWidth(150);
    }

    @Override
    public boolean isValidated() {
        String msg = "O campo [%s] não foi informado!";
        String campo = "";
        if ("".equals(txtNome.getText())) {
            campo = "Nome";
            txtNome.requestFocusInWindow();
        } else {
            return true;
        }
        JOptionPane.showMessageDialog(this, String.format(msg, campo),
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
        return false;
    }

    @Override
    public void clearFields() {
        txtId.setText("");
        txtNome.setText("");
        txtRegistro.setText("");
    }

    @Override
    public void enableFields(boolean active) {
        txtId.setEnabled(false);
        txtNome.setEnabled(active);
        txtRegistro.setEnabled(active);

        btAdicionar.setEnabled(active);
        btImportar.setEnabled(active);
        btLimpar.setEnabled(active);
        btRemover.setEnabled(active);
        estudanteTable.setEnabled(active);
    }

    @Override
    public void initFocus() {
        txtNome.requestFocusInWindow();
    }

    private class ButtonAction implements ActionListener {

        private JPanel panel;

        public ButtonAction(JPanel panel) {
            this.panel = panel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == btAdicionar) {
                if (isValidated()) {
                    Estudante estudante = new Estudante();
                    Integer id = null;
                    String sId = txtId.getText();
                    if (sId.matches("\\d+")) {
                        id = Integer.parseInt(sId);
                    }
                    estudante.setId(id);
                    estudante.setNome(txtNome.getText());
                    estudante.setRegistro(txtRegistro.getText());
                    int index = estudanteTableModel.exists(estudante);
                    if (index > 0) {
                        estudanteTableModel.updateRow(index, source);
                    } else {
                        estudanteTableModel.addRow(estudante);
                    }
                }
            } else if (source == btRemover) {
                int selectedRow = estudanteTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(panel,
                            "Você não selecionou o Estudante que será removida.\n"
                            + "Favor, clique sobre um Estudante!",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                estudanteTableModel.removeRow(selectedRow);
            } else if (source == btImportar) {
                TurmaFieldsPanelEstudanteImportar dialog = new TurmaFieldsPanelEstudanteImportar();
                List<HashMap<String, String>> dadosImportados = dialog.getData();
                if (!dadosImportados.isEmpty()) {
                    estudanteTableModel = new EstudanteTableModel();
                    dadosImportados.forEach((HashMap<String,String>mapValue) -> {
                        Estudante estudante = new Estudante();
                        estudante.setNome(mapValue.get("nome"));
                        estudante.setRegistro(mapValue.get("registro"));
                        estudanteTableModel.addRow(estudante);
                    });
                    refreshTable();
                }
            }
            clearFields();
            initFocus();
            refreshTable();
        }

    }

}
