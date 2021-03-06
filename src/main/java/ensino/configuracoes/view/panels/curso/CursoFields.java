/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.curso;

import ensino.components.GenJButton;
import ensino.components.GenJLabel;
import ensino.components.GenJRadioButton;
import ensino.components.GenJTextField;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.CursoFactory;
import ensino.configuracoes.model.NivelEnsino;
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
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author santos
 */
public class CursoFields extends DefaultFieldsPanel {

    private Campus selectedCampus;
    private JTextField txtId;
    private JTextField txtNome;
    private ButtonGroup buttonGroup;
    private GenJRadioButton[] radios;

    private Curso curso;
    private Component frame;

    public CursoFields(Campus campus, Component frame) {
        super();
        this.frame = frame;
        selectedCampus = campus;
        initComponents();
    }

    public CursoFields() {
        this(null, null);
    }

    public void setFrame(Component frame) {
        this.frame = frame;
    }

    private void initComponents() {
        setName("curso.cadastro");
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEtchedBorder());

        URL urlCurso = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "courses-icon-50px.png"));

        GenJLabel lblTitulo = new GenJLabel("Ficha do Curso", new ImageIcon(urlCurso), JLabel.CENTER);
        lblTitulo.setVerticalTextPosition(JLabel.BOTTOM);
        lblTitulo.setHorizontalTextPosition(JLabel.CENTER);
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
        try {
            JPanel fieldsPanel = createPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            GenJLabel lblId = new GenJLabel("Código:", JLabel.TRAILING);
            txtId = new GenJTextField(5, false);
            txtId.setEnabled(false);
            lblId.setLabelFor(txtId);

            GenJLabel lblNome = new GenJLabel("Nome:", JLabel.TRAILING);
            txtNome = new GenJTextField(30, false);
            lblNome.setLabelFor(txtNome);

            /**
             * Criação dos radiobuttons dos níveis de ensino
             */
            JPanel radiosPanel = createPanel();
            radiosPanel.setBorder(createTitleBorder("Nível de Ensino"));

            buttonGroup = new ButtonGroup();
            List<NivelEnsino> listaNE = ControllerFactory.createNivelEnsinoController().listar();
            radios = new GenJRadioButton[listaNE.size()];
            for (int i = 0; i < listaNE.size(); i++) {
                NivelEnsino ne = listaNE.get(i);
                radios[i] = new GenJRadioButton(ne.getNome(), false, buttonGroup);
                radios[i].setObjectValue(ne);
                
                radiosPanel.add(radios[i]);
            }

            int col = 0, row = 0;
            GridLayoutHelper.setRight(c, col++, row);
            fieldsPanel.add(lblId, c);
            GridLayoutHelper.set(c, col++, row++);
            fieldsPanel.add(txtId, c);

            col = 0;
            GridLayoutHelper.set(c, col, row++, 2, 1, GridBagConstraints.LINE_START);
            fieldsPanel.add(radiosPanel, c);

            col = 0;
            GridLayoutHelper.setRight(c, col++, row);
            fieldsPanel.add(lblNome, c);
            GridLayoutHelper.set(c, col, row);
            fieldsPanel.add(txtNome, c);

            add(fieldsPanel, BorderLayout.CENTER);

        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    private NivelEnsino getSelectedNivelEnsino() {
        for (GenJRadioButton rb : radios) {
            if (rb.isSelected()) {
                return (NivelEnsino) rb.getObjectValue();
            }
        }
        return null;
    }

    private void setSelectedNivelEnsino(NivelEnsino ne) {
        for (GenJRadioButton rb : radios) {
            if (rb.getObjectValue().equals(ne)) {
                rb.setSelected(true);
                break;
            }
        }
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("id", "".equals(txtId.getText()) ? null
                : Integer.parseInt(txtId.getText()));
        map.put("nome", txtNome.getText());
        map.put("campus", selectedCampus);
        map.put("nivelEnsino", getSelectedNivelEnsino());

        return map;
    }

    private void setFieldValues(Long codigo, String nome,
            Campus iCampus, NivelEnsino nivelEnsino) {
        txtId.setText(codigo.toString());
        txtNome.setText(nome);
        selectedCampus = iCampus;
        setSelectedNivelEnsino(nivelEnsino);
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {
        Long codigo = (Long) mapValues.get("id");
        
        setFieldValues(codigo, (String) mapValues.get("nome"),
                (Campus) mapValues.get("campus"),
                (NivelEnsino) mapValues.get("nivelEnsino"));
    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof Curso) {
            curso = (Curso) object;
            setFieldValues(curso.getId().getId(), curso.getNome(),
                    curso.getId().getCampus(), curso.getNivelEnsino());
        }
    }

    @Override
    public boolean isValidated() {
        String msg = "O campo [%s] não foi informado!",
                campo = "";
        if (buttonGroup.getSelection() == null) {
            campo = "NÍVEL DE ENSINO";
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
        buttonGroup.clearSelection();
    }

    @Override
    public void enableFields(boolean active) {
        txtNome.setEnabled(active);
        for (GenJRadioButton rb : radios) {
            rb.setEnabled(active && selectedCampus == null);
        }
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
            Boolean add = Boolean.FALSE;
            if ("".equals(txtId.getText())) {
                curso = CursoFactory.getInstance().getObject(getFieldValues());
                add = Boolean.TRUE;
            } else {
                CursoFactory.getInstance().updateObject(curso, getFieldValues());
            }
            try {
                ControllerFactory.createCursoController().salvar(curso);
                if (add) {
                    txtId.setText(curso.getId().getId().toString());
                    selectedCampus.addCurso(curso);
                }
                onCloseAction(e);
            } catch (Exception ex) {
                showErrorMessage(ex);
            }
        }
    }
}
