/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.nivelEnsino;

import ensino.components.GenJButton;
import ensino.components.GenJCheckBox;
import ensino.components.GenJComboBox;
import ensino.components.GenJLabel;
import ensino.components.GenJTextField;
import ensino.configuracoes.model.EtapaEnsino;
import ensino.configuracoes.model.EtapaEnsinoFactory;
import ensino.configuracoes.model.NivelEnsino;
import ensino.configuracoes.view.models.EtapaEnsinoComboBoxModel;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.util.types.AcoesBotoes;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author santos
 */
public class NivelEnsinoEtapaFields extends DefaultFieldsPanel {

    private NivelEnsino nivelEnsino;
    private EtapaEnsino etapaEnsino;
    private Component frame;

    private GenJTextField txtId;
    private GenJTextField txtNome;
    private GenJCheckBox checkBoxRecupera;
    private GenJComboBox comboNivelDependente;

    private EtapaEnsinoComboBoxModel nivelDependenteComboBoxModel;

    public NivelEnsinoEtapaFields(Component frame) {
        this(null, frame);
    }

    public NivelEnsinoEtapaFields(NivelEnsino nivelEnsino, Component frame) {
        super();
        this.nivelEnsino = nivelEnsino;
        this.frame = frame;
        initComponents();
    }

    private void initComponents() {
        setName("nivelEnsino.etapaEnsino.cadastro");
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEtchedBorder());

        backColor = getBackground();
        foreColor = getForeground();
        setBackground(backColor);

        GenJLabel lblTitulo = new GenJLabel("Etapa de Ensino", JLabel.CENTER);
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
        add(createEtapaFields(), BorderLayout.CENTER);
    }

    private JPanel createEtapaFields() {
        GenJLabel lblId = new GenJLabel("Código:", JLabel.TRAILING);
        GenJLabel lblNome = new GenJLabel("Nome:", JLabel.TRAILING);
        GenJLabel lblNivelDependente = new GenJLabel("Nível dependente:", JLabel.TRAILING);

        txtId = new GenJTextField(10, false);
        txtId.setEnabled(false);
        txtNome = new GenJTextField(30, true);
        checkBoxRecupera = new GenJCheckBox("Recuperação?");
        nivelDependenteComboBoxModel = new EtapaEnsinoComboBoxModel(
                nivelEnsino != null ? nivelEnsino.getEtapas()
                        : new ArrayList());
        comboNivelDependente = new GenJComboBox(nivelDependenteComboBoxModel);

        JPanel panel = createPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        int col = 0, row = 0;
        GridLayoutHelper.set(c, col++, row);
        panel.add(lblId, c);
        GridLayoutHelper.set(c, col++, row);
        panel.add(txtId, c);
        GridLayoutHelper.set(c, col++, row++);
        panel.add(checkBoxRecupera, c);

        col = 0;
        GridLayoutHelper.set(c, col++, row);
        panel.add(lblNome, c);
        GridLayoutHelper.set(c, col++, row++, 2, 1, GridBagConstraints.LINE_START);
        panel.add(txtNome, c);

        col = 0;
        GridLayoutHelper.set(c, col++, row);
        panel.add(lblNivelDependente, c);
        GridLayoutHelper.set(c, col, row++, 2, 1, GridBagConstraints.LINE_START);
        panel.add(comboNivelDependente, c);

        JPanel panelEtapas = createPanel(new BorderLayout(5, 5));
        panelEtapas.setBorder(createTitleBorder("Etapas de Ensino"));
        panelEtapas.add(panel, BorderLayout.PAGE_START);

        return panelEtapas;
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("id", "".equals(txtId.getText()) ? null
                : Integer.parseInt(txtId.getText()));
        map.put("nome", txtNome.getText());
        map.put("recuperaaco", checkBoxRecupera.isSelected());
        map.put("nivelDependente", nivelDependenteComboBoxModel.getSelectedItem());
        return map;
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof EtapaEnsino) {
            etapaEnsino = (EtapaEnsino) object;
            txtId.setText(etapaEnsino.getId().getId().toString());
            txtNome.setText(etapaEnsino.getNome());
            checkBoxRecupera.setSelected(etapaEnsino.isRecuperacao());

            nivelDependenteComboBoxModel.setSelectedItem(etapaEnsino.getNivelDependente());
            comboNivelDependente.repaint();
        }
    }

    @Override
    public boolean isValidated() {
        String msg = "O campo [%s] não foi informado!", campo = "";
        if ("".equals(txtNome.getText())) {
            campo = "NOME";
        } else {
            return true;
        }
        showInformationMessage(String.format(msg, campo));
        return false;
    }

    @Override
    public void clearFields() {
        txtId.setText("");
        txtNome.setText("");
        checkBoxRecupera.setSelected(Boolean.FALSE);
        nivelDependenteComboBoxModel.setSelectedItem(null);
        comboNivelDependente.repaint();
    }

    @Override
    public void enableFields(boolean active) {
        txtId.setEnabled(false);
        txtNome.setEnabled(active);
        checkBoxRecupera.setEnabled(active);
        comboNivelDependente.setEnabled(active);
    }

    @Override
    public void initFocus() {
        txtNome.requestFocusInWindow();
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
                etapaEnsino = EtapaEnsinoFactory.getInstance().getObject(getFieldValues());
                Integer n = nivelEnsino.getEtapas().size();
                Long seq = 1L;
                if (!nivelEnsino.getEtapas().isEmpty()) {
                    seq = nivelEnsino.getEtapas().get(n - 1).getId().getId() + 1;
                }
                etapaEnsino.getId().setId(seq);
                nivelEnsino.addEtapaEnsino(etapaEnsino);
            } else {
                EtapaEnsinoFactory.getInstance().updateObject(etapaEnsino, getFieldValues());
            }
            onCloseAction(e);
        }
    }

}
