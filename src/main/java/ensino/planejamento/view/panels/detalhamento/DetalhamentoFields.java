/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.detalhamento;

import ensino.components.GenJButton;
import ensino.components.GenJComboBox;
import ensino.components.GenJLabel;
import ensino.components.GenJList;
import ensino.components.GenJRadioButton;
import ensino.components.GenJSpinner;
import ensino.components.GenJTextArea;
import ensino.components.renderer.TextAreaCellRenderer;
import ensino.configuracoes.model.SemanaLetiva;
import ensino.configuracoes.view.models.MetodoComboBoxModel;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.BaseObject;
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.controller.DetalhamentoController;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.Metodologia;
import ensino.planejamento.model.MetodologiaFactory;
import ensino.planejamento.model.MetodologiaId;
import ensino.planejamento.model.Objetivo;
import ensino.planejamento.model.ObjetivoDetalhe;
import ensino.planejamento.model.ObjetivoDetalheFactory;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.models.MetodologiaTableModel;
import ensino.planejamento.view.models.ObjetivoComboBoxModel;
import ensino.planejamento.view.models.ObjetivoDetalheTableModel;
import ensino.planejamento.view.renderer.MetodologiaCellRenderer;
import ensino.planejamento.view.renderer.TextAreaItemRenderer;
import ensino.util.types.AcoesBotoes;
import ensino.util.types.TipoMetodo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author nicho
 */
public class DetalhamentoFields extends DefaultFieldsPanel {

    private Detalhamento detalhamento;
    private SemanaLetiva semanaLetiva;
    private GenJLabel lblSemana;

    private GenJSpinner txtNAulasP;
    private GenJSpinner txtNAulasT;
    private GenJTextArea txtConteudo;
    private GenJList txtObservacao;

    private JTable metodologiaTable;
    private MetodologiaTableModel metodologiaTableModel;
    private JTable objetivosDetalheTable;
    private ObjetivoDetalheTableModel objetivoDetalheTableModel;

    private GenJButton btReplicarMetodo;

    private GenJRadioButton checkTecnica;
    private GenJRadioButton checkRecurso;
    private GenJRadioButton checkInstrumento;

    private GenJComboBox comboMetodo;
    private GenJComboBox comboObjetivo;
    private GenJComboBox comboConteudo;
    private ObjetivoComboBoxModel objetivoComboModel;

    private JTabbedPane tabbedDetalhamento;

    private PlanoDeEnsino planoDeEnsino;

    public DetalhamentoFields() {
        this(null);
    }

    public DetalhamentoFields(PlanoDeEnsino planoDeEnsino) {
        super("Detalhamento das atividades da semana");
        this.planoDeEnsino = planoDeEnsino;
        initComponents();
    }

    private void initComponents() {
        setName("plano.detalhamento");
        setLayout(new BorderLayout(5, 5));

        add(createPanelObservacao(), BorderLayout.PAGE_START);

        JPanel panelConteudo = createPanel(new BorderLayout(5, 5));
        panelConteudo.add(createPanelConteudo(), BorderLayout.PAGE_START);

        tabbedDetalhamento = new JTabbedPane();
        panelConteudo.add(tabbedDetalhamento, BorderLayout.CENTER);
        tabbedDetalhamento.addTab("Metodologia", createPanelMetodolotias());
        tabbedDetalhamento.addTab("Objetivos", createPanelObjetivos());

        add(panelConteudo, BorderLayout.CENTER);
    }

    private JPanel createPanelObservacao() {
        JPanel panel = createPanel(new BorderLayout(5, 5));

        lblSemana = new GenJLabel();
        lblSemana.resetFontSize(20);
        lblSemana.setForeground(Color.red);
        panel.add(lblSemana, BorderLayout.PAGE_START);

        JPanel panelObserva = createPanel(new BorderLayout(5, 5));
        panelObserva.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Observações",
                TitledBorder.LEFT, TitledBorder.TOP));

        txtObservacao = new GenJList();
        txtObservacao.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        txtObservacao.setVisibleRowCount(-1);
        JScrollPane observacaoScroll = new JScrollPane(txtObservacao);
        observacaoScroll.setPreferredSize(new Dimension(400, 80));
        observacaoScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        panelObserva.add(observacaoScroll, BorderLayout.CENTER);

        panel.add(panelObserva, BorderLayout.CENTER);

        panel.add(createPanelAulas(), BorderLayout.PAGE_END);
        return panel;
    }

    private JPanel createPanelAulas() {
        JPanel panel = createPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Número de aulas",
                TitledBorder.LEFT, TitledBorder.TOP));

        GenJLabel lblNAulasP = new GenJLabel("Práticas:");
        panel.add(lblNAulasP);
        txtNAulasP = new GenJSpinner(new SpinnerNumberModel(0, 0, 20, 1));
        panel.add(txtNAulasP);

        GenJLabel lblNAulasT = new GenJLabel("Teóricas:");
        panel.add(lblNAulasT);
        txtNAulasT = new GenJSpinner(new SpinnerNumberModel(0, 0, 20, 1));
        panel.add(txtNAulasT);

        return panel;
    }

    private JPanel createPanelConteudo() {
        GenJLabel lblConteudo = new GenJLabel("Conteúdo definido na U.C.:");
        comboConteudo = new GenJComboBox(planoDeEnsino.getUnidadeCurricular().getConteudos().toArray());
        comboConteudo.setEnabled(false);
        lblConteudo.setLabelFor(comboConteudo);

        JPanel panelConteudo = createPanel();
        panelConteudo.add(lblConteudo);
        panelConteudo.add(comboConteudo);

        Border titleBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Conteúdo a ser desenvolvido",
                TitledBorder.LEFT, TitledBorder.TOP);

        txtConteudo = new GenJTextArea(4, 50);
        txtConteudo.setBorder(titleBorder);
        JScrollPane scroll = new JScrollPane(txtConteudo);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel panel = createPanel(new BorderLayout(5, 5));
        panel.add(panelConteudo, BorderLayout.PAGE_START);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createPanelMetodolotias() {
        JPanel panel = createPanel(new BorderLayout(5, 5));

        panel.setBorder(createTitleBorder("Metodologias"));

        JPanel panelMetodologia = createPanel(new GridBagLayout());
        panel.add(panelMetodologia, BorderLayout.PAGE_START);

        CheckBoxChangeListener cbcl = new CheckBoxChangeListener();
        ButtonGroup bg = new ButtonGroup();
        checkTecnica = new GenJRadioButton("Técnica", true, bg);
        checkTecnica.addChangeListener(cbcl);
        checkRecurso = new GenJRadioButton("Recurso", false, bg);
        checkRecurso.addChangeListener(cbcl);
        checkInstrumento = new GenJRadioButton("Instrumento Avaliativo", false, bg);
        checkInstrumento.addChangeListener(cbcl);

        JPanel panelMetodos = createPanel(new GridLayout(0, 1));
        panelMetodos.setBorder(createTitleBorder("Selecione um método"));
        panelMetodos.add(checkTecnica);
        panelMetodos.add(checkRecurso);
        panelMetodos.add(checkInstrumento);

        comboMetodo = new GenJComboBox();
        comboMetodo.addItemListener(new ComboItemListener());

        btReplicarMetodo = createButton(new ActionHandler(AcoesBotoes.DUPLICATE));
        btReplicarMetodo.setText("Replicar");

        JPanel panelComboAddMetodo = createPanel(new GridLayout(0, 1));
        panelComboAddMetodo.add(comboMetodo);
        panelComboAddMetodo.add(btReplicarMetodo);

        // definir a posição
        Integer col = 0, row = 0;
        GridBagConstraints cMetodologia = new GridBagConstraints();
        GridLayoutHelper.set(cMetodologia, col++, row);
        panelMetodologia.add(panelMetodos, cMetodologia);
        GridLayoutHelper.set(cMetodologia, col++, row);
        panelMetodologia.add(panelComboAddMetodo, cMetodologia);

        metodologiaTableModel = new MetodologiaTableModel();
        metodologiaTable = new JTable(metodologiaTableModel);
        metodologiaTable.addKeyListener(new KeyPressedListener());

        JScrollPane metodologiaScroll = new JScrollPane();
        metodologiaScroll.setViewportView(metodologiaTable);
        metodologiaScroll.setPreferredSize(new Dimension(250, 150));
        panel.add(metodologiaScroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createPanelObjetivos() {
        objetivoComboModel = new ObjetivoComboBoxModel();
        comboObjetivo = new GenJComboBox(objetivoComboModel);
        comboObjetivo.addItemListener(new ComboItemListener());
        comboObjetivo.setRenderer(new TextAreaItemRenderer());

        JPanel panelCombo = createPanel(new BorderLayout(5, 5));
        panelCombo.add(comboObjetivo, BorderLayout.PAGE_START);

        objetivoDetalheTableModel = new ObjetivoDetalheTableModel();
        objetivosDetalheTable = new JTable(objetivoDetalheTableModel);
        objetivosDetalheTable.addKeyListener(new KeyPressedListener());

        JScrollPane objetivoDetalhesScroll = new JScrollPane();
        objetivoDetalhesScroll.setViewportView(objetivosDetalheTable);
        objetivoDetalhesScroll.setPreferredSize(new Dimension(450, 120));

        JPanel panel = createPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Objetivos específicos",
                TitledBorder.LEFT, TitledBorder.TOP));

        panel.add(panelCombo, BorderLayout.PAGE_START);
        panel.add(objetivoDetalhesScroll, BorderLayout.CENTER);

        return panel;
    }

    public void setMetodologias(List<Metodologia> lista) {
        metodologiaTableModel = new MetodologiaTableModel(lista);
        reloadMetodologiaTable();
    }

    private void reloadMetodologiaTable() {
        metodologiaTable.setModel(metodologiaTableModel);
        metodologiaTable.repaint();
        metodologiaTable.getColumnModel().getColumn(0).setCellRenderer(new MetodologiaCellRenderer());
    }

    private void reloadObjetivoTable() {
        objetivosDetalheTable.setModel(objetivoDetalheTableModel);
        objetivosDetalheTable.repaint();
        objetivosDetalheTable.getColumnModel().getColumn(0).setCellRenderer(new TextAreaCellRenderer());
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = new HashMap();
        // seleciona todos os itens
        ObservacaoListModel obsModel = (ObservacaoListModel) txtObservacao.getModel();

        map.put("sequencia", detalhamento.getId().getSequencia());
        map.put("planoDeEnsino", this.planoDeEnsino);
        map.put("semanaLetiva", semanaLetiva);
        map.put("conteudoUC", comboConteudo.getSelectedItem());
        map.put("conteudo", txtConteudo.getText());
        map.put("observacao", obsModel.toString());
        map.put("nAulasPraticas", txtNAulasP.getValue());
        map.put("nAulasTeoricas", txtNAulasT.getValue());

        map.put("metodologias", metodologiaTableModel.getData());
        map.put("objetivoDetalhes", objetivoDetalheTableModel.getData());
        return map;
    }

    private void setSemanaLetiva(SemanaLetiva semanaLetiva) {
        this.semanaLetiva = semanaLetiva;
        lblSemana.setText(String.format("%s - Periodo: %s",
                semanaLetiva.toString(),
                semanaLetiva.getPeriodo().toString()));
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {
        List<Objetivo> listaObjetivos = (List<Objetivo>) mapValues.get("objetivos");
        String observacao = (String) mapValues.get("observacao");
        setSemanaLetiva((SemanaLetiva) mapValues.get("semanaLetiva"));
        /**
         * A sequência será a mesma da semana porque existirá somente um
         * detalhamento por semana
         */
        txtObservacao.setModel(new ObservacaoListModel(observacao));
        objetivoComboModel = new ObjetivoComboBoxModel(listaObjetivos);
        objetivoComboModel.refresh();
        comboObjetivo.setModel(objetivoComboModel);
        comboObjetivo.repaint();
    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof Detalhamento) {
            detalhamento = (Detalhamento) object;

            setSemanaLetiva(detalhamento.getSemanaLetiva());
            txtConteudo.setText(detalhamento.getConteudo());
            txtObservacao.setModel(new ObservacaoListModel(detalhamento.getObservacao()));
            txtNAulasP.setValue(detalhamento.getNAulasPraticas());
            txtNAulasT.setValue(detalhamento.getNAulasTeoricas());
            comboConteudo.setSelectedItem(detalhamento.getConteudoUC());

            // inicializa o combo de objetivos
            objetivoComboModel = new ObjetivoComboBoxModel(detalhamento.getId().getPlanoDeEnsino().getObjetivos());
            comboObjetivo.setModel(objetivoComboModel);

            metodologiaTableModel = new MetodologiaTableModel(detalhamento.getMetodologias());
            reloadMetodologiaTable();
            objetivoDetalheTableModel = new ObjetivoDetalheTableModel(detalhamento.getObjetivoDetalhes());
            reloadObjetivoTable();
            tabbedDetalhamento.setSelectedIndex(0);
        }
    }

    @Override
    public boolean isValidated() {
        return true;
    }

    @Override
    public void clearFields() {
        detalhamento = null;
        txtConteudo.setText("");
        txtObservacao = new GenJList();
        txtNAulasP.setValue(0);
        txtNAulasT.setValue(0);
        metodologiaTableModel = new MetodologiaTableModel(new ArrayList());
        objetivoDetalheTableModel = new ObjetivoDetalheTableModel(new ArrayList());
        reloadMetodologiaTable();
        reloadObjetivoTable();
    }

    @Override
    public void enableFields(boolean active) {
        txtNAulasP.setEnabled(active);
        txtNAulasT.setEnabled(active);
        txtObservacao.setEnabled(active);
        txtConteudo.setEnabled(active);
        comboMetodo.setEnabled(active);

        btReplicarMetodo.setEnabled(active && detalhamento.getId().getSequencia() == 1);

        comboObjetivo.setEnabled(active);

        checkInstrumento.setEnabled(active);
        checkRecurso.setEnabled(active);
        checkTecnica.setEnabled(active);
    }

    @Override
    public void componentShown(ComponentEvent e) {
        super.componentShown(e);
        tabbedDetalhamento.setSelectedIndex(0);
    }

    @Override
    public void initFocus() {
        txtNAulasP.requestFocusInWindow();
    }

    @Override
    public void onDuplicateAction(ActionEvent e, Object o) {
        Object source = e.getSource();
        if (source == btReplicarMetodo) {
            if (metodologiaTableModel.isEmpty()) {
                showWarningMessage("Não é possível importar dados pois a tabela de metodologias"
                        + " está vazia.");
                return;
            }

            if (confirmDialog("Confirma a replicação das metodologias para todas as demais"
                    + " semanas?")) {
                try {
                    /**
                     * Recupera a lista de detalhamento
                     */
                    DetalhamentoController detalhaCol = ControllerFactory.createDetalhamentoController();
                    List<Detalhamento> lDetalhamento = detalhaCol.listar(planoDeEnsino);

                    for (int i = 1; i < lDetalhamento.size(); i++) {
                        Detalhamento d = lDetalhamento.get(i);
                        d.getMetodologias().addAll(metodologiaTableModel.getData());
                        detalhaCol.salvar(d);
                    }
                    showInformationMessage("A replicação das metodologias foi realizada com sucesso.");
                } catch (Exception ex) {
                    showErrorMessage(ex);
                }
            }
        }
    }

    private class CheckBoxChangeListener implements ChangeListener {

        private List<BaseObject> tecnicas;
        private List<BaseObject> recursos;
        private List<BaseObject> instrumentos;

        public CheckBoxChangeListener() {
            super();
            try {
                tecnicas = ControllerFactory.createTecnicaController().listar();
                recursos = ControllerFactory.createRecursoController().listar();
                instrumentos = ControllerFactory.createInstrumentoAvaliacaoController().listar();
            } catch (Exception ex) {
                Logger.getLogger(DetalhamentoFields.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            AbstractButton aButton = (AbstractButton) e.getSource();
            ButtonModel aModel = aButton.getModel();

            if (aModel.isSelected()) {
                if (e.getSource() == checkTecnica) {
                    comboMetodo.setModel(new MetodoComboBoxModel(tecnicas));
                } else if (e.getSource() == checkRecurso) {
                    comboMetodo.setModel(new MetodoComboBoxModel(recursos));
                } else if (e.getSource() == checkInstrumento) {
                    comboMetodo.setModel(new MetodoComboBoxModel(instrumentos));
                }
            }
        }

    }

    private class KeyPressedListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                Object source = e.getSource();
                int selectedRow;
                if (source == metodologiaTable) {
                    selectedRow = metodologiaTable.getSelectedRow();
                    if (selectedRow == -1) {
                        return;
                    }
                    metodologiaTableModel.removeRow(selectedRow);
                    reloadMetodologiaTable();
                } else if (source == objetivosDetalheTable) {
                    selectedRow = objetivosDetalheTable.getSelectedRow();
                    if (selectedRow == -1) {
                        return;
                    }
                    objetivoDetalheTableModel.removeRow(selectedRow);
                    reloadObjetivoTable();
                }
            }
        }
    }

    private class ComboItemListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent ie) {
            Object source = ie.getSource();
            if (ie.getStateChange() == ItemEvent.SELECTED) {
                if (source == comboMetodo) {
                    TipoMetodo tipoMetodo = null;
                    if (checkTecnica.isSelected()) {
                        tipoMetodo = TipoMetodo.TECNICA;
                    } else if (checkRecurso.isSelected()) {
                        tipoMetodo = TipoMetodo.RECURSO;
                    } else if (checkInstrumento.isSelected()) {
                        tipoMetodo = TipoMetodo.INSTRUMENTO;
                    }

                    BaseObject selected = (BaseObject) comboMetodo.getSelectedItem();
                    if (selected != null && tipoMetodo != null) {
                        Integer id = 1;
                        if (!metodologiaTableModel.isEmpty()) {
                            Metodologia mTemp = metodologiaTableModel.getMax(Comparator.comparing(a -> a.getId().getSequencia()));
                            id = mTemp.getId().getSequencia() + 1;
                        }
                        Metodologia metodologia = MetodologiaFactory
                                .getInstance()
                                .createObject(new MetodologiaId(id, detalhamento),
                                        tipoMetodo, selected);

                        if (metodologiaTableModel.exists(metodologia) < 0) {
                            metodologiaTableModel.addRow(metodologia);
                            reloadMetodologiaTable();
                        } else {
                            showWarningMessage("Método já adicionado");
                            comboMetodo.requestFocusInWindow();
                        }
                    }
                } else if (source == comboObjetivo) {
                    Objetivo obj = (Objetivo) comboObjetivo.getSelectedItem();
                    if (obj != null) {
                        ObjetivoDetalhe odetalhe = ObjetivoDetalheFactory.getInstance()
                                .createObject(obj, detalhamento);
                        if (objetivoDetalheTableModel.exists(odetalhe) < 0) {
                            objetivoDetalheTableModel.addRow(odetalhe);
                            reloadObjetivoTable();
                        } else {
                            showWarningMessage("Objetivo já adicionado");
                            comboObjetivo.requestFocusInWindow();
                        }
                    }
                }
            }
        }

    }

    private class ObservacaoListModel extends AbstractListModel {

        List<String> lista;

        public ObservacaoListModel(String data) {
            String datas[] = data.split(",");
            lista = new ArrayList<>();
            for (int i = 0; i < datas.length; i++) {
                lista.add(datas[i]);
            }
        }

        @Override
        public int getSize() {
            return lista.size();
        }

        @Override
        public Object getElementAt(int index) {
            return lista.get(index);
        }

        @Override
        public String toString() {
            return lista.toString().replaceAll("[\\[|\\]]", " ").trim();
        }
    }

}
