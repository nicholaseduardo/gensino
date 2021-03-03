/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.docente;

import ensino.components.GenJButton;
import ensino.components.GenJLabel;
import static ensino.components.GenJPanel.IMG_SOURCE;
import ensino.components.GenJTextField;
import ensino.configuracoes.model.DocenteFactory;
import ensino.configuracoes.model.Docente;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.ControllerFactory;
import ensino.util.types.AcoesBotoes;
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
public class DocenteFields extends DefaultFieldsPanel {

    private GenJTextField txtId;
    private GenJTextField txtNome;

    private Docente docente;
    private Component frame;

    public DocenteFields(Component frame) {
        super();
        this.frame = frame;
        initComponents();
    }

    private void initComponents() {
        setName("docente.cadastro");
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEtchedBorder());

        backColor = getBackground();
        foreColor = getForeground();
        setBackground(backColor);

        URL urlDocente = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "teacher-icon-50px.png"));

        GenJLabel lblTitulo = new GenJLabel("Ficha do Docente", new ImageIcon(urlDocente), JLabel.CENTER);
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

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        GenJLabel lblId = new GenJLabel("Código:", JLabel.TRAILING);
        GridLayoutHelper.setRight(c, 0, 0);
        fieldsPanel.add(lblId, c);

        txtId = new GenJTextField(10, false);
        txtId.setEnabled(false);
        txtId.setFocusable(true);
        lblId.setLabelFor(txtId);
        GridLayoutHelper.set(c, 1, 0);
        fieldsPanel.add(txtId, c);

        GenJLabel lblNome = new GenJLabel("Nome:", JLabel.TRAILING);
        GridLayoutHelper.setRight(c, 0, 1);
        fieldsPanel.add(lblNome, c);
        txtNome = new GenJTextField(30, true);
        lblNome.setLabelFor(txtNome);
        GridLayoutHelper.set(c, 1, 1);
        fieldsPanel.add(txtNome, c);

        add(fieldsPanel, BorderLayout.CENTER);
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("id", "".equals(txtId.getText()) ? null
                : Integer.parseInt(txtId.getText()));
        map.put("nome", txtNome.getText());

        return map;
    }

    private void setFieldValues(Integer codigo, String nome) {
        txtId.setText(codigo.toString());
        txtNome.setText(nome);
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {
        Integer codigo = (Integer) mapValues.get("id");
        setFieldValues(codigo, (String) mapValues.get("nome"));
    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof Docente) {
            docente = (Docente) object;
            setFieldValues(docente.getId(), docente.getNome());
        }
    }

    @Override
    public boolean isValidated() {
        if ("".equals(txtNome.getText())) {
            showInformationMessage("O campo NOME não foi informado");
            return false;
        }
        return true;
    }

    @Override
    public void clearFields() {
        txtId.setText("");
        txtNome.setText("");
    }

    @Override
    public void enableFields(boolean active) {
        txtNome.setEnabled(active);
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
                docente = DocenteFactory.getInstance().getObject(getFieldValues());
                ControllerFactory.createDocenteController().salvar(docente);
                txtId.setText(docente.getId().toString());
                showInformationMessage("Dados gravados com sucesso");
                onCloseAction(e);
            } catch (Exception ex) {
                showErrorMessage(ex);
            }
        }
    }

}
