/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.diario;

import ensino.components.GenJButton;
import ensino.components.GenJComboBox;
import ensino.components.GenJFormattedTextField;
import ensino.components.GenJLabel;
import ensino.defaults.DefaultCleanFormPanel;
import ensino.helpers.DateHelper;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.controller.DiarioController;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.models.DiarioTableModel;
import ensino.planejamento.view.renderer.DiarioCellRenderer;
import ensino.util.types.AcoesBotoes;
import ensino.util.types.TipoAula;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author santos
 */
public class DiarioConteudoPanel extends DefaultCleanFormPanel {

    private PlanoDeEnsino planoDeEnsino;
    private GenJComboBox comboTipoAula;
    private GenJFormattedTextField txtData;
    private GenJButton btSearch;
    private GenJButton btClear;

    public DiarioConteudoPanel(PlanoDeEnsino planoDeEnsino) {
        this(null, planoDeEnsino);
    }

    public DiarioConteudoPanel(Component frame, PlanoDeEnsino planoDeEnsino) {
        super(frame);
        try {
            this.planoDeEnsino = planoDeEnsino;
            URL url = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "content-icon-50px.png"));

            setName("panel.planoDeEnsino.diario.conteudo");
            setTitlePanel("Dados do Conteúdo Programático", new ImageIcon(url));
            setController(ControllerFactory.createDiarioController());

            setFieldsPanel(new DiarioConteudoFieldsPanel(planoDeEnsino));
            enableTablePanel();
            showPanelInCard(CARD_LIST);
        } catch (Exception ex) {
            showErrorMessage(ex);
            ex.printStackTrace();
        }
    }

    private void resizeTableColumns() {
        JTable table = getTable();
        getModel().activateButtons();
        ListSelectionModel cellSelectionModel = table.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        TableColumnModel tcm = table.getColumnModel();
        TableColumn col0 = tcm.getColumn(0);
        col0.setMinWidth(400);
        col0.setCellRenderer(new DiarioCellRenderer());

        EnumSet enumSet = EnumSet.of(AcoesBotoes.DEL, AcoesBotoes.EDIT);

        TableColumn col1 = tcm.getColumn(1);
        col1.setCellRenderer(new ButtonsRenderer(null, enumSet));
        col1.setCellEditor(new ButtonsEditor(table, null, enumSet));
    }

    @Override
    public void reloadTableData() {
        try {
            DiarioController col = (DiarioController) getController();
            String sDate = (String) txtData.getValue();
            Date data = sDate == null ? null : DateHelper.stringToDate(sDate, "dd/MM/yyyy");
            setTableModel(new DiarioTableModel(col.listar(planoDeEnsino,
                    data, (TipoAula) comboTipoAula.getSelectedItem())));
            resizeTableColumns();
        } catch (ParseException ex) {
            showErrorMessage(ex);
            ex.printStackTrace();
        }
    }

    @Override
    public Object getSelectedObject() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addFiltersFields() {
        try {
            GenJLabel lblData = new GenJLabel("Data:");
            Calendar cal = Calendar.getInstance();
            txtData = GenJFormattedTextField.createFormattedField("##/##/####", 1);
            txtData.setColumns(8);
            lblData.setLabelFor(txtData);

            GenJLabel lblTipo = new GenJLabel("Tipo: ", JLabel.TRAILING);
            comboTipoAula = new GenJComboBox(TipoAula.values());
            comboTipoAula.addItem(null);
            comboTipoAula.setSelectedItem(null);
            lblTipo.setLabelFor(comboTipoAula);

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

            GridLayoutHelper.set(c, col++, row);
            panel.add(txtData, c);

            GridLayoutHelper.setRight(c, col++, row);
            panel.add(lblTipo, c);

            GridLayoutHelper.set(c, col++, row++);
            panel.add(comboTipoAula, c);

            GridLayoutHelper.set(c, 0, row, 4, 1, GridBagConstraints.LINE_END);
            panel.add(panelButton, c);
        } catch (ParseException ex) {
            showErrorMessage(ex);
            ex.printStackTrace();
        }
    }

    @Override
    public void onClearAction(ActionEvent e) {
        txtData.setValue(null);
        comboTipoAula.setSelectedItem(null);
        onSearchAction(e);
    }

}
