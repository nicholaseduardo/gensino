/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.unidadeCurricular;

import ensino.components.GenJButton;
import ensino.components.GenJComboBox;
import ensino.components.GenJLabel;
import ensino.components.GenJTextArea;
import ensino.components.GenJTextField;
import ensino.configuracoes.model.Bibliografia;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.ReferenciaBibliografica;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.configuracoes.model.UnidadeCurricularFactory;
import ensino.configuracoes.view.models.ReferenciaBibliograficaTableModel;
import ensino.configuracoes.view.panels.filters.BibliografiaSearch;
import ensino.configuracoes.view.renderer.ReferenciaBibliograficaCellRenderer;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.ControllerFactory;
import ensino.reports.ChartsFactory;
import ensino.util.types.AcoesBotoes;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author nicho
 */
public class UnidadeCurricularFields extends DefaultFieldsPanel {

    /**
     * Atributo utilizado quando o curso é pré-requisito para este formulário
     */
    private Curso selectedCurso;

    private GenJTextField txtId;
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
    private Component frame;
    private UnidadeCurricular unidadeCurricular;

    public UnidadeCurricularFields(Curso curso,
            Component frame) {
        this();
        this.selectedCurso = curso;
        this.frame = frame;
    }

    public UnidadeCurricularFields() {
        super();
        initComponents();
    }

    private void initComponents() {
        setName("unidadeCurricular.cadastro");
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEtchedBorder());

        backColor = ChartsFactory.lightBlue;
        foreColor = ChartsFactory.ardoziaBlueColor;
        setBackground(backColor);

        URL urlUnidade = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "school-icon-50px.png"));
        URL urlInfo = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "Info-icon-25px.png"));
        URL urlEmenta = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "Status-mail-task-icon-25px.png"));
        URL urlReferencias = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "library-icon-25px.png"));

        Icon iconInfo = new ImageIcon(urlInfo);
        Icon iconEmenta = new ImageIcon(urlEmenta);
        Icon iconReferencia = new ImageIcon(urlReferencias);

        GenJLabel lblTitulo = new GenJLabel("Ficha da Unidade Curricular",
                new ImageIcon(urlUnidade), JLabel.CENTER);
        lblTitulo.setVerticalTextPosition(JLabel.BOTTOM);
        lblTitulo.setHorizontalTextPosition(JLabel.CENTER);
        lblTitulo.resetFontSize(20);
        lblTitulo.setForeground(foreColor);
        lblTitulo.toBold();
        add(lblTitulo, BorderLayout.PAGE_START);

        GenJButton btSave = createButton(new ActionHandler(AcoesBotoes.SAVE), backColor, foreColor);
        GenJButton btClose = createButton(new ActionHandler(AcoesBotoes.CLOSE), backColor, foreColor);

        JPanel panelButton = createPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButton.add(btSave);
        panelButton.add(btClose);
        add(panelButton, BorderLayout.PAGE_END);

        tabbedFicha = new JTabbedPane();
        tabbedFicha.addTab("Identificação", iconInfo, createIdentificacaoPanel());
        tabbedFicha.addTab("Referências bibliográficas", iconReferencia, createReferenciasPanel());

        add(tabbedFicha, BorderLayout.CENTER);
    }

    private JPanel createReferenciasPanel() {
        GenJLabel lblBibliografia = new GenJLabel("Bibliografia: ", JLabel.TRAILING);
        compoBiblioSearch = new BibliografiaSearch();
        compoBiblioSearch.setBackground(backColor);

        GenJLabel lblClassificacao = new GenJLabel("Classificação: ", JLabel.TRAILING);
        String tipoList[] = {"Referência básica", "Referência complementar"};
        comboTipoRef = new GenJComboBox(tipoList);

        btAdicionar = createButton(new ActionHandler(AcoesBotoes.ADD), backColor, foreColor);
        btRemover = createButton(new ActionHandler(AcoesBotoes.DELETE), backColor, foreColor);

        JPanel panelReferencias = createPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        GridLayoutHelper.setRight(c, 0, 0);
        panelReferencias.add(lblBibliografia, c);
        GridLayoutHelper.set(c, 1, 0, 2, 1, GridBagConstraints.LINE_START);
        panelReferencias.add(compoBiblioSearch, c);

        GridLayoutHelper.setRight(c, 0, 1);
        panelReferencias.add(lblClassificacao, c);
        GridLayoutHelper.set(c, 1, 1);
        panelReferencias.add(comboTipoRef, c);

        JPanel panelButton = createPanel(new FlowLayout(FlowLayout.LEFT));
        panelButton.add(btAdicionar);
        panelButton.add(btRemover);

        GridLayoutHelper.set(c, 2, 1);
        panelReferencias.add(panelButton, c);

        GenJLabel lblListaReferencia = new GenJLabel("Lista de referências", JLabel.TRAILING);
        GridLayoutHelper.set(c, 0, 2, 3, 1, GridBagConstraints.CENTER);
        panelReferencias.add(lblListaReferencia, c);

        referenciaTableModel = new ReferenciaBibliograficaTableModel();
        referenciasTable = new JTable(referenciaTableModel);
        referenciasTable.getColumnModel().getColumn(0).setCellRenderer(new ReferenciaBibliograficaCellRenderer());
        JScrollPane scroll = new JScrollPane(referenciasTable);
        scroll.setPreferredSize(new Dimension(480, 240));
        scroll.setAutoscrolls(true);

        JPanel panel = createPanel(new BorderLayout());
        panel.add(panelReferencias, BorderLayout.PAGE_START);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createIdentificacaoPanel() {
        GenJLabel lblId = new GenJLabel("Código:", JLabel.TRAILING);
        txtId = new GenJTextField(10, false);
        lblId.setLabelFor(txtId);

        GenJLabel lblCarga = new GenJLabel("Carga Horária:", JLabel.TRAILING);
        txtCargaHoraria = new GenJTextField(10, true);
        lblCarga.setLabelFor(txtCargaHoraria);

        GenJLabel lblNome = new GenJLabel("Nome da U.C.:", JLabel.TRAILING);
        txtNome = new GenJTextField(30, true);
        lblNome.setLabelFor(txtNome);

        GenJLabel lblTeoricas = new GenJLabel("N.o Aulas Teóricas:", JLabel.TRAILING);
        txtAulasTeoricas = new GenJTextField(10, true);
        lblTeoricas.setLabelFor(txtAulasTeoricas);

        GenJLabel lblPraticas = new GenJLabel("N.o Aulas Práticas:", JLabel.TRAILING);
        txtAulasPraticas = new GenJTextField(10, true);
        lblPraticas.setLabelFor(txtAulasPraticas);

        txtEmenta = new GenJTextArea(5, 50);
        JScrollPane scroll = new JScrollPane(txtEmenta);
        scroll.setBorder(createTitleBorder("Ementa:"));
        scroll.setAutoscrolls(true);

        int row = 0, col = 0;
        JPanel fieldsPanel = createPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        GridLayoutHelper.setRight(c, col++, row);
        fieldsPanel.add(lblId, c);
        GridLayoutHelper.set(c, col++, row);
        fieldsPanel.add(txtId, c);

        GridLayoutHelper.setRight(c, col++, row);
        fieldsPanel.add(lblCarga, c);
        GridLayoutHelper.set(c, col, row++, 3, 1, GridBagConstraints.LINE_START);
        fieldsPanel.add(txtCargaHoraria, c);

        col = 0;
        GridLayoutHelper.setRight(c, col++, row);
        fieldsPanel.add(lblNome, c);
        GridLayoutHelper.set(c, col, row++, 3, 1, GridBagConstraints.LINE_START);
        c.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(txtNome, c);

        col = 0;
        GridLayoutHelper.setRight(c, col++, row);
        fieldsPanel.add(lblTeoricas, c);
        GridLayoutHelper.set(c, col++, row);
        fieldsPanel.add(txtAulasTeoricas, c);

        GridLayoutHelper.setRight(c, col++, row);
        fieldsPanel.add(lblPraticas, c);
        GridLayoutHelper.set(c, col, row++);
        fieldsPanel.add(txtAulasPraticas, c);

        JPanel panel = createPanel(new BorderLayout());
        panel.add(fieldsPanel, BorderLayout.PAGE_START);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
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
                : Integer.parseInt(txtAulasPraticas.getText()));
        map.put("cargaHoraria", "".equals(txtCargaHoraria.getText()) ? null
                : Integer.parseInt(txtCargaHoraria.getText()));
        map.put("ementa", txtEmenta.getText());
        map.put("curso", selectedCurso);
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

        referenciaTableModel = new ReferenciaBibliograficaTableModel(listReferencias);
        referenciasTable.setModel(referenciaTableModel);
        TableColumnModel tcm = referenciasTable.getColumnModel();
        TableColumn tcReferencia = tcm.getColumn(0);
        tcReferencia.setMinWidth(50);
        tcReferencia.setCellRenderer(new ReferenciaBibliograficaCellRenderer());
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
            unidadeCurricular = (UnidadeCurricular) object;
            setFieldValues(
                    unidadeCurricular.getId().getId(),
                    unidadeCurricular.getNome(),
                    unidadeCurricular.getnAulasTeoricas(),
                    unidadeCurricular.getnAulasPraticas(),
                    unidadeCurricular.getCargaHoraria(),
                    unidadeCurricular.getEmenta(),
                    unidadeCurricular.getId().getCurso(),
                    unidadeCurricular.getReferenciasBibliograficas()
            );
        }
    }

    @Override
    public boolean isValidated() {
        String msg = "O campo [%s] não foi informado.",
                campo = "";

        Integer index = 0;
        if ("".equals(txtNome.getText())) {
            campo = "NOME";
        } else if ("".equals(txtAulasTeoricas.getText())) {
            campo = "N. AULAS TEÓRICAS";
        } else if ("".equals(txtAulasPraticas.getText())) {
            campo = "N. AULAS PRÁTICAS";
        } else if ("".equals(txtCargaHoraria.getText())) {
            campo = "CARGA HORÁRIA";
        } else if ("".equals(txtEmenta.getText())) {
            campo = "EMENTA";
        } else {
            return true;
        }
        showInformationMessage(String.format(msg, campo));
        tabbedFicha.setSelectedIndex(index);
        return false;
    }

    @Override
    public void clearFields() {
        txtId.setText("");
        txtAulasPraticas.setText("");
        txtAulasTeoricas.setText("");
        txtCargaHoraria.setText("");
        txtEmenta.setText("");
        txtNome.setText("");
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

        compoBiblioSearch.setEnable(active);
        comboTipoRef.setEnabled(active);
        btAdicionar.setEnabled(active);
        btRemover.setEnabled(active);
    }

    @Override
    public void initFocus() {
        txtCargaHoraria.requestFocusInWindow();
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
    public void onSaveAction(ActionEvent e, Object o) {
        if (isValidated()) {
            if ("".equals(txtId.getText())) {
                unidadeCurricular = UnidadeCurricularFactory.getInstance()
                        .getObject(getFieldValues());
                selectedCurso.addUnidadeCurricular(unidadeCurricular);
            } else {
                UnidadeCurricularFactory.getInstance()
                        .updateObject(unidadeCurricular, getFieldValues());
            }
            try {
                ControllerFactory.createUnidadeCurricularController().salvar(unidadeCurricular);
                onCloseAction(e);
            } catch (Exception ex) {
                showErrorMessage(ex);
            }
        }
    }

    private void clear() {
        compoBiblioSearch.setObjectValue(null);
        comboTipoRef.setSelectedIndex(0);
    }

    @Override
    public void onAddAction(ActionEvent e, Object o) {
        Bibliografia b = compoBiblioSearch.getObjectValue();
        if (b != null) {
            ReferenciaBibliografica rb = new ReferenciaBibliografica();
            rb.getId().setBibliografia(b);
            rb.setTipo(comboTipoRef.getSelectedIndex());
            if (referenciaTableModel.getData().contains(rb)) {
                showWarningMessage("A Bibliografia já foi adicionada.\nEscolha outro curso!");
                return;
            }
            int id = 1;
            if (!referenciaTableModel.isEmpty()) {
                /**
                 * Procedimento realizado para gerar a chave única de cada
                 * atividade para cada calendário/campusll
                 */
                ReferenciaBibliografica atemp = referenciaTableModel.getMax(Comparator.comparing(a -> a.getId().getSequencia()));
                id = atemp.getId().getSequencia();
            }
            rb.getId().setSequencia(id);
            referenciaTableModel.addRow(rb);
            clear();
        } else {
            showInformationMessage("Informe a identficação da Bibliografia");
        }
    }

    @Override
    public void onDelAction(ActionEvent e, Object o) {
        int selectedRow = referenciasTable.getSelectedRow();
        if (selectedRow == -1) {
            showWarningMessage("Você não selecionou a Referência Bibliográfica que será removida."
                    + "\nFavor, clique sobre uma Referência Bibliográfica!");
            return;
        }
        referenciaTableModel.removeRow(selectedRow);
        referenciasTable.repaint();
    }

}
