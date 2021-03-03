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
        return selectedCalendario != null && !selectedCalendario.getAtividades().isEmpty();
    }

    @Override
    public void createSelectButton() {
        
    }

    @Override
    public Object getSelectedObject() {
        return null;
    }
}
