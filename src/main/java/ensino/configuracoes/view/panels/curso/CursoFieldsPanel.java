/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.curso;

import ensino.components.GenJComboBox;
import ensino.components.GenJLabel;
import ensino.components.GenJTextField;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.NivelEnsino;
import ensino.configuracoes.view.models.NivelEnsinoComboBoxModel;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.ControllerFactory;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author santos
 */
public class CursoFieldsPanel extends DefaultFieldsPanel {

    private Campus selectedCampus;
    private JTextField txtId;
    private JTextField txtNome;
    private GenJComboBox comboCampus;
    private GenJComboBox comboNivelEnsino;

    public CursoFieldsPanel(Campus campus) {
        this();
        selectedCampus = campus;
    }

    public CursoFieldsPanel() {
        super();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(5, 5));
        try {
            JPanel fieldsPanel = new JPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            GenJLabel lblId = new GenJLabel("Código:", JLabel.TRAILING);
            txtId = new GenJTextField(5, false);
            lblId.setLabelFor(txtId);

            GenJLabel lblCampus = new GenJLabel("Campus:", JLabel.TRAILING);
            /**
             * Prepara a lista dos campus cadastrados para vincular ao combobox
             */
            comboCampus = new GenJComboBox(ControllerFactory.createCampusController()
                    .listar().toArray());
            // seleciona o campus
            if (comboCampus.getModel().getSize() > 0) {
                comboCampus.setSelectedIndex(0);
            }
            lblCampus.setLabelFor(comboCampus);

            GenJLabel lblNome = new GenJLabel("Nome:", JLabel.TRAILING);
            txtNome = new GenJTextField(30, false);
            lblNome.setLabelFor(txtNome);

            GenJLabel lblNivelEnsino = new GenJLabel("Nível de Ensino:", JLabel.TRAILING);
            comboNivelEnsino = new GenJComboBox();
            comboNivelEnsino.setModel(new NivelEnsinoComboBoxModel(ControllerFactory.createNivelEnsinoController()));
            lblNivelEnsino.setLabelFor(txtNome);

            int col = 0, row = 0;
            GridLayoutHelper.setRight(c, col++, row);
            fieldsPanel.add(lblId, c);
            GridLayoutHelper.set(c, col++, row++);
            fieldsPanel.add(txtId, c);

            col = 0;
            GridLayoutHelper.setRight(c, col++, row);
            fieldsPanel.add(lblCampus, c);
            GridLayoutHelper.set(c, col++, row++);
            fieldsPanel.add(comboCampus, c);

            col = 0;
            GridLayoutHelper.setRight(c, col++, row);
            fieldsPanel.add(lblNivelEnsino, c);
            GridLayoutHelper.set(c, col++, row++);
            fieldsPanel.add(comboNivelEnsino, c);

            col = 0;
            GridLayoutHelper.setRight(c, col++, row);
            fieldsPanel.add(lblNome, c);
            GridLayoutHelper.set(c, col, row);
            fieldsPanel.add(txtNome, c);

            add(fieldsPanel, BorderLayout.PAGE_START);

        } catch (Exception ex) {
            Logger.getLogger(CursoPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("id", ("".equals(txtId.getText()) ? null
                : Integer.parseInt(txtId.getText())));
        map.put("nome", txtNome.getText());
        map.put("campus", comboCampus.getSelectedItem());
        map.put("nivelEnsino", comboNivelEnsino.getSelectedItem());

        return map;
    }

    private void setFieldValues(Integer codigo, String nome,
            Campus iCampus, NivelEnsino nivelEnsino) {
        txtId.setText(codigo.toString());
        txtNome.setText(nome);
        if (iCampus != null) {
            comboCampus.setSelectedItem(iCampus);
        }
        NivelEnsinoComboBoxModel model = (NivelEnsinoComboBoxModel) comboNivelEnsino.getModel();
        model.setSelectedItem(nivelEnsino);
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {
        Integer codigo = (Integer) mapValues.get("id");
        Campus campus = (Campus) mapValues.get("campus");
        setFieldValues(codigo, (String) mapValues.get("nome"),
                (Campus) mapValues.get("campus"),
                (NivelEnsino) mapValues.get("nivelEnsino"));
    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof Curso) {
            Curso curso = (Curso) object;

            setFieldValues(curso.getId().getId(), curso.getNome(),
                    curso.getId().getCampus(),
                    curso.getNivelEnsino());
        }
    }

    @Override
    public boolean isValidated() {
        String msg = "O campo [%s] não foi informado!",
                campo = "";
        if (comboCampus.getSelectedItem() == null) {
            campo = "CAMPUS";
            comboCampus.requestFocusInWindow();
        } else if (comboNivelEnsino.getSelectedItem() == null) {
            campo = "NÍVEL DE ENSINO";
            comboNivelEnsino.requestFocusInWindow();
        } else if ("".equals(txtNome.getText())) {
            campo = "NOME";
            txtNome.requestFocusInWindow();
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
        comboCampus.setSelectedItem(selectedCampus);
        comboNivelEnsino.setSelectedItem(selectedCampus);
    }

    @Override
    public void enableFields(boolean active) {
        txtId.setEnabled(false);
        txtNome.setEnabled(active);
        comboCampus.setEnabled(active && selectedCampus == null);
        comboNivelEnsino.setEnabled(active && selectedCampus == null);
    }

    @Override
    public void initFocus() {
        txtNome.requestFocusInWindow();
    }
}
