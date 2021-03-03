/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.permanenciaEstudantil;

import ensino.components.GenJButton;
import ensino.components.GenJFormattedTextField;
import ensino.components.GenJLabel;
import ensino.defaults.DefaultCleanFormPanel;
import ensino.helpers.DateHelper;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.controller.PermanenciaEstudantilController;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.models.PermanenciaEstudantilTableModel;
import ensino.planejamento.view.renderer.PermanenciaEstudantilCellRenderer;
import ensino.util.types.AcoesBotoes;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.util.Date;
import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author nicho
 */
public class PermanenciaEstudantilPanel extends DefaultCleanFormPanel {

    private PlanoDeEnsino planoDeEnsino;
    
    private GenJFormattedTextField txtData;
    private GenJButton btSearch;
    private GenJButton btClear;

    public PermanenciaEstudantilPanel(Component frame) {
        this(frame, null);
    }
    
    public PermanenciaEstudantilPanel(Component frame, PlanoDeEnsino planoDeEnsino) {
        super(frame);

        this.planoDeEnsino = planoDeEnsino;
        try {
            super.setName("panel.permanenciaEstudantil");
            super.setTitlePanel("Dados da Permanencia Estudantil");
            // para capturar os dados do planoDeEnsino, usa-se a estrutura do campus
            super.setController(ControllerFactory.createPermanenciaEstudantilController());

            super.enableTablePanel();
            super.setFieldsPanel(new PermanenciaEstudantilFieldsPanel(planoDeEnsino));
            super.showPanelInCard(CARD_LIST);
        } catch (Exception ex) {
            Logger.getLogger(PermanenciaEstudantilPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Object getSelectedObject() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void resizeTableColumns() {
        JTable table = getTable();
        getModel().activateButtons();
        ListSelectionModel cellSelectionModel = table.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        TableColumnModel tcm = table.getColumnModel();
        TableColumn col0 = tcm.getColumn(0);
        col0.setMinWidth(400);
        col0.setCellRenderer(new PermanenciaEstudantilCellRenderer());

        EnumSet enumSet = EnumSet.of(AcoesBotoes.DEL, AcoesBotoes.EDIT);

        TableColumn col1 = tcm.getColumn(1);
        col1.setCellRenderer(new ButtonsRenderer(null, enumSet));
        col1.setCellEditor(new ButtonsEditor(table, null, enumSet));
    }

    @Override
    public void reloadTableData() {
        try {
            PermanenciaEstudantilController col = (PermanenciaEstudantilController) getController();
            
            String sDate = (String) txtData.getValue();
            Date data = sDate == null ? null : DateHelper.stringToDate(sDate, "dd/MM/yyyy");
            
            setTableModel(new PermanenciaEstudantilTableModel(col
                    .listar(planoDeEnsino, data)));
            resizeTableColumns();
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    @Override
    public void addFiltersFields() {
        try {
            GenJLabel lblData = new GenJLabel("Data:");
            
            txtData = GenJFormattedTextField.createFormattedField("##/##/####", 1);
            txtData.setColumns(8);
            lblData.setLabelFor(txtData);

            btSearch = createButton(new ActionHandler(AcoesBotoes.SEARCH));
            btClear = createButton(new ActionHandler(AcoesBotoes.CLEAR));

            JPanel panelButton = createPanel(new FlowLayout(FlowLayout.RIGHT));
            panelButton.add(btSearch);
            panelButton.add(btClear);

            JPanel panel = getFilterPanel();
            panel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            int col = 0, row = 0;
            GridLayoutHelper.setRight(c, col++, row);
            panel.add(lblData, c);

            GridLayoutHelper.set(c, col++, row++);
            panel.add(txtData, c);

            GridLayoutHelper.set(c, 0, row, 4, 1, GridBagConstraints.LINE_END);
            panel.add(panelButton, c);
        } catch (ParseException ex) {
            showErrorMessage(ex);
        }
    }

    @Override
    public void onClearAction(ActionEvent e) {
        txtData.setValue(null);
        onSearchAction(e);
    }

}
