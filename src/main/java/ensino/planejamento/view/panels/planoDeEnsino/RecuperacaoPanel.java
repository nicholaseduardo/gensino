/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.planoDeEnsino;

import ensino.components.GenJLabel;
import ensino.components.GenJTextArea;
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
public class RecuperacaoPanel extends DefaultFieldsPanel {

    private GenJTextArea txtRecuperacao;

    public RecuperacaoPanel() {
        super();
        initComponents();
    }

    private void initComponents() {
        setName("plano.recuperacao");
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        setBorder(BorderFactory.createEtchedBorder());
        
        JPanel panel = new JPanel(new GridBagLayout());
        add(panel);
        GridBagConstraints c = new GridBagConstraints();

        GenJLabel lblReferencia = new GenJLabel("Recuperação da aprendizagem:", JLabel.TRAILING);
        GridLayoutHelper.set(c, 0, 0);
        panel.add(lblReferencia, c);

        txtRecuperacao = new GenJTextArea(10, 50);
        JScrollPane recuperacaoScroll = new JScrollPane(txtRecuperacao);
        recuperacaoScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        GridLayoutHelper.set(c, 0, 1);
        panel.add(recuperacaoScroll, c);
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = new HashMap();
        map.put("recuperacao", txtRecuperacao.getText());

        return map;
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {

    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof PlanoDeEnsino) {
            PlanoDeEnsino plano = (PlanoDeEnsino) object;
            txtRecuperacao.setText(plano.getRecuperacao());
        }
    }

    @Override
    public boolean isValidated() {
        if ("".equals(txtRecuperacao.getText())) {
            txtRecuperacao.requestFocusInWindow();
            return false;
        }
        return true;
    }

    @Override
    public void clearFields() {
        txtRecuperacao.setText("");
    }

    @Override
    public void enableFields(boolean active) {
        txtRecuperacao.setEnabled(active);
    }

    @Override
    public void initFocus() {
        txtRecuperacao.requestFocusInWindow();
    }

}
