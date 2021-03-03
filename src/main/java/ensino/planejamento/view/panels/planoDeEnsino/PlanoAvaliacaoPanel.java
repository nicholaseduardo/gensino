/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.planoDeEnsino;

import ensino.components.GenJButton;
import ensino.components.GenJComboBox;
import ensino.components.GenJLabel;
import ensino.components.GenJSpinner;
import ensino.components.GenJTextField;
import ensino.configuracoes.model.InstrumentoAvaliacao;
import ensino.configuracoes.view.models.MetodoComboBoxModel;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.Objetivo;
import ensino.planejamento.model.PlanoAvaliacao;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.models.ObjetivoComboBoxModel;
import ensino.planejamento.view.models.PlanoAvaliacaoTableModel;
import ensino.planejamento.view.renderer.PlanoAvaliacaoCellRenderer;
import ensino.util.types.Bimestre;
import ensino.util.types.TipoMetodo;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
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
public class PlanoAvaliacaoPanel extends DefaultFieldsPanel {

    private GenJTextField txtNome;
    private GenJComboBox comboBimestre;

    private GenJSpinner spinPeso;
    private GenJSpinner spinValor;
    private GenJSpinner spinData;
    private GenJComboBox comboInstrumento;
    private GenJComboBox comboObjetivo;
    private ObjetivoComboBoxModel objetivoComboModel;

    private GenJButton btAdd;
    private GenJButton btDel;
    private GenJButton btClear;
    private GenJButton btImportar;

    private JTable planoAvaliacaoTable;
    private PlanoAvaliacaoTableModel planoAvaliacaoTableModel;

    /**
     * atributos adicionais para tratativa de automatização e atribuição de
     * valores
     */
    private List<Detalhamento> listaDetalhamento;

    public PlanoAvaliacaoPanel() {
        super("Planejamento de Avaliações");
        initComponents();
    }

    private void initComponents() {
        setName("plano.avaliacoes");
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        /**
         * A tabela foi criada antes por questões de processo lógico da
         * construção da ação dos botões.
         */
        panel.add(createTablePane(), BorderLayout.CENTER);

        JPanel panelDados = new JPanel(new BorderLayout(5, 5));
        panelDados.add(createFields(), BorderLayout.CENTER);
        panelDados.add(createButtonPanel(), BorderLayout.PAGE_END);

        panel.add(panelDados, BorderLayout.PAGE_START);
        add(panel);
    }

    private JPanel createFields() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        try {
            comboBimestre = new GenJComboBox(Bimestre.values());

            txtNome = new GenJTextField(20, true);
            comboInstrumento = new GenJComboBox(new MetodoComboBoxModel(ControllerFactory.createInstrumentoAvaliacaoController()));
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
            GridLayoutHelper.set(c, col++, row, 2, 1, GridBagConstraints.LINE_START);
            panel.add(new GenJLabel("Descrição: "), c);
            GridLayoutHelper.set(c, ++col, row++, 2, 1, GridBagConstraints.LINE_START);
            panel.add(new GenJLabel("Bimestre: "), c);

            col = 0;
            GridLayoutHelper.set(c, col++, row, 2, 1, GridBagConstraints.LINE_START);
            c.fill = GridBagConstraints.HORIZONTAL;
            panel.add(txtNome, c);
            GridLayoutHelper.set(c, ++col, row++, 2, 1, GridBagConstraints.LINE_START);
            panel.add(comboBimestre, c);

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
            Logger.getLogger(PlanoAvaliacaoPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));

        btAdd = new GenJButton("Adicionar", new ImageIcon(getClass().getResource(getImageSourceAdd())));
        btDel = new GenJButton("Remover", new ImageIcon(getClass().getResource(getImageSourceDel())));
        btClear = new GenJButton("Limpar", new ImageIcon(getClass().getResource(getImageSourceClear())));
        btImportar = new GenJButton("Importar do detalhamento", new ImageIcon(getClass().getResource(getImageSourceImport())));
        panel.add(btClear);
        panel.add(btDel);
        panel.add(btAdd);
        panel.add(btImportar);

        ButtonAction buttonAction = new ButtonAction();
        btClear.addActionListener(buttonAction);
        btAdd.addActionListener(buttonAction);
        btDel.addActionListener(buttonAction);
        btImportar.addActionListener(buttonAction);

        return panel;
    }

    private JPanel createTablePane() {
        JPanel panel = new JPanel();
        planoAvaliacaoTable = new JTable();
        ListSelectionModel cellSelectionModel = planoAvaliacaoTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        planoAvaliacaoTable.getSelectionModel().addListSelectionListener(new SelectionListener());

        planoAvaliacaoTableModel = new PlanoAvaliacaoTableModel();
        refreshPlanoAvaliacao();

        JScrollPane planoAvaliacaoScroll = new JScrollPane();
        planoAvaliacaoScroll.setViewportView(planoAvaliacaoTable);
        planoAvaliacaoScroll.setPreferredSize(new Dimension(500, 200));
        panel.add(planoAvaliacaoScroll);
        return panel;
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
            PlanoDeEnsino planoDeEnsino = (PlanoDeEnsino) object;
            planoAvaliacaoTableModel = new PlanoAvaliacaoTableModel(planoDeEnsino.getPlanosAvaliacoes());
            refreshPlanoAvaliacao();
        }
    }

    @Override
    public boolean isValidated() {
        String msg = " ";
        Double peso = (Double) spinPeso.getValue();
        Double valor = (Double) spinValor.getValue();
        if (comboBimestre.getSelectedItem() == null) {
            msg = "O campo bimestre não foi selecionado.";
            comboBimestre.requestFocusInWindow();
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
        } else if (comboObjetivo.getSelectedItem() == null) {
            msg = "Um objetivo deve ser selecionado.";
            comboObjetivo.requestFocusInWindow();
        } else {
            return true;
        }
        JOptionPane.showMessageDialog(this, msg, "Aviso", JOptionPane.WARNING_MESSAGE);
        return false;
    }

    @Override
    public void clearFields() {
        comboBimestre.setSelectedItem(null);
        comboBimestre.repaint();
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
        comboBimestre.setEnabled(active);
        txtNome.setEnabled(active);
        comboInstrumento.setEnabled(active);
        comboObjetivo.setEnabled(active);
        spinData.setEnabled(active);
        spinPeso.setEnabled(active);
        spinValor.setEnabled(active);

        btAdd.setEnabled(active);
        btDel.setEnabled(active);
        btImportar.setEnabled(active);
        btClear.setEnabled(active);

        planoAvaliacaoTable.setEnabled(active);
    }

    @Override
    public void initFocus() {
        comboBimestre.requestFocusInWindow();
    }

    private class ButtonAction implements ActionListener {

        private int sequencia = 1;

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();

            int selectedRow = planoAvaliacaoTable.getSelectedRow();
            PlanoAvaliacao temp = planoAvaliacaoTableModel.getRow(selectedRow);
            if (source == btClear) {
                clearFields();
            } else if (source == btAdd) {
                if (isValidated()) {
                    PlanoAvaliacao plano = new PlanoAvaliacao();

                    plano.setNome(txtNome.getText());
                    plano.setInstrumentoAvaliacao((InstrumentoAvaliacao) comboInstrumento.getSelectedItem());
                    plano.setPeso((Double) spinPeso.getValue());
                    plano.setValor((Double) spinValor.getValue());
                    plano.setData((Date) spinData.getValue());
                    plano.setObjetivo((Objetivo) comboObjetivo.getSelectedItem());
//                    plano.setBimestre((Bimestre) comboBimestre.getSelectedItem());

                    if (selectedRow < 0 || temp.getId().getSequencia() == null) {
                        // cria um novo plano
                        planoAvaliacaoTableModel.addRow(plano);
                    } else {
                        // atualiza o plano existente
                        planoAvaliacaoTableModel.updateRow(selectedRow, plano);
                    }
                    clearFields();
                }
            } else if (source == btDel) {
                if (selectedRow < 0) {
                    JOptionPane.showMessageDialog(planoAvaliacaoTable,
                            "Nenhuma avaliação foi selecionada para remoção",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                planoAvaliacaoTableModel.removeRow(selectedRow);
                planoAvaliacaoTable.repaint();
                clearFields();
            } else if (source == btImportar) {
                // verifica se existem dados na lista de detalhamento
                if (listaDetalhamento.isEmpty()) {
                    JOptionPane.showMessageDialog(planoAvaliacaoTable,
                            "Não existem lançamentos de detalhamento para geração automática de avaliações",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (!planoAvaliacaoTableModel.isEmpty()) {
                    JOptionPane.showMessageDialog(planoAvaliacaoTable,
                            "Já existem avalições lançadas. Essa operação lançará novas avaliações "
                            + "no final da lista.", "Aviso", JOptionPane.WARNING_MESSAGE);
                }

                listaDetalhamento.forEach((detalhe) -> {
                    // para cada detalhe, verifica-se os métodos vinculados
                    detalhe.getMetodologias().forEach((metodologia) -> {
                        // verifica se a metodologia é de avaliacao
                        if (metodologia.getTipo() == TipoMetodo.INSTRUMENTO) {
                            // Cria a avaliação básica a partir do método de avaliação
                            PlanoAvaliacao plano = new PlanoAvaliacao();
                            plano.setNome(String.format("Avaliação %d", sequencia++));
                            // Inicaliza sempre no primeiro bimestre
//                            plano.setBimestre(Bimestre.PRIMEIRO);
                            // Inicializa com peso 1
                            plano.setPeso(1.0);
                            // Inicializa com o valor 10.0 (nota máxima)
                            plano.setValor(10.0);

                            /**
                             * atribui o último dia da semana letiva ao qual o
                             * método foi atribuído
                             */
                            plano.setData(detalhe.getSemanaLetiva().getPeriodo().getAte());
                            /**
                             * Atribui somente o primeiro objetivo à avaliação
                             * sendo incluída
                             */
                            if (!detalhe.getObjetivoDetalhes().isEmpty()) {
                                plano.setObjetivo(detalhe.getObjetivoDetalhes().get(0).getObjetivo());
                            }
                            /**
                             * O método é um objeto da classe
                             * InstrumentoAvaliacao
                             */
                            plano.setInstrumentoAvaliacao((InstrumentoAvaliacao) metodologia.getMetodo());
                            // adiciona o plano na tabela
                            planoAvaliacaoTableModel.addRow(plano);
                        }
                    });
                });
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

                txtNome.setText(plano.getNome());
                comboInstrumento.setSelectedItem(plano.getInstrumentoAvaliacao());
                comboInstrumento.repaint();

                spinPeso.setValue(plano.getPeso());
                spinValor.setValue(plano.getValor());
                spinData.setValue(plano.getData());
//                comboBimestre.setSelectedItem(plano.getBimestre());
                comboBimestre.repaint();

                comboObjetivo.setSelectedItem(plano.getObjetivo());
                comboObjetivo.repaint();

                refreshPlanoAvaliacao();
            }
        }

    }

}
