/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.unidadeCurricular;

import ensino.components.GenJLabel;
import ensino.components.GenJTextArea;
import ensino.components.GenJTextField;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.net.URL;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author nicho
 */
public class UnidadeCurricularFields extends DefaultFieldsPanel {

    /**
     * Atributo utilizado quando o curso é pré-requisito para este formulário
     */
    private Curso selectedCurso;

    private GenJTextField txtId;
    private GenJTextField txtNome;
    private GenJTextField txtAulasTeoricas;
    private GenJTextField txtAulasPraticas;
    private GenJTextField txtCargaHoraria;
    private GenJTextArea txtEmenta;

    private Component frame;
    private UnidadeCurricular unidadeCurricular;

    public UnidadeCurricularFields(Curso curso, Component frame) {
        super();
        this.selectedCurso = curso;
        this.frame = frame;
        initComponents();
    }

    public UnidadeCurricularFields() {
        this(null, null);
    }
    
    public void setFrame(Component frame) {
        this.frame = frame;
    }

    private void initComponents() {
        setName("unidadeCurricular.cadastro");
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEtchedBorder());

        URL urlUnidade = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "school-icon-50px.png"));
        
        GenJLabel lblTitulo = new GenJLabel("Ficha da Unidade Curricular",
                new ImageIcon(urlUnidade), JLabel.CENTER);
        lblTitulo.setVerticalTextPosition(JLabel.BOTTOM);
        lblTitulo.setHorizontalTextPosition(JLabel.CENTER);
        lblTitulo.resetFontSize(20);
        lblTitulo.setForeground(foreColor);
        lblTitulo.toBold();
        
        JPanel panel = createPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(lblTitulo);
        
        add(panel, BorderLayout.PAGE_START);
        add(createIdentificacaoPanel(), BorderLayout.CENTER);
    }

    private JPanel createIdentificacaoPanel() {
        GenJLabel lblId = new GenJLabel("Código:", JLabel.TRAILING);
        txtId = new GenJTextField(10, false);
        lblId.setLabelFor(txtId);

        GenJLabel lblCarga = new GenJLabel("Carga Horária:", JLabel.TRAILING);
        txtCargaHoraria = new GenJTextField(10, true);
        lblCarga.setLabelFor(txtCargaHoraria);

        GenJLabel lblNome = new GenJLabel("Nome da U.C.:", JLabel.TRAILING);
        txtNome = new GenJTextField(30, true);
        lblNome.setLabelFor(txtNome);

        GenJLabel lblTeoricas = new GenJLabel("N.o Aulas Teóricas:", JLabel.TRAILING);
        txtAulasTeoricas = new GenJTextField(10, true);
        lblTeoricas.setLabelFor(txtAulasTeoricas);

        GenJLabel lblPraticas = new GenJLabel("N.o Aulas Práticas:", JLabel.TRAILING);
        txtAulasPraticas = new GenJTextField(10, true);
        lblPraticas.setLabelFor(txtAulasPraticas);

        txtEmenta = new GenJTextArea(5, 50);
        JScrollPane scroll = new JScrollPane(txtEmenta);
        scroll.setBorder(createTitleBorder("Ementa:"));
        scroll.setAutoscrolls(true);

        int row = 0, col = 0;
        JPanel fieldsPanel = createPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        GridLayoutHelper.setRight(c, col++, row);
        fieldsPanel.add(lblId, c);
        GridLayoutHelper.set(c, col++, row);
        fieldsPanel.add(txtId, c);

        GridLayoutHelper.setRight(c, col++, row);
        fieldsPanel.add(lblCarga, c);
        GridLayoutHelper.set(c, col, row++, 3, 1, GridBagConstraints.LINE_START);
        fieldsPanel.add(txtCargaHoraria, c);

        col = 0;
        GridLayoutHelper.setRight(c, col++, row);
        fieldsPanel.add(lblNome, c);
        GridLayoutHelper.set(c, col, row++, 3, 1, GridBagConstraints.LINE_START);
        c.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(txtNome, c);

        col = 0;
        GridLayoutHelper.setRight(c, col++, row);
        fieldsPanel.add(lblTeoricas, c);
        GridLayoutHelper.set(c, col++, row);
        fieldsPanel.add(txtAulasTeoricas, c);

        GridLayoutHelper.setRight(c, col++, row);
        fieldsPanel.add(lblPraticas, c);
        GridLayoutHelper.set(c, col, row++);
        fieldsPanel.add(txtAulasPraticas, c);

        JPanel panel = createPanel(new BorderLayout());
        panel.add(fieldsPanel, BorderLayout.PAGE_START);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("id", ("".equals(txtId.getText()) ? null
                : Integer.parseInt(txtId.getText())));
        map.put("nome", txtNome.getText());
        map.put("nAulasTeoricas", "".equals(txtAulasTeoricas.getText()) ? null
                : Integer.parseInt(txtAulasTeoricas.getText()));
        map.put("nAulasPraticas", "".equals(txtAulasPraticas.getText()) ? null
                : Integer.parseInt(txtAulasPraticas.getText()));
        map.put("cargaHoraria", "".equals(txtCargaHoraria.getText()) ? null
                : Integer.parseInt(txtCargaHoraria.getText()));
        map.put("ementa", txtEmenta.getText());
        map.put("curso", selectedCurso);
        return map;
    }

    private void setFieldValues(Integer id, String nome, Integer nAulasTeoricas,
            Integer nAulasPraticas, Integer cargaHoraria,
            String ementa, Curso curso) {
        txtId.setText(id.toString());
        txtNome.setText(nome);
        txtAulasTeoricas.setText(nAulasTeoricas.toString());
        txtAulasPraticas.setText(nAulasPraticas.toString());
        txtCargaHoraria.setText(cargaHoraria.toString());
        txtEmenta.setText(ementa);
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {
        setFieldValues(
                (Integer) mapValues.get("id"),
                (String) mapValues.get("nome"),
                (Integer) mapValues.get("nAulasTeoricas"),
                (Integer) mapValues.get("nAulasPraticas"),
                (Integer) mapValues.get("cargaHoraria"),
                (String) mapValues.get("ementa"),
                (Curso) mapValues.get("curso"));
    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof UnidadeCurricular) {
            unidadeCurricular = (UnidadeCurricular) object;
            selectedCurso = unidadeCurricular.getCurso();
            setFieldValues(
                    unidadeCurricular.getId().getId(),
                    unidadeCurricular.getNome(),
                    unidadeCurricular.getnAulasTeoricas(),
                    unidadeCurricular.getnAulasPraticas(),
                    unidadeCurricular.getCargaHoraria(),
                    unidadeCurricular.getEmenta(),
                    unidadeCurricular.getId().getCurso());
        }
    }

    @Override
    public boolean isValidated() {
        String msg = "O campo [%s] não foi informado.",
                campo = "";
        if ("".equals(txtNome.getText())) {
            campo = "NOME";
            txtNome.requestFocusInWindow();
        } else if ("".equals(txtAulasTeoricas.getText())) {
            campo = "N. AULAS TEÓRICAS";
            txtAulasTeoricas.requestFocusInWindow();
        } else if ("".equals(txtAulasPraticas.getText())) {
            campo = "N. AULAS PRÁTICAS";
            txtAulasPraticas.requestFocusInWindow();
        } else if ("".equals(txtCargaHoraria.getText())) {
            campo = "CARGA HORÁRIA";
            txtCargaHoraria.requestFocusInWindow();
        } else if ("".equals(txtEmenta.getText())) {
            campo = "EMENTA";
            txtEmenta.requestFocusInWindow();
        } else {
            return true;
        }
        showInformationMessage(String.format(msg, campo));
        return false;
    }

    @Override
    public void clearFields() {
        txtId.setText("");
        txtAulasPraticas.setText("");
        txtAulasTeoricas.setText("");
        txtCargaHoraria.setText("");
        txtEmenta.setText("");
        txtNome.setText("");
    }

    @Override
    public void enableFields(boolean active) {
        txtId.setEnabled(false);
        txtAulasPraticas.setEnabled(active);
        txtAulasTeoricas.setEnabled(active);
        txtCargaHoraria.setEnabled(active);
        txtEmenta.setEnabled(active);
        txtNome.setEnabled(active);
    }

    @Override
    public void initFocus() {
        txtCargaHoraria.requestFocusInWindow();
    }

}
