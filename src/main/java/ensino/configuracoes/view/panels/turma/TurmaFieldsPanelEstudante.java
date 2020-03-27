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
import ensino.configuracoes.model.EstudanteFactory;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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
public class TurmaFieldsPanelEstudante extends DefaultFieldsPanel {

    private GenJTextField txtId;
    private GenJTextField txtNome;
    private GenJTextField txtRegistro;

    private GenJButton btAdicionar;
    private GenJButton btRemover;
    private GenJButton btNovo;
    private GenJButton btImportar;
    private GenJButton btAtualizar;

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

        txtId = new GenJTextField(5, false);
        lblId.setLabelFor(txtId);
        GridLayoutHelper.set(c, col++, row);
        panel.add(txtId, c);

        GenJLabel lblRegistro = new GenJLabel("Número R.A.: ", JLabel.TRAILING);
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblRegistro, c);

        txtRegistro = new GenJTextField(10, false);
        lblRegistro.setLabelFor(txtRegistro);
        GridLayoutHelper.set(c, col, row++);
        panel.add(txtRegistro, c);

        col = 0;
        GenJLabel lblNome = new GenJLabel("Nome: ", JLabel.TRAILING);
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblNome, c);

        txtNome = new GenJTextField(30, true);
        lblNome.setLabelFor(txtNome);
        GridLayoutHelper.set(c, col, row++, 3, 1, GridBagConstraints.LINE_START);
        panel.add(txtNome, c);

        btNovo = new GenJButton("Novo", new ImageIcon(getClass().getResource(String.format("/img/%s", "view-button-25px.png"))));
        btAdicionar = new GenJButton("Adicionar", new ImageIcon(getClass().getResource(getImageSourceAdd())));
        btRemover = new GenJButton("Remover", new ImageIcon(getClass().getResource(getImageSourceDel())));
        btImportar = new GenJButton("Importar", new ImageIcon(getClass().getResource(getImageSourceImport())));

        String source = String.format("/img/%s", "update-button-25px.png");
        btAtualizar = new GenJButton("Alterar", new ImageIcon(getClass().getResource(source)));

        ButtonAction btAction = new ButtonAction();
        btAdicionar.addActionListener(btAction);
        btRemover.addActionListener(btAction);
        btNovo.addActionListener(btAction);
        btImportar.addActionListener(btAction);
        btAtualizar.addActionListener(btAction);

        col = 0;
        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButton.add(btNovo);
        panelButton.add(btAdicionar);
        panelButton.add(btAtualizar);
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

        estudanteTable = new JTable(estudanteTableModel);
        ListSelectionModel cellSelectionModel = estudanteTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cellSelectionModel.addListSelectionListener(new EstudanteListSelectionListener());

        setData(new ArrayList()
        );
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
        setData(estudantes);
    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof Turma) {
            Turma turma = (Turma) object;
            setData(turma.getEstudantes());
        } else if (object instanceof Estudante) {
            Estudante o = (Estudante) object;
            txtId.setText(o.getId().getId().toString());
            txtNome.setText(o.getNome());
            txtRegistro.setText(o.getRegistro());
        }
    }

    private void setData(List<Estudante> data) {
        estudanteTableModel = new EstudanteTableModel(data);
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
        clearLocalFields();
        setData(new ArrayList());
    }

    private void clearLocalFields() {
        txtId.setText("");
        txtNome.setText("");
        txtRegistro.setText("");
    }

    @Override
    public void enableFields(boolean active) {
        txtId.setEnabled(false);
        txtNome.setEnabled(active);
        txtRegistro.setEnabled(active);
        estudanteTable.setEnabled(active);
        enableLocalButtons(active);
        clearLocalFields();
    }

    private void enableLocalButtons(Boolean active) {
        Boolean status = "".equals(txtId.getText());

        btAdicionar.setEnabled(active && status);
        btNovo.setEnabled(active && !status);
        btImportar.setEnabled(active && !status);
        btAtualizar.setEnabled(active && !status);
        btRemover.setEnabled(active && !status);
    }

    @Override
    public void initFocus() {
        txtNome.requestFocusInWindow();
    }

    private class ButtonAction implements ActionListener {

        private void clear() {
            clearLocalFields();
            enableLocalButtons(Boolean.TRUE);
            initFocus();
        }

        private Estudante createEstudanteFromFields() {
            Integer id = null;
            String sId = txtId.getText();
            if (sId.matches("\\d+")) {
                id = Integer.parseInt(sId);
            }
            Estudante estudante = EstudanteFactory.getInstance()
                    .createObject(id, txtNome.getText(),
                            txtRegistro.getText());
            return estudante;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == btAtualizar && isValidated()) {
                int selectedRow = estudanteTable.getSelectedRow();
                estudanteTableModel.updateRow(selectedRow, createEstudanteFromFields());
            } else if (source == btAdicionar && isValidated()) {
                Integer id = 1;

                if (!estudanteTableModel.isEmpty()) {
                    /**
                     * Procedimento realizado para gerar a chave única de cada
                     * estudante para cada turma
                     */
                    Estudante eTemp = estudanteTableModel.getMax(Comparator.comparing(est -> est.getId().getId()));
                    id = eTemp.getId().getId() + 1;
                }
                /**
                 * atribui o valor do ID ao campo para reaproveitar o método de
                 * criação do objeto Estudante
                 */
                txtId.setText(id.toString());
                estudanteTableModel.addRow(createEstudanteFromFields());
            } else if (source == btRemover) {
                int selectedRow = estudanteTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(getParent(),
                            "Você não selecionou o Estudante que será removida.\n"
                            + "Favor, clique sobre um Estudante!",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                estudanteTableModel.removeRow(selectedRow);
                estudanteTable.repaint();
            } else if (source == btImportar) {
                TurmaFieldsPanelEstudanteImportar dialog = new TurmaFieldsPanelEstudanteImportar();
                List<HashMap<String, String>> dadosImportados = dialog.getData();
                if (!dadosImportados.isEmpty()) {
                    estudanteTableModel = new EstudanteTableModel();
                    int id = 1;
                    for (int i = 0; i < dadosImportados.size(); i++) {
                        HashMap<String, String> mapValue = dadosImportados.get(i);

                        Estudante estudante = new Estudante();
                        estudante.getId().setId(id++);
                        estudante.setNome(mapValue.get("nome"));
                        estudante.setRegistro(mapValue.get("registro"));
                        estudanteTableModel.addRow(estudante);
                    }
                }
            }
            clear();
        }

    }

    private class EstudanteListSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            int selectedRow = estudanteTable.getSelectedRow();
            if (selectedRow >= 0) {
                Estudante o = (Estudante) estudanteTableModel.getRow(selectedRow);
                setFieldValues(o);
                enableLocalButtons(Boolean.TRUE);
                initFocus();
            }
        }

    }

}
