/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.objetivoEspecifico;

import ensino.components.GenJButton;
import static ensino.components.GenJPanel.IMG_SOURCE;
import ensino.components.renderer.TextAreaCellRenderer;
import ensino.configuracoes.model.ObjetivoUC;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.defaults.DefaultCleanFormPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.controller.ObjetivoController;
import ensino.planejamento.model.Objetivo;
import ensino.planejamento.model.ObjetivoFactory;
import ensino.planejamento.model.ObjetivoId;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.models.ObjetivoTableModel;
import ensino.util.types.AcoesBotoes;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.EnumSet;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author santos
 */
public class PlanoDeEnsinoObjetivoEspecifico extends DefaultCleanFormPanel {

    private PlanoDeEnsino planoDeEnsino;
    private GenJButton btImport;

    public PlanoDeEnsinoObjetivoEspecifico(Component frame, PlanoDeEnsino planoDeEnsino) {
        super(frame);

        try {
            this.planoDeEnsino = planoDeEnsino;
            ImageIcon icon = new ImageIcon(getClass().getResource(String.format("%s/%s", IMG_SOURCE, "target-icon-50px.png")));

            setName("panel.planoDeEnsino.objetivoEspecifico");
            setTitlePanel("Dados de Objetivos Específicos", icon);
            setController(ControllerFactory.createObjetivoController());

            setFieldsPanel(new PlanoDeEnsinoObjetivoEspecificoFields(this.planoDeEnsino));
            enableTablePanel();
            showPanelInCard(CARD_LIST);
        } catch (Exception ex) {
            showErrorMessage(ex);
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
        col0.setCellRenderer(new TextAreaCellRenderer());

        EnumSet enumSet = EnumSet.of(AcoesBotoes.DEL, AcoesBotoes.EDIT);

        TableColumn col1 = tcm.getColumn(1);
        col1.setCellRenderer(new ButtonsRenderer(null, enumSet));
        col1.setCellEditor(new ButtonsEditor(table, null, enumSet));
    }

    @Override
    public void reloadTableData() {
        ObjetivoController col = (ObjetivoController) getController();
        setTableModel(new ObjetivoTableModel(col.listar(planoDeEnsino)));
        resizeTableColumns();
    }

    @Override
    public Object getSelectedObject() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addFiltersFields() {
        btImport = createButton(new ActionHandler(AcoesBotoes.IMPORT));
        btImport.setText("Importar Objetivos da U.C.");
        btImport.setToolTipText("Importar Objetivos da Unidade Curricular");
        
        JPanel panel = getFilterPanel();
        panel.setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        
        int col = 0, row = 0;
        GridLayoutHelper.setRight(c, col++, row);
        GridLayoutHelper.set(c, 0, row, 1, 1, GridBagConstraints.LINE_END);
        panel.add(btImport, c);
    }

    @Override
    public void onImportAction(ActionEvent e) {
        UnidadeCurricular uc = planoDeEnsino.getUnidadeCurricular();
        if (uc.getObjetivos().isEmpty()) {
            showWarningMessage("A U.C. vinculada ao plano de ensino não tem\n"
                    + "não tem objetivos cadastrados");
            return;
        }
        try {
            List<ObjetivoUC> lista = uc.getObjetivos();
            for (ObjetivoUC ouc : lista) {
                Objetivo obj = ObjetivoFactory.getInstance().createObject(
                        new ObjetivoId(null, planoDeEnsino),
                        ouc.getDescricao(), ouc);
                getController().salvar(obj);
                planoDeEnsino.addObjetivo(obj);
            }
            reloadTableData();
        } catch (Exception ex) {
            showErrorMessage(ex);
            ex.printStackTrace();
        }
    }

}
