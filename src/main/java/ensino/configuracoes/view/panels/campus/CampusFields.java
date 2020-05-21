/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.campus;

import ensino.components.GenJButton;
import ensino.components.GenJComboBox;
import ensino.components.GenJLabel;
import static ensino.components.GenJPanel.IMG_SOURCE;
import ensino.components.GenJTextField;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.CampusFactory;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.ControllerFactory;
import ensino.util.types.AcoesBotoes;
import ensino.util.types.StatusCampus;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author santos
 */
public class CampusFields extends DefaultFieldsPanel {

    private GenJTextField txtId;
    private GenJTextField txtNome;
    private GenJComboBox comboStatus;

    private Campus campus;
    private Component frame;

    public CampusFields(Component frame) {
        super();
        this.frame = frame;
        initComponents();
    }

    private void initComponents() {
        setName("campus.cadastro");
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEtchedBorder());

        backColor = getBackground();
        foreColor = getForeground();
        setBackground(backColor);

        URL urlCampus = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "teacher-icon-50px.png"));

        GenJLabel lblTitulo = new GenJLabel("Ficha do Campus", new ImageIcon(urlCampus), JLabel.CENTER);
        lblTitulo.setVerticalTextPosition(JLabel.BOTTOM);
        lblTitulo.setHorizontalTextPosition(JLabel.CENTER);
        lblTitulo.resetFontSize(20);
        lblTitulo.setForeground(foreColor);
        lblTitulo.toBold();
        add(lblTitulo, BorderLayout.PAGE_START);

        JPanel panelButton = createPanel(new FlowLayout(FlowLayout.RIGHT));
        GenJButton btSave = createButton(new ActionHandler(AcoesBotoes.SAVE), backColor, foreColor);
        panelButton.add(btSave);

        if (frame != null) {
            GenJButton btClose = createButton(new ActionHandler(AcoesBotoes.CLOSE), backColor, foreColor);
            panelButton.add(btClose);
        }

        add(panelButton, BorderLayout.PAGE_END);

        add(createFieldsPanel(), BorderLayout.CENTER);
    }

    private JPanel createFieldsPanel() {
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        int col = 0, row = 0;

        GenJLabel lblId = new GenJLabel("Código:", JLabel.TRAILING);
        GridLayoutHelper.setRight(c, col++, row);
        fieldsPanel.add(lblId, c);

        txtId = new GenJTextField(10, false);
        txtId.setEnabled(false);
        txtId.setFocusable(true);
        lblId.setLabelFor(txtId);
        GridLayoutHelper.set(c, col++, row);
        fieldsPanel.add(txtId, c);

        GenJLabel lblStatus = new GenJLabel("Status:", JLabel.TRAILING);
        GridLayoutHelper.setRight(c, col++, row);
        fieldsPanel.add(lblStatus, c);

        comboStatus = new GenJComboBox(StatusCampus.values());
        comboStatus.setFocusable(true);
        lblStatus.setLabelFor(comboStatus);
        GridLayoutHelper.set(c, col++, row++);
        fieldsPanel.add(comboStatus, c);

        col = 0;
        GenJLabel lblNome = new GenJLabel("Nome:", JLabel.TRAILING);
        GridLayoutHelper.setRight(c, col++, row);
        fieldsPanel.add(lblNome, c);
        txtNome = new GenJTextField(30, false);
        lblNome.setLabelFor(txtNome);
        GridLayoutHelper.set(c, col, row, 3, 1, GridBagConstraints.BASELINE);
        fieldsPanel.add(txtNome, c);
        return fieldsPanel;
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("id", ("".equals(txtId.getText()) ? null
                : Integer.parseInt(txtId.getText())));
        map.put("nome", txtNome.getText());
        map.put("status", comboStatus.getSelectedItem());

        return map;
    }

    private void setFieldValues(Integer codigo, String nome, StatusCampus status) {
        txtId.setText(codigo.toString());
        txtNome.setText(nome);
        comboStatus.setSelectedItem(status);
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {
        Integer codigo = (Integer) mapValues.get("id");
        setFieldValues(codigo, (String) mapValues.get("nome"), null);
    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof Campus) {
            campus = (Campus) object;
            setFieldValues(campus.getId(), campus.getNome(), campus.getStatus());
        }
    }

    @Override
    public boolean isValidated() {
        String msg = "O campo [%s] não foi informado", campo = "";
        if ("".equals(txtNome.getText())) {
            campo = "NOME";
            txtNome.requestFocusInWindow();
        } else if (comboStatus.getSelectedItem() == null) {
            campo = "STATUS";
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
        comboStatus.setSelectedItem(null);
    }

    @Override
    public void enableFields(boolean active) {
        txtNome.setEnabled(active);
        comboStatus.setEnabled(active);
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
        } else if (frame instanceof JFrame) {
            JFrame f = (JFrame) frame;
            f.dispose();
        }
    }

    @Override
    public void onSaveAction(ActionEvent e, Object o) {
        if (isValidated()) {
            try {
                campus = CampusFactory.getInstance().getObject(getFieldValues());
                ControllerFactory.createCampusController().salvar(campus);
                txtId.setText(campus.getId().toString());
                showInformationMessage("Dados gravados com sucesso");
                onCloseAction(e);
            } catch (Exception ex) {
                showErrorMessage(ex);
            }
        }
    }

}
