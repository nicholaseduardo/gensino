/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.diario;

import ensino.components.GenJComboBox;
import ensino.components.GenJFormattedTextField;
import ensino.components.GenJLabel;
import ensino.components.GenJSpinner;
import ensino.components.GenJTextArea;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.planejamento.model.Diario;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.util.types.TipoAula;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author nicho
 */
public class DiarioConteudoFieldsPanel extends DefaultFieldsPanel {

    private PlanoDeEnsino planoDeEnsino;
    private Long sequencia;
    private GenJTextArea txtConteudo;
    private GenJTextArea txtObservacao;
    private GenJComboBox comboTipoAula;
    private GenJFormattedTextField txtHorario;
    private GenJSpinner spinData;

    public DiarioConteudoFieldsPanel() {
        this(null);
    }

    public DiarioConteudoFieldsPanel(PlanoDeEnsino planoDeEnsino) {
        super("Conteúdo programático");
        this.planoDeEnsino = planoDeEnsino;
        initComponents();
    }

    private void initComponents() {
        setName("panel.planoDeEnsino.diario.conteudo.ficha");
        setLayout(new BorderLayout(5, 5));

        JPanel panelDados = new JPanel(new BorderLayout(5, 5));
        panelDados.add(createFields(), BorderLayout.CENTER);

        add(panelDados, BorderLayout.PAGE_START);
    }

    private JPanel createFields() {
        JPanel panel = new JPanel(new GridBagLayout());
        try {
            GridBagConstraints c = new GridBagConstraints();
            comboTipoAula = new GenJComboBox(TipoAula.values());
            txtConteudo = new GenJTextArea(3, 50);
            txtObservacao = new GenJTextArea(3, 50);
            Calendar cal = Calendar.getInstance();
            spinData = new GenJSpinner(new SpinnerDateModel(cal.getTime(),
                    null, null, Calendar.DATE));
            spinData.setEditor(new JSpinner.DateEditor(spinData, "dd/MM/yyyy"));

            MaskFormatter fHorario = new MaskFormatter("##:##");
            fHorario.setValidCharacters("0123456789");
            txtHorario = new GenJFormattedTextField(fHorario);
            txtHorario.setColumns(4);

            int col = 0, row = 0;
            GridLayoutHelper.set(c, col++, row);
            panel.add(new GenJLabel("Data: "), c);
            GridLayoutHelper.set(c, col++, row);
            panel.add(new GenJLabel("Horário: "), c);
            GridLayoutHelper.set(c, col, row++);
            panel.add(new GenJLabel("Tipo: "), c);
            col = 0;
            GridLayoutHelper.set(c, col++, row);
            panel.add(spinData, c);
            GridLayoutHelper.set(c, col++, row);
            c.fill = GridBagConstraints.HORIZONTAL;
            panel.add(txtHorario, c);
            GridLayoutHelper.set(c, col++, row++);
            c.fill = GridBagConstraints.HORIZONTAL;
            panel.add(comboTipoAula, c);
            col = 0;
            GridLayoutHelper.set(c, col, row++, 3, 1, GridBagConstraints.LINE_START);
            panel.add(new GenJLabel("Conteúdo ministrado: "), c);
            GridLayoutHelper.set(c, col, row++, 3, 1, GridBagConstraints.LINE_START);
            JScrollPane conteudoScroll = new JScrollPane(txtConteudo);
            conteudoScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            panel.add(conteudoScroll, c);

            col = 0;
            GridLayoutHelper.set(c, col, row++, 3, 1, GridBagConstraints.LINE_START);
            panel.add(new GenJLabel("Observação: "), c);
            GridLayoutHelper.set(c, col, row++, 3, 1, GridBagConstraints.LINE_START);
            panel.add(txtObservacao, c);
            JScrollPane observacaoScroll = new JScrollPane(txtObservacao);
            observacaoScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            panel.add(observacaoScroll, c);
        } catch (ParseException ex) {
            Logger.getLogger(DiarioConteudoFieldsPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return panel;
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = new HashMap();
        map.put("id", sequencia);
        map.put("planoDeEnsino", planoDeEnsino);
        map.put("data", spinData.getValue());
        map.put("horario", txtHorario.getText());
        map.put("observacoes", txtObservacao.getText());
        map.put("conteudo", txtConteudo.getText());
        map.put("tipoAula", comboTipoAula.getSelectedItem());

        return map;
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {

    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof Diario) {
            Diario diario = (Diario) object;
            
            sequencia = diario.getId().getId();
            txtConteudo.setText(diario.getConteudo());
            txtObservacao.setText(diario.getObservacoes());
            txtHorario.setValue(diario.getHorario());
            txtHorario.repaint();

            spinData.setValue(diario.getData());
            comboTipoAula.setSelectedItem(diario.getTipoAula());
            comboTipoAula.repaint();
        }
    }

    @Override
    public boolean isValidated() {
        String msg = "O campo %s não foi informado!", campo = "";
        if (spinData.getValue() == null) {
            campo = "DATA";
            spinData.requestFocusInWindow();
        } else if (comboTipoAula.getSelectedItem() == null) {
            campo = "TIPO";
            comboTipoAula.requestFocusInWindow();
        } else if ("".equals(txtConteudo.getText())) {
            campo = "CONTEUDO";
            txtConteudo.requestFocusInWindow();
        } else if (txtHorario.getValue() == null) {
            campo = "HORÁRIO";
            txtHorario.requestFocusInWindow();
        } else {
            return true;
        }
        showInformationMessage(String.format(msg, campo));
        return false;
    }

    @Override
    public void clearFields() {
        sequencia = null;
        comboTipoAula.setSelectedItem(null);
        comboTipoAula.repaint();
        txtConteudo.setText("");
        txtObservacao.setText("");
        txtHorario.setValue(null);
        txtHorario.repaint();
        Calendar cal = Calendar.getInstance();
        spinData.setValue(cal.getTime());
    }

    @Override
    public void enableFields(boolean active) {
        comboTipoAula.setEnabled(active);
        txtConteudo.setEnabled(active);
        txtObservacao.setEnabled(active);
        txtHorario.setEnabled(active);
        spinData.setEnabled(active);
    }

    @Override
    public void initFocus() {
        comboTipoAula.requestFocusInWindow();
    }

}
