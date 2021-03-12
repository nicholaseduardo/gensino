/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.calendario.atividade;

import ensino.components.GenJFormattedTextField;
import ensino.components.GenJLabel;
import ensino.components.GenJList;
import ensino.components.GenJTextField;
import ensino.configuracoes.model.Atividade;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.Legenda;
import ensino.configuracoes.view.models.LegendaListModel;
import ensino.configuracoes.view.renderer.LegendaListCellRenderer;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.util.types.Periodo;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.ParseException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 *
 * @author santos
 */
public class CalendarioAtividadesFields extends DefaultFieldsPanel {

    private final Calendario selectedCalendario;
    
    private GenJTextField txtId;
    private GenJFormattedTextField txtDe;
    private GenJFormattedTextField txtAte;
    private GenJTextField txtAtividade;
    private GenJList listLegenda;
    
    private Atividade atividade;
    
    public CalendarioAtividadesFields(Calendario calendario) {
        super();
        this.selectedCalendario = calendario;
        initComponents();
    }
    
    private void initComponents() {
        try {
            setName("calendario.atividade.cadastro");
            setLayout(new BorderLayout(10, 10));
            setBorder(BorderFactory.createEtchedBorder());
            
            GenJLabel lblTitulo = new GenJLabel("Ficha da Atividade");
            lblTitulo.resetFontSize(20);
            lblTitulo.setForeground(foreColor);
            lblTitulo.toBold();
            
            JPanel panel = createPanel(new FlowLayout(FlowLayout.CENTER));
            panel.add(lblTitulo);
            
            add(panel, BorderLayout.PAGE_START);
            add(createIdentificacaoPanel(), BorderLayout.CENTER);
        } catch (ParseException ex) {
            showErrorMessage(ex);
        }
    }
    
    private JPanel createIdentificacaoPanel() throws ParseException {
        JPanel panel = createPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        GenJLabel lblId = new GenJLabel("Código:", JLabel.TRAILING);
        txtId = new GenJTextField(5, false);
        txtId.setEnabled(false);
        lblId.setLabelFor(txtId);

        txtDe = GenJFormattedTextField.createFormattedField("##/##/####", 1);
        txtDe.setColumns(8);
        txtDe.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (txtDe.getValue() != null && txtAte.getValue() == null) {
                    try {
                        txtDe.commitEdit();
                        txtAte.setValue(txtDe.getValue());
                        txtAtividade.requestFocusInWindow();
                    } catch (ParseException ex) {
                        Logger.getLogger(CalendarioAtividadesPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        });

        txtAte = GenJFormattedTextField.createFormattedField("##/##/####", 1);
        txtAte.setColumns(8);

        JPanel panelPeriodo = createPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        panelPeriodo.setBorder(createTitleBorder("Período da atividade"));
        panelPeriodo.add(txtDe);
        panelPeriodo.add(new GenJLabel(" a "));
        panelPeriodo.add(txtAte);

        GenJLabel lblDescricao = new GenJLabel("Atividade: ", JLabel.TRAILING);
        txtAtividade = new GenJTextField(20, false);
        lblDescricao.setLabelFor(txtAtividade);

        listLegenda = new GenJList(new LegendaListModel());
        listLegenda.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listLegenda.setLayoutOrientation(JList.VERTICAL);
        listLegenda.setVisibleRowCount(4);
        JScrollPane scrollLegenda = new JScrollPane(listLegenda);
        scrollLegenda.setBorder(createTitleBorder("Legenda"));
        scrollLegenda.setAutoscrolls(true);

        listLegenda.setCellRenderer(new LegendaListCellRenderer());

        int col = 0, row = 0;
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblId, c);
        GridLayoutHelper.set(c, col++, row);
        panel.add(txtId, c);

        GridLayoutHelper.set(c, col++, row);
        panel.add(panelPeriodo, c);

        GridLayoutHelper.set(c, col, row++, 1, 3, GridBagConstraints.LINE_START);
        panel.add(scrollLegenda, c);

        col = 0;
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblDescricao, c);
        GridLayoutHelper.set(c, col++, row++, 2, 1, GridBagConstraints.LINE_START);
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(txtAtividade, c);

        return panel;
    }
    
    @Override
    public HashMap<String, Object> getFieldValues() {
        try {
            HashMap<String, Object> map = new HashMap<>();
            
            map.put("id", "".equals(txtId.getText()) ? null
                    : Long.parseLong(txtId.getText()));
            map.put("periodo", new Periodo(txtDe.getText(), txtAte.getText()));
            map.put("descricao", txtAtividade.getText());
            map.put("legenda", listLegenda.getSelectedValue());
            map.put("calendario", this.selectedCalendario);
            return map;
        } catch (ParseException ex) {
            showErrorMessage(ex);
            return null;
        }
    }

    private void setFieldValues(Long id, String descricao, Periodo periodo,
            Legenda legenda) {
        txtId.setText(id.toString());
        txtAtividade.setText(descricao);
        txtDe.setText(periodo.getDeText());
        txtAte.setText(periodo.getAteText());
        listLegenda.setSelectedValue(legenda);
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {
        setFieldValues(
                (Long) mapValues.get("id"),
                (String) mapValues.get("descricao"),
                (Periodo) mapValues.get("periodo"),
                (Legenda) mapValues.get("legenda"));
    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof Atividade) {
            atividade = (Atividade) object;
            setFieldValues(
                    atividade.getId().getId(),
                    atividade.getDescricao(),
                    atividade.getPeriodo(),
                    atividade.getLegenda());
        }
    }

    @Override
    public boolean isValidated() {
        String msg = "O campo %s não foi informado!", campo = "";
        if (txtDe.getValue() == null) {
            campo = "Data de Início";
            txtDe.requestFocusInWindow();
        } else if (txtAte.getValue() == null) {
            campo = "Data Final";
            txtAte.requestFocusInWindow();
        } else if ("".equals(txtAtividade.getText())) {
            campo = "Descrição";
            txtAtividade.requestFocusInWindow();
        } else if (listLegenda.getSelectedValue() == null) {
            campo = "Legenda";
            listLegenda.requestFocusInWindow();
        } else {
            return true;
        }
        showInformationMessage(String.format(msg, campo));
        return false;
    }

    @Override
    public void clearFields() {
        txtId.setText("");
        txtAte.setText("");
        txtAte.setValue(null);
        txtDe.setText("");
        txtDe.setValue(null);
        txtAtividade.setText("");
        listLegenda.setSelectedValue(null);
    }

    @Override
    public void enableFields(boolean active) {
        txtId.setEnabled(active);
        txtAte.setEnabled(active);
        txtDe.setEnabled(active);
        txtAtividade.setEnabled(active);
        listLegenda.setEnabled(active);
    }

    @Override
    public void initFocus() {
        txtDe.requestFocusInWindow();
    }
    
}
