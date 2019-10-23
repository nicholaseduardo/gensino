/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.unidadeCurricular;

import ensino.components.GenJComboBox;
import ensino.components.GenJLabel;
import ensino.components.GenJTextArea;
import ensino.components.GenJTextField;
import ensino.configuracoes.model.Bibliografia;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.ReferenciaBibliografica;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.configuracoes.view.models.CampusComboBoxModel;
import ensino.configuracoes.view.models.CursoComboBoxListModel;
import ensino.configuracoes.view.models.CursoListModel;
import ensino.configuracoes.view.models.ReferenciaBibliograficaTableModel;
import ensino.configuracoes.view.panels.filters.BibliografiaSearch;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.HashMap;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author nicho
 */
public class UnidadeCurricularFieldsPanel extends DefaultFieldsPanel {

    /**
     * Atributo utilizado quando o curso é pré-requisito
     * para este formulário
     */
    private Curso selectedCurso;

    private GenJTextField txtId;
    private GenJComboBox comboCampus;
    private GenJComboBox comboCurso;
    private GenJTextField txtNome;
    private GenJTextField txtAulasTeoricas;
    private GenJTextField txtAulasPraticas;
    private GenJTextField txtCargaHoraria;
    private GenJTextArea txtEmenta;

    private BibliografiaSearch compoBiblioSearch;
    private GenJComboBox comboTipoRef;
    private JButton btAdicionar;
    private JButton btRemover;
    private JTable referenciasTable;
    private ReferenciaBibliograficaTableModel referenciaTableModel;

    private JTabbedPane tabbedFicha;

    public UnidadeCurricularFieldsPanel(Curso curso) {
        this();
        this.selectedCurso = curso;
    }

    public UnidadeCurricularFieldsPanel() {
        super();
        initComponents();
    }

    private JPanel createEmentaPanel() {
        JPanel panelEmenta = new JPanel(new GridBagLayout());
        tabbedFicha.addTab("Ementa", panelEmenta);
        GridBagConstraints c = new GridBagConstraints();

        int col = 0, row = 0;
        GenJLabel lblEmenta = new GenJLabel("Ementa", JLabel.TRAILING);
        GridLayoutHelper.set(c, col, row++);
        panelEmenta.add(lblEmenta, c);
        txtEmenta = new GenJTextArea(10, 50);
        lblEmenta.setLabelFor(txtEmenta);
        GridLayoutHelper.set(c, col, row);
        c.fill = GridBagConstraints.HORIZONTAL;
        JScrollPane ementaScroll = new JScrollPane(txtEmenta);
        ementaScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        c.fill = GridBagConstraints.BOTH;
        panelEmenta.add(ementaScroll, c);
        return panelEmenta;
    }

    private JPanel createReferenciasPanel() {
        JPanel panelReferencias = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        GenJLabel lblBibliografia = new GenJLabel("Bibliografia: ", JLabel.TRAILING);
        GridLayoutHelper.setRight(c, 0, 0);
        panelReferencias.add(lblBibliografia, c);

        compoBiblioSearch = new BibliografiaSearch();
        GridLayoutHelper.set(c, 1, 0, 2, 1, GridBagConstraints.LINE_START);
        panelReferencias.add(compoBiblioSearch, c);

        GenJLabel lblClassificacao = new GenJLabel("Classificação: ", JLabel.TRAILING);
        GridLayoutHelper.setRight(c, 0, 1);
        panelReferencias.add(lblClassificacao, c);

        String tipoList[] = {"Referência básica", "Referência complementar"};
        comboTipoRef = new GenJComboBox(tipoList);
        GridLayoutHelper.set(c, 1, 1);
        panelReferencias.add(comboTipoRef, c);

        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String source = String.format("/img/%s", "add-icon-png-25px.png");
        btAdicionar = new JButton("Adicionar", new ImageIcon(getClass().getResource(source)));
        btAdicionar.addActionListener((ActionEvent e) -> {
            Bibliografia b = compoBiblioSearch.getObjectValue();
            if (b != null) {
                ReferenciaBibliografica rb = new ReferenciaBibliografica();
                rb.setBibliografia(b);
                rb.setTipo(comboTipoRef.getSelectedIndex());
                if (referenciaTableModel.getData().contains(rb)) {
                    JOptionPane.showMessageDialog(panelReferencias, "A Bibliografia já foi adicionada.\nEscolha outro curso!",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                referenciaTableModel.addRow(rb);

                compoBiblioSearch.setObjectValue(null);
                comboTipoRef.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(panelReferencias, "Informe a identficação da Bibliografia",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        panelButton.add(btAdicionar);
        source = String.format("/img/%s", "del-button-png-25px.png");
        btRemover = new JButton("Remover", new ImageIcon(getClass().getResource(source)));
        btRemover.addActionListener((ActionEvent e) -> {
            int selectedRow = referenciasTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(panelReferencias,
                        "Você não selecionou a Referência Bibliográfica que será removida.\nFavor, clique sobre uma Unidade Curricular!",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            referenciaTableModel.removeRow(selectedRow);
            referenciasTable.repaint();
        });
        panelButton.add(btRemover);
        GridLayoutHelper.set(c, 2, 1);
        panelReferencias.add(panelButton, c);

        GenJLabel lblListaReferencia = new GenJLabel("Lista de referências", JLabel.TRAILING);
        GridLayoutHelper.set(c, 0, 2, 3, 1, GridBagConstraints.CENTER);
        panelReferencias.add(lblListaReferencia, c);

        referenciaTableModel = new ReferenciaBibliograficaTableModel();
        referenciasTable = new JTable(referenciaTableModel);
        JScrollPane listReferenciasScroll = new JScrollPane(referenciasTable);
        listReferenciasScroll.setPreferredSize(new Dimension(500, 200));
        GridLayoutHelper.set(c, 0, 3, 3, 1, GridBagConstraints.CENTER);
        panelReferencias.add(listReferenciasScroll, c);

        return panelReferencias;
    }

    private JPanel createIdentificacaoPanel() {
        int row = 0, col = 0;
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        GenJLabel lblId = new GenJLabel("Código:", JLabel.TRAILING);
        GridLayoutHelper.setRight(c, col++, row);
        fieldsPanel.add(lblId, c);
        txtId = new GenJTextField(10);
        lblId.setLabelFor(txtId);
        GridLayoutHelper.set(c, col++, row);
        fieldsPanel.add(txtId, c);

        GenJLabel lblCarga = new GenJLabel("Carga Horária:", JLabel.TRAILING);
        GridLayoutHelper.setRight(c, col++, row);
        fieldsPanel.add(lblCarga, c);
        txtCargaHoraria = new GenJTextField(10);
        lblCarga.setLabelFor(txtCargaHoraria);
        GridLayoutHelper.set(c, col, row++, 3, 1, GridBagConstraints.LINE_START);
        fieldsPanel.add(txtCargaHoraria, c);

        col = 0;
        GenJLabel lblCampus = new GenJLabel("Campus:", JLabel.TRAILING);
        GridLayoutHelper.setRight(c, col++, row);
        fieldsPanel.add(lblCampus, c);
        comboCampus = new GenJComboBox(new CampusComboBoxModel());
        comboCampus.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Campus campus = (Campus) e.getItem();
                CursoListModel cursoModel = (CursoListModel) comboCurso.getModel();
                cursoModel.setCampus(campus);
                cursoModel.refresh();
                comboCurso.setSelectedItem(selectedCurso);
            }
        });
        lblCampus.setLabelFor(comboCampus);
        GridLayoutHelper.set(c, col++, row);
        fieldsPanel.add(comboCampus, c);

        GenJLabel lblCurso = new GenJLabel("Curso:", JLabel.TRAILING);
        GridLayoutHelper.setRight(c, col++, row);
        fieldsPanel.add(lblCurso, c);
        comboCurso = new GenJComboBox(new CursoComboBoxListModel());
        lblCurso.setLabelFor(comboCurso);
        GridLayoutHelper.set(c, col, row++);
        fieldsPanel.add(comboCurso, c);

        col = 0;
        GenJLabel lblNome = new GenJLabel("Nome da U.C.:", JLabel.TRAILING);
        GridLayoutHelper.setRight(c, col++, row);
        fieldsPanel.add(lblNome, c);
        txtNome = new GenJTextField(30);
        lblNome.setLabelFor(txtNome);
        GridLayoutHelper.set(c, col, row++, 3, 1, GridBagConstraints.LINE_START);
        c.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(txtNome, c);

        col = 0;
        GenJLabel lblTeoricas = new GenJLabel("N.o Aulas Teóricas:", JLabel.TRAILING);
        GridLayoutHelper.setRight(c, col++, row);
        fieldsPanel.add(lblTeoricas, c);
        txtAulasTeoricas = new GenJTextField(10);
        lblTeoricas.setLabelFor(txtAulasTeoricas);
        GridLayoutHelper.set(c, col++, row);
        fieldsPanel.add(txtAulasTeoricas, c);

        GenJLabel lblPraticas = new GenJLabel("N.o Aulas Práticas:", JLabel.TRAILING);
        GridLayoutHelper.setRight(c, col++, row);
        fieldsPanel.add(lblPraticas, c);
        txtAulasPraticas = new GenJTextField(10);
        lblPraticas.setLabelFor(txtAulasPraticas);
        GridLayoutHelper.set(c, col, row++);
        fieldsPanel.add(txtAulasPraticas, c);

        return fieldsPanel;
    }

    private void initComponents() {
        tabbedFicha = new JTabbedPane();
        tabbedFicha.addTab("Identificação", createIdentificacaoPanel());
        tabbedFicha.addTab("Ementa", createEmentaPanel());
        tabbedFicha.addTab("Referências bibliográficas", createReferenciasPanel());

        add(tabbedFicha);
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("id", ("".equals(txtId.getText()) ? null
                : Integer.parseInt(txtId.getText())));
        map.put("nome", txtNome.getText());
        map.put("nAulasTeoricas", "".equals(txtAulasTeoricas.getText()) ? null
                : Integer.parseInt(txtAulasTeoricas.getText()));
        map.put("nAulasPraticas", "".equals(txtAulasPraticas.getText()) ? null
                : Integer.parseInt(txtAulasTeoricas.getText()));
        map.put("cargaHoraria", "".equals(txtCargaHoraria.getText()) ? null
                : Integer.parseInt(txtAulasTeoricas.getText()));
        map.put("ementa", txtEmenta.getText());
        map.put("curso", comboCurso.getSelectedItem());
        map.put("referenciasBibliograficas",
                (List<ReferenciaBibliografica>) referenciaTableModel.getData());
        return map;
    }

    private void setFieldValues(Integer id, String nome, Integer nAulasTeoricas,
            Integer nAulasPraticas, Integer cargaHoraria,
            String ementa, Curso curso,
            List<ReferenciaBibliografica> listReferencias) {
        txtId.setText(id.toString());
        txtNome.setText(nome);
        txtAulasTeoricas.setText(nAulasTeoricas.toString());
        txtAulasPraticas.setText(nAulasPraticas.toString());
        txtCargaHoraria.setText(cargaHoraria.toString());
        txtEmenta.setText(ementa);

        Campus campus = curso.getCampus();
        comboCampus.setSelectedItem(campus);
        CursoListModel cursoModel = (CursoListModel) comboCurso.getModel();
        cursoModel.setCampus(campus);
        cursoModel.refresh();
        comboCurso.setSelectedItem(curso);

        referenciaTableModel = new ReferenciaBibliograficaTableModel(listReferencias);
        referenciasTable.setModel(referenciaTableModel);
        TableColumnModel tcm = referenciasTable.getColumnModel();
        tcm.getColumn(0).setMaxWidth(50);
        tcm.getColumn(1).setMaxWidth(100);
        tcm.getColumn(2).setMinWidth(50);
        tcm.getColumn(3).setMinWidth(50);
        referenciasTable.repaint();
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {
        setFieldValues(
                (Integer) mapValues.get("id"),
                (String) mapValues.get("nome"),
                (Integer) mapValues.get("nAulasTeoricas"),
                (Integer) mapValues.get("nAulasPraticas"),
                (Integer) mapValues.get("cargaHoraria"),
                (String) mapValues.get("ementa"),
                (Curso) mapValues.get("curso"),
                (List<ReferenciaBibliografica>) mapValues.get("referenciasBibliograficas"));
    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof UnidadeCurricular) {
            UnidadeCurricular unidade = (UnidadeCurricular) object;
            setFieldValues(
                    unidade.getId(),
                    unidade.getNome(),
                    unidade.getnAulasTeoricas(),
                    unidade.getnAulasPraticas(),
                    unidade.getCargaHoraria(),
                    unidade.getEmenta(),
                    unidade.getCurso(),
                    unidade.getReferenciasBibliograficas()
            );
        }
    }

    @Override
    public boolean isValidated() {
        return comboCurso.getSelectedItem() != null
                && !"".equals(txtNome.getText())
                && !"".equals(txtAulasTeoricas.getText())
                && !"".equals(txtAulasPraticas.getText())
                && !"".equals(txtCargaHoraria.getText())
                && !"".equals(txtEmenta.getText());
    }

    @Override
    public void clearFields() {
        txtId.setText("");
        txtAulasPraticas.setText("");
        txtAulasTeoricas.setText("");
        txtCargaHoraria.setText("");
        txtEmenta.setText("");
        txtNome.setText("");
        comboCurso.setSelectedItem(selectedCurso);
        comboCampus.setSelectedItem(selectedCurso == null ? selectedCurso
                : selectedCurso.getCampus());
        compoBiblioSearch.setObjectValue(null);
    }

    @Override
    public void enableFields(boolean active) {
        txtId.setEnabled(false);
        txtAulasPraticas.setEnabled(active);
        txtAulasTeoricas.setEnabled(active);
        txtCargaHoraria.setEnabled(active);
        txtEmenta.setEnabled(active);
        txtNome.setEnabled(active);

        comboCurso.setEnabled(active && selectedCurso == null);
        comboCampus.setEnabled(active && selectedCurso == null);

        boolean activeSearch = active && !"".equals(txtId.getText());
        compoBiblioSearch.setEnable(activeSearch);
        comboTipoRef.setEnabled(activeSearch);
        btAdicionar.setEnabled(activeSearch);
        btRemover.setEnabled(activeSearch);
    }

    @Override
    public void initFocus() {
        comboCampus.requestFocusInWindow();
    }

}
