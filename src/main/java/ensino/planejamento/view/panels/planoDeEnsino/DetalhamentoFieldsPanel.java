/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.planoDeEnsino;

import ensino.components.GenJButton;
import ensino.components.GenJComboBox;
import ensino.components.GenJLabel;
import ensino.components.GenJList;
import ensino.components.GenJRadioButton;
import ensino.components.GenJSpinner;
import ensino.components.GenJTextArea;
import ensino.configuracoes.model.SemanaLetiva;
import ensino.configuracoes.view.models.MetodoComboBoxModel;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.BaseObject;
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.controller.MetodologiaController;
import ensino.planejamento.controller.ObjetivoDetalheController;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.Metodologia;
import ensino.planejamento.model.MetodologiaFactory;
import ensino.planejamento.model.Objetivo;
import ensino.planejamento.model.ObjetivoDetalhe;
import ensino.planejamento.model.ObjetivoDetalheFactory;
import ensino.planejamento.view.models.MetodologiaTableModel;
import ensino.planejamento.view.models.ObjetivoComboBoxModel;
import ensino.planejamento.view.models.ObjetivoDetalheTableModel;
import ensino.planejamento.view.renderer.MetodologiaCellRenderer;
import ensino.planejamento.view.renderer.ObjetivoDetalheCellRenderer;
import ensino.util.types.TipoMetodo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.AbstractListModel;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author nicho
 */
public class DetalhamentoFieldsPanel extends DefaultFieldsPanel {

    private Integer sequencia;
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
    private ObjetivoComboBoxModel objetivoComboModel;

    private JTabbedPane tabbedDetalhamento;
    private DetalhamentoPanel parent;

    public DetalhamentoFieldsPanel() {
        this(null);
    }

    public DetalhamentoFieldsPanel(DetalhamentoPanel parent) {
        super("Detalhamento das atividades da semana");
        this.parent = parent;
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        int col = 0, row = 0;
        lblSemana = new GenJLabel();
        lblSemana.resetFontSize(20);
        lblSemana.setForeground(Color.red);
        GridLayoutHelper.set(c, col, row++);
        add(lblSemana, c);

        GridLayoutHelper.set(c, col, row++);
        add(createPanelObservacao(), c);

        tabbedDetalhamento = new JTabbedPane();

        GridLayoutHelper.set(c, col, row++);
        add(tabbedDetalhamento, c);

        JPanel panelConteudo = new JPanel(new BorderLayout(5, 5));
        tabbedDetalhamento.addTab("Conteúdo", panelConteudo);
        panelConteudo.add(createPanelAulas(), BorderLayout.PAGE_START);
        panelConteudo.add(createPanelConteudo(), BorderLayout.CENTER);

        tabbedDetalhamento.addTab("Metodologia", createPanelMetodolotias());
        tabbedDetalhamento.addTab("Objetivos", createPanelObjetivos());

        btReplicarMetodo.addActionListener(new ButtonAction());

    }

    private void addKeyEventTo(JTable table, String keyString) {
        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Object source = ae.getSource();
                int selectedRow;
                if (source == metodologiaTable) {
                    selectedRow = metodologiaTable.getSelectedRow();
                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(getParent(),
                                "Você não selecionou o Método que será removido.\n"
                                + "Favor, clique sobre um Método!",
                                "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    metodologiaTableModel.removeRow(selectedRow);
                    reloadMetodologiaTable();
                } else if (source == objetivosDetalheTable) {
                    selectedRow = objetivosDetalheTable.getSelectedRow();
                    if (selectedRow == -1) {
                        JOptionPane.showMessageDialog(getParent(),
                                "Você não selecionou o Objetivo que será removido.\nFavor, clique sobre um Objetivo!",
                                "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    objetivoDetalheTableModel.removeRow(selectedRow);
                    reloadObjetivoTable();
                }
            }
        };
        table.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(keyString), "evento");
        table.getActionMap().put("evento", action);
    }

    private JPanel createPanelObservacao() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JPanel panelObserva = new JPanel(new BorderLayout(5, 5));
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

        panel.add(panelObserva);
        return panel;
    }

    private JPanel createPanelAulas() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
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
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), "Conteúdo a ser desenvolvido",
                TitledBorder.LEFT, TitledBorder.TOP));

        txtConteudo = new GenJTextArea(5, 30);
        JScrollPane conteudoScroll = new JScrollPane(txtConteudo);
        conteudoScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(conteudoScroll);
        return panel;
    }

    private JPanel createPanelMetodolotias() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        panel.setBorder(createTitleBorder("Metodologias"));

        JPanel panelMetodologia = new JPanel(new GridBagLayout());
        panel.add(panelMetodologia, BorderLayout.PAGE_START);

        // definir a posição
        JPanel panelMetodos = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        GridBagConstraints cMetodologia = new GridBagConstraints();
        GridLayoutHelper.set(cMetodologia, 0, 0);
        panelMetodologia.add(panelMetodos, cMetodologia);
        panelMetodos.setBorder(createTitleBorder("Selecione um método"));

        CheckBoxChangeListener cbcl = new CheckBoxChangeListener();
        ButtonGroup bg = new ButtonGroup();
        checkTecnica = new GenJRadioButton("Técnica", true, bg);
        checkTecnica.addChangeListener(cbcl);
        checkRecurso = new GenJRadioButton("Recurso", false, bg);
        checkRecurso.addChangeListener(cbcl);
        checkInstrumento = new GenJRadioButton("Instrumento Avaliativo", false, bg);
        checkInstrumento.addChangeListener(cbcl);
        panelMetodos.add(checkTecnica);
        panelMetodos.add(checkRecurso);
        panelMetodos.add(checkInstrumento);

        JPanel panelComboAddMetodo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        GridLayoutHelper.set(cMetodologia, 0, 1);
        panelMetodologia.add(panelComboAddMetodo, cMetodologia);

        comboMetodo = new GenJComboBox();
        comboMetodo.addItemListener(new ComboItemListener());
        btReplicarMetodo = new GenJButton("Replicar");

        panelComboAddMetodo.add(comboMetodo);
        panelComboAddMetodo.add(btReplicarMetodo);

        metodologiaTableModel = new MetodologiaTableModel();
        metodologiaTable = new JTable(metodologiaTableModel);
        metodologiaTable.addKeyListener(new KeyPressedListener());

        JScrollPane metodologiaScroll = new JScrollPane();
        metodologiaScroll.setViewportView(metodologiaTable);
        metodologiaScroll.setPreferredSize(new Dimension(250, 120));
        panel.add(metodologiaScroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createPanelObjetivos() {
        objetivoComboModel = new ObjetivoComboBoxModel();
        comboObjetivo = new GenJComboBox(objetivoComboModel);
        comboObjetivo.addItemListener(new ComboItemListener());

        JPanel panelCombo = new JPanel(new BorderLayout(5, 5));
        panelCombo.add(comboObjetivo, BorderLayout.PAGE_START);

        objetivoDetalheTableModel = new ObjetivoDetalheTableModel();
        objetivosDetalheTable = new JTable(objetivoDetalheTableModel);
        objetivosDetalheTable.addKeyListener(new KeyPressedListener());

        JScrollPane objetivoDetalhesScroll = new JScrollPane();
        objetivoDetalhesScroll.setViewportView(objetivosDetalheTable);
        objetivoDetalhesScroll.setPreferredSize(new Dimension(450, 120));

        JPanel panel = new JPanel(new BorderLayout(5, 5));
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
        objetivosDetalheTable.getColumnModel().getColumn(0).setCellRenderer(new ObjetivoDetalheCellRenderer());
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = new HashMap();
        // seleciona todos os itens
        ObservacaoListModel obsModel = (ObservacaoListModel) txtObservacao.getModel();

        map.put("sequencia", sequencia);
        map.put("semanaLetiva", semanaLetiva);
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
        sequencia = semanaLetiva.getId().getId();
        txtObservacao.setModel(new ObservacaoListModel(observacao));
        objetivoComboModel = new ObjetivoComboBoxModel(listaObjetivos);
        objetivoComboModel.refresh();
        comboObjetivo.setModel(objetivoComboModel);
        comboObjetivo.repaint();
    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof Detalhamento) {
            Detalhamento detalhe = (Detalhamento) object;

            sequencia = detalhe.getId().getSequencia();
            setSemanaLetiva(detalhe.getSemanaLetiva());
            txtConteudo.setText(detalhe.getConteudo());
            txtObservacao.setModel(new ObservacaoListModel(detalhe.getObservacao()));
            txtNAulasP.setValue(detalhe.getNAulasPraticas());
            txtNAulasT.setValue(detalhe.getNAulasTeoricas());
            try {
                /**
                 * Recupera a lista de metodologias e os objetivos do
                 * detalhamento
                 */
                if (detalhe.getMetodologias().isEmpty()) {
                    MetodologiaController metodologiaCol = ControllerFactory.createMetodologiaController();
                    detalhe.setMetodologias(metodologiaCol.listar(detalhe));
                }

                if (detalhe.getObjetivoDetalhes().isEmpty()) {
                    ObjetivoDetalheController objetivoCol = ControllerFactory.createObjetivoDetalheController();
                    detalhe.setObjetivoDetalhes(objetivoCol.listar(detalhe));
                }
            } catch (Exception ex) {
                Logger.getLogger(DetalhamentoFieldsPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            metodologiaTableModel = new MetodologiaTableModel(detalhe.getMetodologias());
            reloadMetodologiaTable();
            objetivoDetalheTableModel = new ObjetivoDetalheTableModel(detalhe.getObjetivoDetalhes());
            reloadObjetivoTable();
        }
    }

    @Override
    public boolean isValidated() {
        return true;
    }

    @Override
    public void clearFields() {
        sequencia = null;
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
        btReplicarMetodo.setEnabled(active && sequencia == 1);

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

    private class CheckBoxChangeListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            AbstractButton aButton = (AbstractButton) e.getSource();
            ButtonModel aModel = aButton.getModel();

            try {
                if (aModel.isSelected()) {
                    if (e.getSource() == checkTecnica) {
                        comboMetodo.setModel(new MetodoComboBoxModel(ControllerFactory.createTecnicaController()));
                    } else if (e.getSource() == checkRecurso) {
                        comboMetodo.setModel(new MetodoComboBoxModel(ControllerFactory.createRecursoController()));
                    } else if (e.getSource() == checkInstrumento) {
                        comboMetodo.setModel(new MetodoComboBoxModel(ControllerFactory.createInstrumentoAvaliacaoController()));
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(DetalhamentoFieldsPanel.class.getName()).log(Level.SEVERE, null, ex);
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

    private class ButtonAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btReplicarMetodo) {
                if (metodologiaTableModel.isEmpty()) {
                    JOptionPane.showMessageDialog(getParent(),
                            "Não é possível importar dados pois a tabela de metodologias"
                            + " está vazia.",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int confirmForm = JOptionPane.showConfirmDialog(DetalhamentoFieldsPanel.this,
                        "Confirma a replicação das metodologias para todas as demais"
                        + " semanas?", "Confirmação", JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (confirmForm == JOptionPane.OK_OPTION) {
                    parent.replicarMetodologiasParaTodosOsDetalhamentos(metodologiaTableModel.getData());

                    JOptionPane.showMessageDialog(getParent(),
                            "A replicação das metodologias foi realizada com sucesso.",
                            "Informação", JOptionPane.INFORMATION_MESSAGE);
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
                                .createObject(id, tipoMetodo, selected);

                        if (metodologiaTableModel.exists(metodologia) < 0) {
                            metodologiaTableModel.addRow(metodologia);
                            reloadMetodologiaTable();
                        } else {
                            JOptionPane.showMessageDialog(tabbedDetalhamento,
                                    "Método já adicionado", "Aviso", JOptionPane.WARNING_MESSAGE);
                            comboMetodo.requestFocusInWindow();
                        }
                    }
                } else if (source == comboObjetivo) {
                    Objetivo obj = (Objetivo) comboObjetivo.getSelectedItem();
                    if (obj != null) {
                        ObjetivoDetalhe odetalhe = ObjetivoDetalheFactory.getInstance().createObject();
                        odetalhe.getId().setObjetivo(obj);
                        if (objetivoDetalheTableModel.exists(odetalhe) < 0) {
                            objetivoDetalheTableModel.addRow(odetalhe);
                            reloadObjetivoTable();
                        } else {
                            JOptionPane.showMessageDialog(tabbedDetalhamento,
                                    "Objetivo já adicionado", "Aviso", JOptionPane.WARNING_MESSAGE);
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
