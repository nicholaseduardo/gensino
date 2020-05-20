/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels;

import ensino.components.GenJLabel;
import ensino.configuracoes.model.Legenda;
import ensino.configuracoes.view.models.LegendaTableModel;
import ensino.configuracoes.view.renderer.ColorCellRenderer;
import ensino.defaults.DefaultFieldsPanel;
import ensino.defaults.DefaultFormPanel;
import ensino.defaults.SpringUtilities;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import ensino.components.GenJCheckBox;
import ensino.components.GenJTextField;
import ensino.patterns.factory.ControllerFactory;
import java.awt.Component;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

/**
 *
 * @author nicho
 */
public class LegendaPanel extends DefaultFormPanel {

    public LegendaPanel(Component iframe) {
        super(iframe);
        try {
            super.setName("panel.legenda");
            super.setTitlePanel("Dados de Legenda");
            
            super.setController(ControllerFactory.createLegendaController());
            
            super.enableTablePanel();
            
            super.setFieldsPanel(new LegendaFields());
            super.showPanelInCard(CARD_LIST);
        } catch (Exception ex) {
            Logger.getLogger(LegendaPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void resizeTableColumns() {
        javax.swing.JTable table = getTable();
        javax.swing.table.TableColumnModel tcm = table.getColumnModel();
        
        tcm.getColumn(0).setMaxWidth(50);
        tcm.getColumn(1).setMinWidth(50);
        tcm.getColumn(2).setMaxWidth(50);
        tcm.getColumn(3).setMaxWidth(100);
        tcm.getColumn(4).setMaxWidth(50);
        tcm.getColumn(4).setCellRenderer(new ColorCellRenderer());
    }

    @Override
    public void onSearchButton(ActionEvent e) {
        
    }

    @Override
    public void addFiltersFields() {
        
    }

    @Override
    public void reloadTableData() {
            setTableModel(new LegendaTableModel(getController().listar()));
            resizeTableColumns();
    }

    @Override
    public void createSelectButton() {
        
    }

    @Override
    public Object getSelectedObject() {
        return null;
    }
    
    private class LegendaFields extends DefaultFieldsPanel {
        
        private GenJTextField txtId;
        private GenJTextField txtNome;
        private GenJCheckBox checkLetivo;
        private GenJCheckBox checkInformativo;
        private GenJLabel lblCorSelecionada;
        private JButton btCor;
        
        public LegendaFields() {
            super();
            initComponents();
        }
        
        private void initComponents() {
            SpringLayout layout = new SpringLayout();
            
            JPanel fieldsPanel = new JPanel(layout);
            GenJLabel lblId = new GenJLabel("Código:", JLabel.TRAILING);
            txtId = new GenJTextField(10, false);
            txtId.setFocusable(true);
            lblId.setLabelFor(txtId);
            
            GenJLabel lblNome = new GenJLabel("Nome:", JLabel.TRAILING);
            txtNome = new GenJTextField(30, true);
            lblNome.setLabelFor(txtNome);
            
            fieldsPanel.add(lblId);
            fieldsPanel.add(txtId);
            
            fieldsPanel.add(lblNome);
            fieldsPanel.add(txtNome);
            
            fieldsPanel.add(new GenJLabel("Cor da legenda:", JLabel.TRAILING));
            lblCorSelecionada = new GenJLabel("Cor Selecionada");
            Font font = lblCorSelecionada.getFont();
            lblCorSelecionada.setFont(font.deriveFont(Font.BOLD));
            lblCorSelecionada.setOpaque(true);
            lblCorSelecionada.setBorder(BorderFactory.createEtchedBorder());
            fieldsPanel.add(lblCorSelecionada);
            fieldsPanel.add(new GenJLabel(" "));
            btCor = new JButton("Selecionar cor");
            btCor.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Color c = JColorChooser.showDialog(getFrame(), "Escolha uma cor", lblCorSelecionada.getBackground());
                    if (c != null) {
                        lblCorSelecionada.setBackground(c);
                    }
                }
            });
            fieldsPanel.add(btCor);
            
            
            fieldsPanel.add(new GenJLabel(" "));
            checkLetivo = new GenJCheckBox("É dia letivo?");
            fieldsPanel.add(checkLetivo);
            
            fieldsPanel.add(new GenJLabel(" "));
            checkInformativo = new GenJCheckBox("Registra apenas informação?");
            fieldsPanel.add(checkInformativo);
            
            SpringUtilities.makeCompactGrid(fieldsPanel, 6, 2, 6, 6, 6, 6);
            add(fieldsPanel);
        }

        @Override
        public HashMap<String, Object> getFieldValues() {
            HashMap<String, Object> map = new HashMap<>();
            
            map.put("id", ("".equals(txtId.getText()) ? null : 
                    Integer.parseInt(txtId.getText())));
            map.put("nome", txtNome.getText());
            map.put("letivo", checkLetivo.isSelected());
            map.put("informativo", checkInformativo.isSelected());
            map.put("cor", lblCorSelecionada.getBackground());
            return map;
        }
        
        private void setFieldValues(Integer codigo, String nome, Boolean letivo, Boolean informativo, Color cor) {
            txtId.setText(codigo.toString());
            txtNome.setText(nome);
            checkLetivo.setSelected(letivo);
            checkInformativo.setSelected(informativo);
            lblCorSelecionada.setBackground(cor);
        }

        @Override
        public void setFieldValues(HashMap<String, Object> mapValues) {
            Integer codigo = (Integer)mapValues.get("id");
            setFieldValues(codigo, (String)mapValues.get("nome"),
                    (Boolean)mapValues.get("letivo"), 
                    (Boolean)mapValues.get("informativo"),
                    (Color)mapValues.get("cor"));
        }

        @Override
        public void setFieldValues(Object object) {
            if (object instanceof Legenda) {
                Legenda legenda = (Legenda) object;
                setFieldValues(legenda.getId(), legenda.getNome(),
                        legenda.isLetivo(), legenda.isInformativo(),
                        legenda.getCor());
            }
        }

        @Override
        public boolean isValidated() {
            return !"".equals(txtNome.getText());
        }

        @Override
        public void clearFields() {
            txtId.setText("");
            txtNome.setText("");
            checkLetivo.setSelected(false);
            checkInformativo.setSelected(false);
            lblCorSelecionada.setBackground(Color.black);
        }

        @Override
        public void enableFields(boolean active) {
            txtId.setEnabled(false);
            txtNome.setEnabled(active);
            checkLetivo.setEnabled(active);
            checkInformativo.setEnabled(active);
            btCor.setEnabled(active);
        }

        @Override
        public void initFocus() {
            txtNome.requestFocusInWindow();
        }
        
    }
    
}
