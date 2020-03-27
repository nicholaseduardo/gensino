/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.planoDeEnsino;

import ensino.components.GenJLabel;
import ensino.components.GenJTextArea;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.planejamento.model.PlanoDeEnsino;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author nicho
 */
public class ReferenciasPanel extends DefaultFieldsPanel {

    private GenJTextArea txtReferencia;

    public ReferenciasPanel() {
        super();
        initComponents();
    }

    private void initComponents() {
        setName("plano.referencias");
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        setBorder(BorderFactory.createEtchedBorder());

        JPanel panel = new JPanel(new GridBagLayout());
        add(panel);
        GridBagConstraints c = new GridBagConstraints();

        GenJLabel lblReferencia = new GenJLabel("Referencias bibliográficas", JLabel.TRAILING);
        GridLayoutHelper.set(c, 0, 0);
        panel.add(lblReferencia, c);

        txtReferencia = new GenJTextArea(10, 50);
        txtReferencia.setEnabled(false);
        JScrollPane referenciaScroll = new JScrollPane(txtReferencia);
        referenciaScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        GridLayoutHelper.set(c, 0, 1);
        panel.add(referenciaScroll, c);
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        return null;
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {
        UnidadeCurricular und = (UnidadeCurricular) mapValues.get("unidadeCurricular");
        if (und != null) {
            txtReferencia.setText(und.referenciaBibliograficaToString());
        }
    }

    /**
     * Atribui valor à referencia bibliográfica
     *
     * @param object Objeto da classe <code>UnidadeCurricular</code>
     */
    @Override
    public void setFieldValues(Object object) {
        if (object instanceof PlanoDeEnsino) {
            PlanoDeEnsino planoDeEnsino = (PlanoDeEnsino) object;
            txtReferencia.setText(planoDeEnsino.getUnidadeCurricular().referenciaBibliograficaToString());
        }
    }

    @Override
    public boolean isValidated() {
        return true;
    }

    @Override
    public void clearFields() {
        txtReferencia.setText("");
    }

    @Override
    public void enableFields(boolean active) {

    }

    @Override
    public void initFocus() {

    }

}
