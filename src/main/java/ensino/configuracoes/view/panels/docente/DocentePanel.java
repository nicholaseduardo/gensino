/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.docente;

import ensino.components.GenJTextField;
import ensino.configuracoes.model.Docente;
import ensino.configuracoes.view.models.DocenteTableModel;
import ensino.defaults.DefaultFieldsPanel;
import ensino.defaults.DefaultFormPanel;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import ensino.components.GenJLabel;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.ControllerFactory;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author nicho
 */
public class DocentePanel extends DefaultFormPanel {

    public DocentePanel(Component frame) {
        super(frame);
        try {
            setName("panel.docente");
            setTitlePanel("Dados do Docente");
            setController(ControllerFactory.createDocenteController());

            enableTablePanel();
            setFieldsPanel(new DocenteFieldsPanel());

            showPanelInCard(CARD_LIST);
        } catch (Exception ex) {
            Logger.getLogger(DocentePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void resizeTableColumns() {
        javax.swing.JTable table = getTable();
        javax.swing.table.TableColumnModel tcm = table.getColumnModel();
        tcm.getColumn(0).setMaxWidth(50);
        tcm.getColumn(1).setMinWidth(50);
    }

    @Override
    public void onSearchButton(ActionEvent e) {

    }

    @Override
    public void addFiltersFields() {

    }

    @Override
    public void reloadTableData() {
        setTableModel(new DocenteTableModel(getController().listar()));
        resizeTableColumns();
    }

    @Override
    public void createSelectButton() {
        
    }

    @Override
    public Object getSelectedObject() {
        return null;
    }

}
