/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels;

import ensino.configuracoes.controller.CalendarioController;
import ensino.configuracoes.model.Atividade;
import ensino.configuracoes.model.Calendario;
import ensino.defaults.DefaultFieldsPanel;
import ensino.defaults.DefaultFormPanel;
import ensino.patterns.factory.ControllerFactory;
import ensino.util.JCalendario;
import ensino.util.types.MesesDeAno;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author nicho
 */
public class AtividadePanel extends DefaultFormPanel {

    private AtividadeFields atividadeFields;
    private JCalendario componenteCalendario;
    private JTabbedPane tabbedPane;
    private Calendario selectedCalendario;

    public AtividadePanel(JInternalFrame iframe) {
        super(iframe);
        try {
            setName("panel.atividade");
            setTitlePanel("Atividades");
            setController(ControllerFactory.createAtividadeController());

            tabbedPane = new JTabbedPane();
            tabbedPane.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if (e.getSource() instanceof JTabbedPane) {
                        JTabbedPane source = (JTabbedPane) e.getSource();
                        // recupera a TAB selecionada
                        Component component = source.getSelectedComponent();
                        if (component instanceof JCalendario) {
                            JCalendario sourceTabbedPane = (JCalendario) component;
                            sourceTabbedPane.reloadCalendar();
                        }
                    }
                }
            });
            enableTablePanel(tabbedPane);
            reloadTableData();
            // Campos
            atividadeFields = new AtividadeFields();
            setFieldsPanel(atividadeFields);
            showPanelInCard(CARD_LIST);

        } catch (Exception ex) {
            Logger.getLogger(AtividadePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Calendario getCalendario() {
        return selectedCalendario;
    }

    public void setCalendario(Calendario calendario) {
        this.selectedCalendario = calendario;
        reloadTableData();
        componentsControl(0);
    }

    public void reloadTableData() {
        List atividadesLis = new ArrayList();
        if (selectedCalendario != null) {
            try {
                // busca a lista de atividades
                CalendarioController calCol = ControllerFactory.createCalendarioController();
                selectedCalendario = calCol.buscarPor(selectedCalendario.getId().getAno(), 
                        selectedCalendario.getId().getCampus());
                atividadesLis = selectedCalendario.getAtividades();

                atividadeFields.setCalendario(selectedCalendario);
            } catch (Exception ex) {
                Logger.getLogger(AtividadePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        int count = tabbedPane.getTabCount();
        for (MesesDeAno mes : MesesDeAno.values()) {
            if (count < 1) {
                // O componente não existe
                tabbedPane.addTab(mes.name(), new JCalendario(2019, mes, atividadesLis));
            } else {
                int index = mes.getValue() - 1;
                // Componente existe e a lista de atividades é atualizada

                Component tabComponent = tabbedPane.getComponentAt(index);
                ((JCalendario) tabComponent).setAtividades(atividadesLis);
            }
        }
    }

    @Override
    public void onSearchButton(ActionEvent e) {

    }

    @Override
    public void addFiltersFields() {

    }

    @Override
    public void onSaveAction(ActionEvent e) {
        DefaultFieldsPanel inPanel = getFieldsPanel();
        if (inPanel.isValidated()) {
            HashMap<String, Object> params = inPanel.getFieldValues();
            try {
                Object object = getController().salvar(params);
                if (inPanel.getStatusPanel() == DefaultFieldsPanel.UPDATE_STATUS_PANEL) {
                    selectedCalendario.updAtividade((Atividade) object);
                } else {
                    selectedCalendario.addAtividade((Atividade) object);
                }
                JOptionPane.showMessageDialog(getFrame(), "Dados gravados com sucesso!",
                        "Informação", JOptionPane.INFORMATION_MESSAGE);
                componenteCalendario = (JCalendario) tabbedPane.getSelectedComponent();
                componenteCalendario.setAtividades(selectedCalendario.getAtividades());
                componentsControl(0);
                showPanelInCard(CARD_LIST);
            } catch (Exception ex) {
                Logger.getLogger(DefaultFormPanel.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(getFrame(), ex.getMessage(),
                        "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(getFrame(), "Os campos em Asterisco (*) não foram preenchidos/selecioados.",
                    "Informação", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Sobrescrita do método para aceitar o controle dos dados através do JList
     * do componente JCalendario
     *
     * @param e
     */
    @Override
    public void onViewButton(ActionEvent e) {
        if (selectedCalendario != null) {
            componenteCalendario = (JCalendario) tabbedPane.getSelectedComponent();
            Atividade selectedItem = componenteCalendario.getSelectedAtividade();
            if (selectedItem == null) {
                JOptionPane.showMessageDialog(null,
                        "Selecione uma atividade para realizar a operação de alteração",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                getFieldsPanel().setStatusPanel(DefaultFieldsPanel.UPDATE_STATUS_PANEL);
                getFieldsPanel().setFieldValues(selectedItem);
                componentsControl(2);
                showPanelInCard(CARD_FICHA);
            }
        }
    }

    /**
     * Método sobrescrito para controlar a exclusão do item da JList
     *
     * @param e
     */
    @Override
    public void onDeleteButton(ActionEvent e) {
        componenteCalendario = (JCalendario) tabbedPane.getSelectedComponent();
        Atividade selectedItem = componenteCalendario.getSelectedAtividade();
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(getFrame(),
                    "Selecione uma atividade para realizar a operação de alteração",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                if (JOptionPane.showConfirmDialog(getFrame(), "Confirma a exclusão do registro?", "Confirmação",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.CANCEL_OPTION) {
                    return;
                }
                getFieldsPanel().setStatusPanel(DefaultFieldsPanel.UPDATE_STATUS_PANEL);
                getController().remover(selectedItem);
                // remove o objeto da lista cuja linha já foi marcada como selecionada
                componenteCalendario.getAtividades().remove(selectedItem);
                componenteCalendario.reloadCalendar();
                componentsControl(0);
                JOptionPane.showMessageDialog(null,
                        "Dados excluídos com sucesso!", "Confirmação",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                Logger.getLogger(DefaultFormPanel.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(getFrame(), ex.getMessage(),
                        "Aviso", JOptionPane.WARNING_MESSAGE);
            }

        }
    }

    /**
     * Método sobrescrito para controlar os botões
     *
     * @return
     */
    @Override
    protected boolean hasData() {
        return (selectedCalendario != null && !selectedCalendario.getAtividades().isEmpty());
    }

//    private class AtividadeFields extends DefaultFieldsPanel {
//
//        private GenJTextField txtId;
//        private GenJFormattedTextField txtDe;
//        private GenJFormattedTextField txtAte;
//        private GenJTextField txtDescricao;
//        private GenJComboBox comboLegenda;
//
//        public AtividadeFields() {
//            super();
//            initComponents();
//        }
//
//        private void initComponents() {
//            try {
//                JPanel fieldsPanel = new JPanel(new GridBagLayout());
//                GridBagConstraints c = new GridBagConstraints();
//
//                GenJLabel lblId = new GenJLabel("Código:", JLabel.TRAILING);
//                GridLayoutHelper.setRight(c, 0, 0);
//                fieldsPanel.add(lblId, c);
//                txtId = new GenJTextField(10);
//                lblId.setLabelFor(txtId);
//                GridLayoutHelper.set(c, 1, 0);
//                fieldsPanel.add(txtId, c);
//
//                GenJLabel lblDe = new GenJLabel("De: ", JLabel.TRAILING);
//                GridLayoutHelper.setRight(c, 0, 1);
//                fieldsPanel.add(lblDe, c);
//                txtDe = createFormattedField("##/##/####", 1);
//                lblDe.setLabelFor(txtDe);
//                GridLayoutHelper.set(c, 1, 1);
//                c.fill = GridBagConstraints.HORIZONTAL;
//                fieldsPanel.add(txtDe, c);
//
//                GenJLabel lblAte = new GenJLabel("Até: ", JLabel.TRAILING);
//                GridLayoutHelper.setRight(c, 2, 1);
//                fieldsPanel.add(lblAte, c);
//                txtAte = createFormattedField("##/##/####", 1);
//                lblAte.setLabelFor(txtAte);
//                GridLayoutHelper.set(c, 3, 1);
//                c.fill = GridBagConstraints.HORIZONTAL;
//                fieldsPanel.add(txtAte, c);
//
//                GenJLabel lblDescricao = new GenJLabel("Descrição: ", JLabel.TRAILING);
//                GridLayoutHelper.setRight(c, 0, 2);
//                fieldsPanel.add(lblDescricao, c);
//                txtDescricao = new GenJTextField(25);
//                lblDescricao.setLabelFor(txtDescricao);
//                GridLayoutHelper.set(c, 1, 2);
//                c.gridwidth = 3;
//                fieldsPanel.add(txtDescricao, c);
//
//                GenJLabel lblLegenda = new GenJLabel("Legenda: ", JLabel.TRAILING);
//                GridLayoutHelper.setRight(c, 0, 3);
//                fieldsPanel.add(lblLegenda, c);
//                LegendaController legendaCol = new LegendaController();
//                comboLegenda = new GenJComboBox(legendaCol.listar().toArray());
//                lblLegenda.setLabelFor(comboLegenda);
//                GridLayoutHelper.set(c, 1, 3);
//                c.gridwidth = 3;
//                fieldsPanel.add(comboLegenda, c);
//
//                add(fieldsPanel);
//            } catch (ParseException | IOException ex) {
//                Logger.getLogger(AtividadePanel.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
//        /**
//         * Cria um campo formatado
//         *
//         * @param format Formato a ser mostrado
//         * @param type Type = 0, para números e Type = 1 para outros.
//         * @return
//         * @throws ParseException
//         */
//        private GenJFormattedTextField createFormattedField(String format, Integer type) throws ParseException {
//            DefaultFormatterFactory formatter = null;
//            if (type == 0) {
//                formatter = new DefaultFormatterFactory(new NumberFormatter(new DecimalFormat("#0.00")));
//            } else {
//                formatter = new DefaultFormatterFactory(new MaskFormatter(format));
//            }
//            GenJFormattedTextField textField = new GenJFormattedTextField();
//            textField.setFormatterFactory(formatter);
//            return textField;
//        }
//
//        @Override
//        public HashMap<String, Object> getFieldValues() {
//            try {
//                HashMap<String, Object> map = new HashMap<>();
//
//                map.put("id", ("".equals(txtId.getText()) ? null
//                        : Integer.parseInt(txtId.getText())));
//                map.put("periodo", new Periodo(txtDe.getText(), txtAte.getText()));
//                map.put("descricao", txtDescricao.getText());
//                map.put("legenda", comboLegenda.getSelectedItem());
//                map.put("calendario", calendario);
//                return map;
//            } catch (ParseException ex) {
//                Logger.getLogger(AtividadePanel.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            return null;
//        }
//
//        private void setFieldValues(Integer id, Periodo periodo,
//                String descricao, Legenda legenda) {
//            txtId.setText(id.toString());
//            txtDe.setText(periodo.getDeText());
//            txtAte.setText(periodo.getAteText());
//            txtDescricao.setText(descricao);
//            comboLegenda.setSelectedItem(legenda);
//        }
//
//        @Override
//        public void setFieldValues(HashMap<String, Object> mapValues) {
//            Integer id = (Integer) mapValues.get("id");
//            setFieldValues(id,
//                    (Periodo) mapValues.get("periodo"),
//                    (String) mapValues.get("descricao"),
//                    (Legenda) mapValues.get("legenda"));
//        }
//
//        @Override
//        public void setFieldValues(Object object) {
//            if (object instanceof Atividade) {
//                Atividade at = (Atividade) object;
//                setFieldValues(at.getId(), at.getPeriodo(),
//                        at.getDescricao(), at.getLegenda());
//            }
//        }
//
//        @Override
//        public boolean isValidaded() {
//            try {
//                txtDe.commitEdit();
//                txtAte.commitEdit();
//                return txtDe.getValue() != null
//                        && txtAte.getValue() != null
//                        && (!"".equals(txtDescricao.getText()))
//                        && comboLegenda.getSelectedItem() != null;
//            } catch (ParseException ex) {
//                Logger.getLogger(AtividadePanel.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            return false;
//        }
//
//        @Override
//        public void clearFields() {
//            txtId.setText("");
//            txtDe.setText("");
//            txtAte.setText("");
//            txtDescricao.setText("");
//            comboLegenda.setSelectedItem(null);
//        }
//
//        @Override
//        public void enableFields(boolean active) {
//            txtId.setEnabled(false);
//            txtDe.setEnabled(active);
//            txtAte.setEnabled(active);
//            txtDescricao.setEnabled(active);
//            comboLegenda.setEnabled(active);
//        }
//
//        @Override
//        public void initFocus() {
//            txtDe.requestFocusInWindow();
//        }
//
//    }

    @Override
    public void createSelectButton() {
        
    }

    @Override
    public Object getSelectedObject() {
        return null;
    }
}
