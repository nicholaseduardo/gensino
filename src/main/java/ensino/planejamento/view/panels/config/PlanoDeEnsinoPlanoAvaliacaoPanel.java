/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.config;

import ensino.components.GenJButton;
import ensino.components.GenJComboBox;
import ensino.components.GenJLabel;
import ensino.components.GenJSpinner;
import ensino.components.GenJTextField;
import ensino.configuracoes.model.EtapaEnsino;
import ensino.configuracoes.model.InstrumentoAvaliacao;
import ensino.configuracoes.model.InstrumentoAvaliacaoFactory;
import ensino.configuracoes.model.NivelEnsino;
import ensino.configuracoes.view.models.EtapaEnsinoComboBoxModel;
import ensino.configuracoes.view.models.MetodoComboBoxModel;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.BaseObject;
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.controller.PlanoAvaliacaoController;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.Metodologia;
import ensino.planejamento.model.Objetivo;
import ensino.planejamento.model.PlanoAvaliacao;
import ensino.planejamento.model.PlanoAvaliacaoFactory;
import ensino.planejamento.model.PlanoAvaliacaoId;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.models.ObjetivoComboBoxModel;
import ensino.planejamento.view.models.PlanoAvaliacaoTableModel;
import ensino.planejamento.view.renderer.PlanoAvaliacaoCellRenderer;
import ensino.util.types.TipoMetodo;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author nicho
 */
public class PlanoDeEnsinoPlanoAvaliacaoPanel extends DefaultFieldsPanel {

    private PlanoDeEnsino planoDeEnsino;

    private GenJTextField txtId;
    private GenJTextField txtNome;
    private GenJComboBox comboEtapaEnsino;
    private EtapaEnsinoComboBoxModel modelEtapaEnsino;

    private GenJSpinner spinPeso;
    private GenJSpinner spinValor;
    private GenJSpinner spinData;
    private GenJComboBox comboInstrumento;
    private GenJComboBox comboObjetivo;
    private ObjetivoComboBoxModel objetivoComboModel;

    private GenJButton btAdd;
    private GenJButton btDel;
    private GenJButton btNew;
    private GenJButton btImportar;
    private GenJButton btUpdate;

    private JTable planoAvaliacaoTable;
    private PlanoAvaliacaoTableModel planoAvaliacaoTableModel;

    /**
     * atributos adicionais para tratativa de automatização e atribuição de
     * valores
     */
    private List<Detalhamento> listaDetalhamento;

    public PlanoDeEnsinoPlanoAvaliacaoPanel() {
        super("Planejamento de Avaliações");
        initComponents();
    }

    private void initComponents() {
        setName("plano.avaliacoes");
        setLayout(new BorderLayout(5, 5));
        /**
         * A tabela foi criada antes por questões de processo lógico da
         * construção da ação dos botões.
         */
        add(createTablePane(), BorderLayout.CENTER);

        JPanel panelDados = new JPanel(new BorderLayout(5, 5));
        panelDados.add(createFields(), BorderLayout.CENTER);
        panelDados.add(createButtonPanel(), BorderLayout.PAGE_END);

        add(panelDados, BorderLayout.PAGE_START);
    }

    private JPanel createFields() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        try {
            comboEtapaEnsino = new GenJComboBox();

            txtId = new GenJTextField(5, false);
            txtId.setEnabled(false);
            txtNome = new GenJTextField(20, true);
            comboInstrumento = new GenJComboBox(new MetodoComboBoxModel(TipoMetodo.INSTRUMENTO));
            spinPeso = new GenJSpinner(new SpinnerNumberModel(0.0, 0.0, null, 0.1));
            spinPeso.setEditor(new JSpinner.NumberEditor(spinPeso, "0.0"));
            spinValor = new GenJSpinner(new SpinnerNumberModel(0.0, 0.0, null, 0.1));
            spinValor.setEditor(new JSpinner.NumberEditor(spinValor, "0.0"));
            Calendar cal = Calendar.getInstance();
            spinData = new GenJSpinner(new SpinnerDateModel(cal.getTime(),
                    null, null, Calendar.DATE));
            spinData.setEditor(new JSpinner.DateEditor(spinData, "dd/MM/yyyy"));

            objetivoComboModel = new ObjetivoComboBoxModel();
            comboObjetivo = new GenJComboBox(objetivoComboModel);

            int col = 0, row = 0;
            GridLayoutHelper.set(c, col, row++, 4, 1, GridBagConstraints.LINE_START);
            panel.add(new GenJLabel("Identificação:"), c);
            GridLayoutHelper.set(c, col, row++, 4, 1, GridBagConstraints.LINE_START);
            c.fill = GridBagConstraints.HORIZONTAL;
            panel.add(txtId, c);

            col = 0;

            GridLayoutHelper.set(c, col++, row, 2, 1, GridBagConstraints.LINE_START);
            panel.add(new GenJLabel("Descrição: "), c);
            GridLayoutHelper.set(c, ++col, row++, 2, 1, GridBagConstraints.LINE_START);
            panel.add(new GenJLabel("Etapa: "), c);

            col = 0;
            GridLayoutHelper.set(c, col++, row, 2, 1, GridBagConstraints.LINE_START);
            c.fill = GridBagConstraints.HORIZONTAL;
            panel.add(txtNome, c);
            GridLayoutHelper.set(c, ++col, row++, 2, 1, GridBagConstraints.LINE_START);
            panel.add(comboEtapaEnsino, c);

            col = 0;
            GridLayoutHelper.set(c, col++, row);
            panel.add(new GenJLabel("Instrumento de Avaliação: "), c);
            GridLayoutHelper.set(c, col++, row);
            panel.add(new GenJLabel("Data prevista: "), c);
            GridLayoutHelper.set(c, col++, row);
            panel.add(new GenJLabel("Peso: "), c);
            GridLayoutHelper.set(c, col, row++);
            panel.add(new GenJLabel("Valor: "), c);

            col = 0;
            GridLayoutHelper.set(c, col++, row);
            c.fill = GridBagConstraints.HORIZONTAL;
            panel.add(comboInstrumento, c);
            GridLayoutHelper.set(c, col++, row);
            panel.add(spinData, c);
            GridLayoutHelper.set(c, col++, row);
            panel.add(spinPeso, c);
            GridLayoutHelper.set(c, col, row++);
            panel.add(spinValor, c);

            col = 0;
            GridLayoutHelper.set(c, col, row++, 4, 1, GridBagConstraints.LINE_START);
            panel.add(new GenJLabel("Objetivo Específico: "), c);
            GridLayoutHelper.set(c, col, row++, 4, 1, GridBagConstraints.LINE_START);
            c.fill = GridBagConstraints.HORIZONTAL;
            panel.add(comboObjetivo, c);

        } catch (Exception ex) {
            showErrorMessage(ex);
        }
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));

        btAdd = new GenJButton("Adicionar", new ImageIcon(getClass().getResource(getImageSourceAdd())));
        btUpdate = new GenJButton("Atualizar", new ImageIcon(getClass().getResource(getImageSourceUpdate())));
        btDel = new GenJButton("Remover", new ImageIcon(getClass().getResource(getImageSourceDel())));
        btNew = new GenJButton("Novo", new ImageIcon(getClass().getResource(getImageSourceNew())));
        btImportar = new GenJButton("Importar do detalhamento", new ImageIcon(getClass().getResource(getImageSourceImport())));
        panel.add(btNew);
        panel.add(btAdd);
        panel.add(btUpdate);
        panel.add(btDel);
        panel.add(btImportar);

        ButtonAction buttonAction = new ButtonAction();
        btNew.addActionListener(buttonAction);
        btAdd.addActionListener(buttonAction);
        btUpdate.addActionListener(buttonAction);
        btDel.addActionListener(buttonAction);
        btImportar.addActionListener(buttonAction);

        return panel;
    }

    private JScrollPane createTablePane() {
        planoAvaliacaoTable = new JTable();
        ListSelectionModel cellSelectionModel = planoAvaliacaoTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        planoAvaliacaoTable.getSelectionModel().addListSelectionListener(new SelectionListener());

        planoAvaliacaoTableModel = new PlanoAvaliacaoTableModel();
        refreshPlanoAvaliacao();

        JScrollPane planoAvaliacaoScroll = new JScrollPane();
        planoAvaliacaoScroll.setViewportView(planoAvaliacaoTable);
        return planoAvaliacaoScroll;
    }

    public void setData(List<PlanoAvaliacao> data) {
        planoAvaliacaoTableModel = new PlanoAvaliacaoTableModel(data);
        refreshPlanoAvaliacao();
    }

    private void refreshPlanoAvaliacao() {
        planoAvaliacaoTable.setModel(planoAvaliacaoTableModel);
        planoAvaliacaoTable.getColumnModel().getColumn(0).setCellRenderer(new PlanoAvaliacaoCellRenderer());
        planoAvaliacaoTable.repaint();
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = new HashMap();
        map.put("planoAvaliacoes", planoAvaliacaoTableModel.getData());
        return map;
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {
        listaDetalhamento = (List<Detalhamento>) mapValues.get("detalhamentos");
        List<Objetivo> listaObjetivos = (List<Objetivo>) mapValues.get("objetivos");
        objetivoComboModel = new ObjetivoComboBoxModel(listaObjetivos);
        objetivoComboModel.refresh();
        comboObjetivo.setModel(objetivoComboModel);
        comboObjetivo.repaint();

        refreshPlanoAvaliacao();
    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof PlanoDeEnsino) {
            try {
                planoDeEnsino = (PlanoDeEnsino) object;

                modelEtapaEnsino = new EtapaEnsinoComboBoxModel(
                        planoDeEnsino.getUnidadeCurricular().getCurso().getNivelEnsino()
                );
                comboEtapaEnsino.setModel(modelEtapaEnsino);
                comboEtapaEnsino.repaint();

                setData(planoDeEnsino.getPlanosAvaliacoes());
                clearLocalFields();
                enableLocalButtons(Boolean.TRUE);

                objetivoComboModel = new ObjetivoComboBoxModel(planoDeEnsino.getObjetivos());
                objetivoComboModel.refresh();
                comboObjetivo.setModel(objetivoComboModel);
                comboObjetivo.repaint();
            } catch (Exception ex) {
                showErrorMessage(ex);
            }
        }
    }

    @Override
    public boolean isValidated() {
        String msg = " ";
        Double peso = (Double) spinPeso.getValue();
        Double valor = (Double) spinValor.getValue();
        if (comboEtapaEnsino.getSelectedItem() == null) {
            msg = "O campo Etapa de Ensino não foi selecionado.";
            comboEtapaEnsino.requestFocusInWindow();
        } else if ("".equals(txtNome.getText())) {
            msg = "O campo nome não foi preenchido.";
            txtNome.requestFocusInWindow();
        } else if (peso == 0.0) {
            msg = "O campo peso deve ser maior do que ZERO.";
            spinPeso.requestFocusInWindow();
        } else if (valor == 0.0) {
            msg = "O campo valor deve ser maior do que ZERO.";
            spinValor.requestFocusInWindow();
        } else if (comboInstrumento.getSelectedItem() == null) {
            msg = "O instrumento de avaliação não foi selecionado.";
            comboInstrumento.requestFocusInWindow();
        } else {
            return true;
        }
        showInformationMessage(msg);
        return false;
    }

    @Override
    public void clearFields() {
        clearLocalFields();
        setData(new ArrayList());
    }

    private void clearLocalFields() {
        txtId.setText("");
        comboEtapaEnsino.setSelectedItem(null);
        comboEtapaEnsino.repaint();
        txtNome.setText("");
        comboInstrumento.setSelectedItem(null);
        comboInstrumento.repaint();
        comboObjetivo.setSelectedItem(null);
        comboObjetivo.repaint();
        Calendar cal = Calendar.getInstance();
        spinData.setValue(cal.getTime());
        spinPeso.setValue(0.0);
        spinValor.setValue(0.0);
    }

    @Override
    public void enableFields(boolean active) {
        comboEtapaEnsino.setEnabled(active);
        txtNome.setEnabled(active);
        comboInstrumento.setEnabled(active);
        comboObjetivo.setEnabled(active);
        spinData.setEnabled(active);
        spinPeso.setEnabled(active);
        spinValor.setEnabled(active);

        planoAvaliacaoTable.setEnabled(active);
        enableLocalButtons(active);
    }

    private void enableLocalButtons(Boolean active) {
        Boolean status = "".equals(txtId.getText());

        btAdd.setEnabled(active && status);
        btNew.setEnabled(active && !status);
        btUpdate.setEnabled(active && !status);
        btDel.setEnabled(active && !status);
        btImportar.setEnabled(active && status);
    }

    @Override
    public void initFocus() {
        txtNome.requestFocusInWindow();
    }

    private class ButtonAction implements ActionListener {

        private Long sequencia;

        public ButtonAction() {
            sequencia = 1L;
        }

        private void clear() {
            clearLocalFields();
            enableLocalButtons(Boolean.TRUE);
            initFocus();
        }

        private PlanoAvaliacao createPlanoAvaliacaoFromFields(PlanoAvaliacaoController col) {
            String sSequencia = txtId.getText();
            Long seq = sSequencia.matches("\\d+") ? Long.parseLong(sSequencia) : null;
            PlanoAvaliacao o = PlanoAvaliacaoFactory.getInstance()
                    .createObject(
                            new PlanoAvaliacaoId(seq, planoDeEnsino),
                            txtNome.getText(),
                            comboEtapaEnsino.getSelectedItem(),
                            spinPeso.getValue(),
                            spinValor.getValue(),
                            spinData.getValue(),
                            comboInstrumento.getSelectedItem(),
                            comboObjetivo.getSelectedItem());
            /**
             * Cria as avaliações
             */
            o.criarAvaliacoes(planoDeEnsino.getTurma().getEstudantes());
            try {
                col.salvar(o);
            } catch (Exception ex) {
                showErrorMessage(ex);
            }

            return o;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                PlanoAvaliacaoController col = ControllerFactory.createPlanoAvaliacaoController();
                Object source = e.getSource();
                int selectedRow = planoAvaliacaoTable.getSelectedRow();
                if (source == btUpdate) {
                    planoAvaliacaoTableModel.updateRow(selectedRow, createPlanoAvaliacaoFromFields(col));
                } else if (source == btAdd && isValidated()) {
                    Long id = 1L;
                    if (!planoAvaliacaoTableModel.isEmpty()) {
                        /**
                         * Procedimento realizado para gerar a chave única de cada
                         * objetivo
                         */
                        PlanoAvaliacao paTemp = planoAvaliacaoTableModel.getMax(Comparator.comparing(a -> a.getId().getSequencia()));
                        id = paTemp.getId().getSequencia() + 1;
                    }
                    /**
                     * atribui o valor do ID ao campo para reaproveitar o método de
                     * criação do objeto Atividade
                     */
                    sequencia = id;
                    planoAvaliacaoTableModel.addRow(createPlanoAvaliacaoFromFields(col));
                } else if (source == btDel) {
                    try {
                        if (selectedRow < 0) {
                            showInformationMessage("Nenhuma avaliação foi selecionada para remoção");
                            return;
                        }
                        PlanoAvaliacao o = planoAvaliacaoTableModel.getRow(selectedRow);
                        col.remover(o);
                        
                        planoAvaliacaoTableModel.removeRow(selectedRow);
                    } catch (Exception ex) {
                        showErrorMessage(ex);
                    }
                } else if (source == btImportar) {
                    // verifica se existem dados na lista de detalhamento
                    listaDetalhamento = planoDeEnsino.getDetalhamentos();
                    if (listaDetalhamento.isEmpty()) {
                        showWarningMessage("Não existem lançamentos de detalhamento para geração automática de avaliações");
                        return;
                    }
                    if (!planoAvaliacaoTableModel.isEmpty()) {
                        showWarningMessage("Já existem avalições lançadas. Essa operação lançará novas avaliações "
                                + "no final da lista.");
                    }
                    
                    NivelEnsino ne = planoDeEnsino.getUnidadeCurricular().getCurso().getNivelEnsino();
                    EtapaEnsino etapaPadrao = ne.getEtapas().get(0);
                    
                    listaDetalhamento.forEach((detalhe) -> {
                        // para cada detalhe, verifica-se os métodos vinculados
                        List<Metodologia> lMetodologia = detalhe.getMetodologias();
                        lMetodologia.forEach((metodologia) -> {
                            // verifica se a metodologia é de avaliacao
                            if (metodologia.getTipo().equals(TipoMetodo.INSTRUMENTO)) {
                                try {
                                    BaseObject bo = metodologia.getMetodo();
                                    InstrumentoAvaliacao instrumentoAvaliacao = InstrumentoAvaliacaoFactory.getInstance()
                                            .createObject(bo.getId(), bo.getNome());
                                    // Cria a avaliação básica a partir do método de avaliação
                                    PlanoAvaliacao plano = PlanoAvaliacaoFactory.getInstance()
                                            .createObject(
                                                    new PlanoAvaliacaoId(sequencia, planoDeEnsino),
                                                    String.format("%s %d",
                                                            metodologia.getMetodo().getNome(),
                                                            sequencia++
                                                    ),
                                                    etapaPadrao,
                                                    1.0, 10.0,
                                                    detalhe.getSemanaLetiva().getPeriodo().getAte(),
                                                    instrumentoAvaliacao,
                                                    !detalhe.getObjetivoDetalhes().isEmpty()
                                                            ? detalhe.getObjetivoDetalhes().get(0).getObjetivo() : null
                                            );
                                    /**
                                     * Cria as avaliações do plano de avaliação por
                                     * estudante
                                     */
                                    plano.criarAvaliacoes(planoDeEnsino.getTurma().getEstudantes());
                                    col.salvar(plano);
                                    // adiciona o plano na tabela
                                    planoAvaliacaoTableModel.addRow(plano);
                                } catch (Exception ex) {
                                    showErrorMessage(ex);
                                }
                                
                            }
                        });
                    });
                }
                col.close();
                clear();
            } catch (Exception ex) {
                showErrorMessage(ex);
            }
        }

    }

    private class SelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            int index = planoAvaliacaoTable.getSelectedRow();
            if (planoAvaliacaoTable.getRowSelectionAllowed()
                    && planoAvaliacaoTable.isEnabled() && index >= 0) {
                PlanoAvaliacao plano = (PlanoAvaliacao) planoAvaliacaoTableModel.getRow(index);

                txtId.setText(plano.getId().getSequencia().toString());
                txtNome.setText(plano.getNome());
                comboInstrumento.setSelectedItem(plano.getInstrumentoAvaliacao());
                comboInstrumento.repaint();

                spinPeso.setValue(plano.getPeso());
                spinValor.setValue(plano.getValor());
                spinData.setValue(plano.getData());

                EtapaEnsinoComboBoxModel model = (EtapaEnsinoComboBoxModel) comboEtapaEnsino.getModel();
                model.setSelectedItem(plano.getEtapaEnsino());
                comboEtapaEnsino.repaint();

                comboObjetivo.setSelectedItem(plano.getObjetivo());
                comboObjetivo.repaint();

                refreshPlanoAvaliacao();
                enableLocalButtons(Boolean.TRUE);
                initFocus();
            }
        }

    }

}
