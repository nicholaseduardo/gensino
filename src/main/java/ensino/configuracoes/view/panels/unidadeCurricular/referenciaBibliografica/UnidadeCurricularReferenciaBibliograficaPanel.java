/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.unidadeCurricular.referenciaBibliografica;

import ensino.components.GenJButton;
import ensino.components.GenJComboBox;
import ensino.components.GenJLabel;
import ensino.configuracoes.controller.ReferenciaBibliograficaController;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.configuracoes.view.models.ReferenciaBibliograficaTableModel;
import ensino.configuracoes.view.renderer.ReferenciaBibliograficaCellRenderer;
import ensino.defaults.DefaultCleanFormPanel;
import static ensino.defaults.DefaultCleanFormPanel.CARD_LIST;
import ensino.patterns.factory.ControllerFactory;
import ensino.util.types.AcoesBotoes;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.EnumSet;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

/**
 *
 * @author santos
 */
public class UnidadeCurricularReferenciaBibliograficaPanel extends DefaultCleanFormPanel {

    private UnidadeCurricular unidadeCurricular;

    private GenJComboBox comboTipoRef;
    private GenJButton btSearch;
    private GenJButton btClear;

    public UnidadeCurricularReferenciaBibliograficaPanel(Component frame) {
        this(frame, null);
    }

    public UnidadeCurricularReferenciaBibliograficaPanel(Component frame,
            UnidadeCurricular unidadeCurricular) {
        super(frame);
        this.unidadeCurricular = unidadeCurricular;
        initComponents();
    }

    private void initComponents() {
        try {
            setName("panel.unidadeCurricular.referenciaBibliografica");
            setTitlePanel("Dados de Referências Bibliográficas");
            setController(ControllerFactory.createReferenciaBibliograficaController());

            enableTablePanel();
            setFieldsPanel(new UnidadeCurricularReferenciaBiliograficaFields(this.unidadeCurricular));
            showPanelInCard(CARD_LIST);
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    private void resizeTableColumns() {
        JTable table = getTable();

        TableColumn tc0 = table.getColumnModel().getColumn(0);
        tc0.setCellRenderer(new ReferenciaBibliograficaCellRenderer());

        EnumSet enumSet = EnumSet.of(AcoesBotoes.EDIT, AcoesBotoes.DEL);

        TableColumn col1 = table.getColumnModel().getColumn(1);
        col1.setCellRenderer(new ButtonsRenderer(null, enumSet));
        col1.setCellEditor(new ButtonsEditor(table, null, enumSet));
    }

    @Override
    public void reloadTableData() {
        ReferenciaBibliograficaController col = (ReferenciaBibliograficaController) getController();
        setTableModel(new ReferenciaBibliograficaTableModel(
                col.listar(unidadeCurricular, comboTipoRef.getSelectedIndex())));
        resizeTableColumns();
    }
    
    private void clearFields() {
        comboTipoRef.setSelectedIndex(-1);
    }

    @Override
    public void addFiltersFields() {
        GenJLabel lblTipo = new GenJLabel("Tipo de bibliografia: ", JLabel.TRAILING);
        String tipoList[] = {"Referência básica", "Referência complementar"};
        comboTipoRef = new GenJComboBox(tipoList);
        clearFields();

        btSearch = createButton(new ActionHandler(AcoesBotoes.SEARCH));
        btClear = createButton(new ActionHandler(AcoesBotoes.CLEAR));

        JPanel filterPanel = getFilterPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(lblTipo);
        filterPanel.add(comboTipoRef);
        filterPanel.add(btSearch);
        filterPanel.add(btClear);
    }

    @Override
    public void onClearAction(ActionEvent e) {
        clearFields();
        reloadTableData();
    }

}
