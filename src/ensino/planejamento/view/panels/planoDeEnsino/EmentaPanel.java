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
public class EmentaPanel extends DefaultFieldsPanel {

    private GenJTextArea txtEmenta;

    public EmentaPanel() {
        super();
        initComponents();
    }

    private void initComponents() {
        setName("plano.ementa");
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        setBorder(BorderFactory.createEtchedBorder());

        JPanel panel = new JPanel(new GridBagLayout());
        add(panel);
        GridBagConstraints c = new GridBagConstraints();

        GenJLabel lblReferencia = new GenJLabel("Ementa", JLabel.TRAILING);
        GridLayoutHelper.set(c, 0, 0);
        panel.add(lblReferencia, c);

        txtEmenta = new GenJTextArea(10, 50);
        txtEmenta.setEditable(false);
        JScrollPane ementaScroll = new JScrollPane(txtEmenta);
        ementaScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        GridLayoutHelper.set(c, 0, 1);
        panel.add(ementaScroll, c);
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = new HashMap();
        map.put("ementa", txtEmenta.getText());

        return map;
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {
        UnidadeCurricular und = (UnidadeCurricular) mapValues.get("unidadeCurricular");
        if (und != null) {
            txtEmenta.setText(und.getEmenta());
        }
    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof PlanoDeEnsino) {
            PlanoDeEnsino plano = (PlanoDeEnsino) object;
            txtEmenta.setText(plano.getUnidadeCurricular().getEmenta());
        }
    }

    @Override
    public boolean isValidated() {
        return true;
    }

    @Override
    public void clearFields() {
        txtEmenta.setText("");
    }

    @Override
    public void enableFields(boolean active) {
        txtEmenta.setEnabled(active);
    }

    @Override
    public void initFocus() {
        txtEmenta.requestFocusInWindow();
    }

}
