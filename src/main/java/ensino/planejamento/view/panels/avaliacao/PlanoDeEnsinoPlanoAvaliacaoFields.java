/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.avaliacao;

import ensino.components.GenJComboBox;
import ensino.components.GenJLabel;
import ensino.components.GenJSpinner;
import ensino.components.GenJTextField;
import ensino.configuracoes.view.models.EtapaEnsinoComboBoxModel;
import ensino.configuracoes.view.models.MetodoComboBoxModel;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.controller.ObjetivoController;
import ensino.planejamento.model.Objetivo;
import ensino.planejamento.model.PlanoAvaliacao;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.models.ObjetivoComboBoxModel;
import ensino.util.types.TipoMetodo;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author nicho
 */
public class PlanoDeEnsinoPlanoAvaliacaoFields extends DefaultFieldsPanel {

    private PlanoDeEnsino planoDeEnsino;

    private GenJTextField txtId;
    private GenJTextField txtDescricao;
    private GenJComboBox comboEtapaEnsino;
    private EtapaEnsinoComboBoxModel modelEtapaEnsino;

    private GenJSpinner spinPeso;
    private GenJSpinner spinValor;
    private GenJSpinner spinData;
    private GenJComboBox comboInstrumento;
    private GenJComboBox comboObjetivo;
    private ObjetivoComboBoxModel objetivoComboModel;
    
    public PlanoDeEnsinoPlanoAvaliacaoFields(PlanoDeEnsino planoDeEnsino) {
        super("Planejamento de Avaliações");
        this.planoDeEnsino = planoDeEnsino;
        initComponents();
    }

    private void initComponents() {
        try {
            setName("plano.avaliacoes");
            setLayout(new BorderLayout(5, 5));

            JPanel panelDados = createPanel(new BorderLayout(5, 5));
            panelDados.add(createFields(), BorderLayout.CENTER);
            add(panelDados, BorderLayout.PAGE_START);
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    private JPanel createFields() {
        JPanel panel = createPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        try {
            modelEtapaEnsino = new EtapaEnsinoComboBoxModel(
                    planoDeEnsino.getUnidadeCurricular().getCurso().getNivelEnsino()
            );
            comboEtapaEnsino = new GenJComboBox(modelEtapaEnsino);

            txtId = new GenJTextField(5, false);
            txtId.setEnabled(false);
            txtDescricao = new GenJTextField(20, true);
            comboInstrumento = new GenJComboBox(new MetodoComboBoxModel(TipoMetodo.INSTRUMENTO));
            spinPeso = new GenJSpinner(new SpinnerNumberModel(0.0, 0.0, null, 0.1));
            spinPeso.setEditor(new JSpinner.NumberEditor(spinPeso, "0.0"));
            spinValor = new GenJSpinner(new SpinnerNumberModel(0.0, 0.0, null, 0.1));
            spinValor.setEditor(new JSpinner.NumberEditor(spinValor, "0.0"));
            Calendar cal = Calendar.getInstance();
            spinData = new GenJSpinner(new SpinnerDateModel(cal.getTime(),
                    null, null, Calendar.DATE));
            spinData.setEditor(new JSpinner.DateEditor(spinData, "dd/MM/yyyy"));

            ObjetivoController objetivoCol = ControllerFactory.createObjetivoController();
            List<Objetivo> listaObjetivos = objetivoCol.listar(this.planoDeEnsino);
            objetivoCol.close();
            objetivoComboModel = new ObjetivoComboBoxModel(listaObjetivos);
            comboObjetivo = new GenJComboBox(objetivoComboModel);

            int col = 0, row = 0;
            GridLayoutHelper.set(c, col++, row);
            panel.add(new GenJLabel("Identificação:"), c);
            GridLayoutHelper.set(c, col++, row, 2, 1, GridBagConstraints.LINE_START);
            panel.add(new GenJLabel("Descrição: "), c);
            GridLayoutHelper.set(c, ++col, row++);
            panel.add(new GenJLabel("Etapa: "), c);

            col = 0;
            GridLayoutHelper.set(c, col++, row);
            c.fill = GridBagConstraints.HORIZONTAL;
            panel.add(txtId, c);
            GridLayoutHelper.set(c, col++, row, 2, 1, GridBagConstraints.LINE_START);
            c.fill = GridBagConstraints.HORIZONTAL;
            panel.add(txtDescricao, c);
            GridLayoutHelper.set(c, ++col, row++, 2, 1, GridBagConstraints.LINE_START);
            panel.add(comboEtapaEnsino, c);

            col = 0;
            GridLayoutHelper.set(c, col++, row, 2, 1, GridBagConstraints.LINE_START);
            panel.add(new GenJLabel("Instrumento de Avaliação: "), c);
            GridLayoutHelper.set(c, ++col, row);
            panel.add(new GenJLabel("Data prevista: "), c);
            GridLayoutHelper.set(c, ++col, row);
            panel.add(new GenJLabel("Peso: "), c);
            GridLayoutHelper.set(c, ++col, row++);
            panel.add(new GenJLabel("Valor: "), c);

            col = 0;
            GridLayoutHelper.set(c, col++, row, 2, 1, GridBagConstraints.LINE_START);
            c.fill = GridBagConstraints.HORIZONTAL;
            panel.add(comboInstrumento, c);
            GridLayoutHelper.set(c, ++col, row);
            panel.add(spinData, c);
            GridLayoutHelper.set(c, ++col, row);
            panel.add(spinPeso, c);
            GridLayoutHelper.set(c, ++col, row++);
            panel.add(spinValor, c);

            col = 0;
            GridLayoutHelper.set(c, col, row++, 5, 1, GridBagConstraints.LINE_START);
            panel.add(new GenJLabel("Objetivo Específico: "), c);
            GridLayoutHelper.set(c, col, row++, 5, 1, GridBagConstraints.LINE_START);
            c.fill = GridBagConstraints.HORIZONTAL;
            panel.add(comboObjetivo, c);

        } catch (Exception ex) {
            Logger.getLogger(PlanoDeEnsinoPlanoAvaliacaoFields.class.getName()).log(Level.SEVERE, null, ex);
        }
        return panel;
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = new HashMap();
        String sId = txtId.getText();
        Long id = null;
        if (sId.matches("\\d+")) {
            id = Long.parseLong(sId);
        }
        map.put("sequencia", id);
        map.put("planoDeEnsino", planoDeEnsino);
        map.put("nome", txtDescricao.getText());
        map.put("etapaEnsino", modelEtapaEnsino.getSelectedItem());
        map.put("peso", spinPeso.getValue());
        map.put("valor", spinValor.getValue());
        map.put("data", spinData.getValue());
        map.put("instrumentoAvaliacao", comboInstrumento.getSelectedItem());
        map.put("objetivo", comboObjetivo.getSelectedItem());
        
        return map;
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {
        
    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof PlanoAvaliacao) {
            try {
                PlanoAvaliacao o = (PlanoAvaliacao) object;
                planoDeEnsino = o.getPlanoDeEnsino();

                txtId.setText(o.getId().getSequencia().toString());
                txtDescricao.setText(o.getNome());
                comboInstrumento.setSelectedItem(o.getInstrumentoAvaliacao());
                comboInstrumento.repaint();

                spinPeso.setValue(o.getPeso());
                spinValor.setValue(o.getValor());
                spinData.setValue(o.getData());

                modelEtapaEnsino.setSelectedItem(o.getEtapaEnsino());
                comboEtapaEnsino.setModel(modelEtapaEnsino);
                comboEtapaEnsino.repaint();

                comboObjetivo.setSelectedItem(o.getObjetivo());
                objetivoComboModel.setSelectedItem(o.getObjetivo());
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
        } else if ("".equals(txtDescricao.getText())) {
            msg = "O campo nome não foi preenchido.";
            txtDescricao.requestFocusInWindow();
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
        txtId.setText("");
        comboEtapaEnsino.setSelectedItem(null);
        comboEtapaEnsino.repaint();
        txtDescricao.setText("");
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
        txtDescricao.setEnabled(active);
        comboInstrumento.setEnabled(active);
        comboObjetivo.setEnabled(active);
        spinData.setEnabled(active);
        spinPeso.setEnabled(active);
        spinValor.setEnabled(active);
    }

    @Override
    public void initFocus() {
        txtDescricao.requestFocusInWindow();
    }

}
