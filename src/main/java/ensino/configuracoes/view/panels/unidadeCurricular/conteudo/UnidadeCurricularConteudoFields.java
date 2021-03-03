/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.unidadeCurricular.conteudo;

import ensino.components.GenJComboBox;
import ensino.components.GenJLabel;
import ensino.components.GenJTextArea;
import ensino.components.GenJTextField;
import ensino.configuracoes.model.Conteudo;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.configuracoes.view.models.ConteudoComboBoxModel;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

/**
 *
 * @author santos
 */
public class UnidadeCurricularConteudoFields extends DefaultFieldsPanel {

    private UnidadeCurricular unidadeCurricular;
    private GenJTextField txtId;
    private GenJTextField txtSequencia;
    private GenJTextField txtNivel;
    private GenJTextArea txtDescricao;
    private GenJComboBox comboConteudo;
    private ConteudoComboBoxModel comboModel;

    public UnidadeCurricularConteudoFields(UnidadeCurricular unidadeCurricular) {
        super();
        this.unidadeCurricular = unidadeCurricular;

        initComponents();
    }

    private void initComponents() {
        setName("unidadeCurricular.cadastro.conteudo");
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEtchedBorder());

        URL urlReferencias = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "binary-tree-icon-50px.png"));
        GenJLabel lblTitulo = new GenJLabel("Bases Científicos-Tecnológicas (Conteúdos)",
                new ImageIcon(urlReferencias), JLabel.CENTER);
        lblTitulo.setVerticalTextPosition(JLabel.BOTTOM);
        lblTitulo.setHorizontalTextPosition(JLabel.CENTER);
        lblTitulo.resetFontSize(20);
        lblTitulo.setForeground(foreColor);
        lblTitulo.toBold();
        add(lblTitulo, BorderLayout.PAGE_START);
        add(createAttributesPanel(), BorderLayout.CENTER);
    }

    private JPanel createAttributesPanel() {
        JPanel panel = createPanel(new GridBagLayout());

        int col = 0, row = 0;
        GridBagConstraints c = new GridBagConstraints();

        GenJLabel lblId = new GenJLabel("Identificação: ");
        txtId = new GenJTextField(10, false);

        GenJLabel lblDescrição = new GenJLabel("Descrição: ");
        txtDescricao = new GenJTextArea(2, 30);
        JScrollPane scroll = new JScrollPane(txtDescricao);
        scroll.setAutoscrolls(true);

        comboModel = new ConteudoComboBoxModel(this.unidadeCurricular);
        GenJLabel lblConteudoPai = new GenJLabel("Conteúdo Superior: ");
        comboConteudo = new GenJComboBox(comboModel);
        comboConteudo.setRenderer(new ComboRenderer());
        comboConteudo.addItemListener(new ComboItemListener());

        txtSequencia = new GenJTextField(10, false);
        txtSequencia.setLabelFor("Sequência");

        txtNivel = new GenJTextField(10, false);
        txtNivel.setLabelFor("Nível");

        JPanel panelArvore = createPanel();
        panelArvore.setBorder(createTitleBorder("Dados de organização na árvore"));
        panelArvore.add(txtNivel);
        panelArvore.add(txtSequencia);

        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblId, c);
        GridLayoutHelper.set(c, col, row++);
        panel.add(txtId, c);

        col = 0;
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblConteudoPai, c);
        GridLayoutHelper.set(c, col, row++);
        panel.add(comboConteudo, c);

        col = 0;
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblDescrição, c);
        GridLayoutHelper.set(c, col, row++);
        panel.add(scroll, c);

        col = 0;
        GridLayoutHelper.set(c, col, row++, 2, 1, GridBagConstraints.LINE_END);
        panel.add(panelArvore, c);

        return panel;
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = new HashMap();

        String sId = txtId.getText();
        Integer id = sId.matches("\\d+") ? Integer.parseInt(sId) : null;
        String sSequencia = txtSequencia.getText();
        Integer sequencia = sSequencia.matches("\\d+") ? Integer.parseInt(sSequencia) : 0;
        String sNivel = txtNivel.getText();
        Integer nivel = sNivel.matches("\\d+") ? Integer.parseInt(sNivel) : 0;

        map.put("id", id);
        map.put("unidadeCurricular", unidadeCurricular);
        map.put("descricao", txtDescricao.getText());
        map.put("conteudoParent", comboConteudo.getSelectedIndex() == 0 ? null : comboModel.getSelectedItem());
        map.put("sequencia", sequencia);
        map.put("nivel", nivel);

        return map;
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {

    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof Conteudo) {
            Conteudo o = (Conteudo) object;
            comboModel.refresh(o);

            unidadeCurricular = o.getUnidadeCurricular();
            txtId.setText(o.getId().getId().toString());
            txtDescricao.setText(o.getDescricao());
            txtNivel.setText(o.getNivel() != null ? o.getNivel().toString() : "");
            txtSequencia.setText(o.getSequencia() != null ? o.getSequencia().toString() : "");
            comboModel.setSelectedItem(o.getConteudoParent());
        }
    }

    @Override
    public boolean isValidated() {
        String msg = "O campo [%s] não foi informado.", campo = "";
        if ("".equals(txtDescricao.getText())) {
            campo = "DESCRIÇÃO";
            txtDescricao.requestFocusInWindow();
        } else {
            return true;
        }
        showWarningMessage(String.format(msg, campo));
        return false;
    }

    @Override
    public void clearFields() {
        txtId.setText("");
        txtDescricao.setText("");
        txtNivel.setText("");
        txtSequencia.setText("");
        comboConteudo.setSelectedItem(null);
        comboModel.refresh();
    }

    @Override
    public void enableFields(boolean active) {
        txtId.setEnabled(false);
        txtDescricao.setEnabled(active);
        txtNivel.setEnabled(active);
        txtNivel.setEditable(active);
        txtSequencia.setEnabled(active);
        txtSequencia.setEditable(active);
        comboConteudo.setEnabled(active);
    }

    @Override
    public void initFocus() {
        txtDescricao.requestFocusInWindow();
    }

    private class ComboItemListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent evt) {
            if (evt.getStateChange() == ItemEvent.SELECTED) {
                Conteudo item = (Conteudo) evt.getItem();
                Integer nivel = null, sequencia = null;
                /**
                 * Verifica se na lista de children do item já existe o conteúdo
                 * com o ID já informado
                 */
                String sId = txtId.getText();
                System.out.println("SID == " + sId);
                if (sId.matches("\\d+")) {
                    Integer id = Integer.parseInt(sId);
                    for (int i = 0; i < item.getChildren().size(); i++) {
                        Conteudo c = item.getChildren().get(i);
                        /**
                         * Mantém a sequencia correta
                         */
                        if (c.getId().getId().equals(id)) {
                            /**
                             * Força a atualização correta da sequencia do item
                             */
                            sequencia = i;
                            break;
                        }
                    }
                }

                nivel = item.getNivel() != null ? item.getNivel() + 1 : 0;
                if (sequencia == null) {
                    sequencia = item.getChildren().size();
                }

                txtNivel.setText(nivel.toString());
                /**
                 * O valor da sequência sempre será aquele relacionado ao número
                 * total de elementos do conteúdo superior
                 */
                txtSequencia.setText(sequencia.toString());
            } else if (evt.getStateChange() == ItemEvent.DESELECTED) {
                // Item is no longer selected
                txtNivel.setText("");
                txtSequencia.setText("");
            }
        }

    }

    private class ComboRenderer implements ListCellRenderer {

        protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

        public String repeatString(String value, Integer number) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < number; i++) {
                sb.append(value);
            }
            return sb.toString();
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            if (value instanceof Conteudo) {
                Conteudo c = (Conteudo) value;
                Color theForeground = null;

                Integer nivel = c.getNivel();

                int n = nivel != null && nivel > 1 ? nivel - 1 : 0;
                String theText = c.getDescricao() != null ? c.getDescricao() : "";
                if (n > 0) {
                    theText = repeatString("--", n).concat(theText);
                }

                theForeground = list.getForeground();

                GenJLabel renderer = new GenJLabel();
                renderer.resetFontSize(16);
                if (!isSelected) {
                    renderer.setForeground(theForeground);
                }
                renderer.setText(theText, 60 + (n * 2));

                return renderer;
            }
            return null;
        }

    }
}
