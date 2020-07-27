/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.objetivoEspecifico;

import ensino.components.GenJButton;
import ensino.components.GenJComboBox;
import ensino.components.GenJLabel;
import ensino.components.GenJTextArea;
import ensino.components.renderer.TextAreaCellRenderer;
import ensino.configuracoes.model.ObjetivoUC;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.controller.ObjetivoController;
import ensino.planejamento.model.Objetivo;
import ensino.planejamento.model.ObjetivoFactory;
import ensino.planejamento.model.ObjetivoId;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.models.ObjetivoTableModel;
import ensino.reports.ChartsFactory;
import ensino.util.types.AcoesBotoes;
import ensino.util.types.TipoAula;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
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
public class PlanoDeEnsinoObjetivoEspecificoFields extends DefaultFieldsPanel {

    private Integer sequencia;
    private GenJTextArea txtObjetivo;
    private GenJComboBox comboObjetivoUC;

    private PlanoDeEnsino planoDeEnsino;

    private Component frame;

    public PlanoDeEnsinoObjetivoEspecificoFields(Component frame) {
        this(frame, null);
    }

    public PlanoDeEnsinoObjetivoEspecificoFields(Component frame,
            PlanoDeEnsino planoDeEnsino) {
        super("Objetivos específicos");
        this.frame = frame;
        this.planoDeEnsino = planoDeEnsino;
        initComponents();
    }

    private void initComponents() {
        try {
            setName("plano.objetivos");
            setLayout(new BorderLayout(5, 5));
            setBorder(BorderFactory.createEtchedBorder());

            add(createPanelEspecificos(), BorderLayout.CENTER);
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    private JPanel createPanelEspecificos() {
        txtObjetivo = new GenJTextArea(2, 50);
        JScrollPane objetivoScroll = new JScrollPane(txtObjetivo);
        objetivoScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        objetivoScroll.setBorder(createTitleBorder("Descrição do objetivo"));

        GenJLabel lblObjetivoUC = new GenJLabel("Objetivo da U.C.: ", JLabel.TRAILING);
        if (!planoDeEnsino.getUnidadeCurricular().getObjetivos().isEmpty()) {
            comboObjetivoUC = new GenJComboBox(planoDeEnsino.getUnidadeCurricular().getObjetivos().toArray());
        } else {
            comboObjetivoUC = new GenJComboBox();
        }
        lblObjetivoUC.setLabelFor(comboObjetivoUC);

        JPanel panel = createPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        int col = 0, row = 0;
        GridLayoutHelper.set(c, col, row++, 2, 1, GridBagConstraints.LINE_START);
        panel.add(objetivoScroll, c);

        col = 0;
        GridLayoutHelper.set(c, col++, row);
        panel.add(lblObjetivoUC, c);
        GridLayoutHelper.set(c, col++, row);
        panel.add(comboObjetivoUC, c);

        return panel;
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = new HashMap();
        map.put("sequencia", sequencia);
        map.put("planoDeEnsino", planoDeEnsino);
        map.put("descricao", txtObjetivo.getText());
        map.put("objetivoUC", comboObjetivoUC.getSelectedItem());
        return map;
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {

    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof Objetivo) {
            Objetivo o = (Objetivo) object;
            planoDeEnsino = o.getPlanoDeEnsino();

            sequencia = o.getId().getSequencia();
            txtObjetivo.setText(o.getDescricao());
            comboObjetivoUC.setSelectedItem(o.getObjetivoUC());
        }
    }

    @Override
    public boolean isValidated() {
        boolean result = false;
        String msg = "O campo %s não foi informado!", campo = "";
        if ("".equals(txtObjetivo.getText())) {
            campo = "Descrição do objetivo";
            txtObjetivo.requestFocusInWindow();
        } else {
            return true;
        }
        showInformationMessage(String.format(msg, campo));
        return result;
    }

    @Override
    public void clearFields() {
        sequencia = null;
        txtObjetivo.setText("");
        comboObjetivoUC.setSelectedItem(null);
    }

    @Override
    public void enableFields(boolean active) {
        txtObjetivo.setEnabled(active);
        comboObjetivoUC.setEnabled(active);
    }

    @Override
    public void initFocus() {
        txtObjetivo.requestFocusInWindow();
    }

}
