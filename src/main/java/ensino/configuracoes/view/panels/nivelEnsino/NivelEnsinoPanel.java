/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.nivelEnsino;

import ensino.defaults.DefaultFormPanel;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import ensino.configuracoes.model.NivelEnsino;
import ensino.configuracoes.view.models.NivelEnsinoTableModel;
import ensino.configuracoes.view.renderer.BaseObjectCellRenderer;
import ensino.patterns.factory.ControllerFactory;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

/**
 * s
 *
 * @author nicho
 */
public class NivelEnsinoPanel extends DefaultFormPanel {

    private NivelEnsino selectedNivelEnsino;

    public NivelEnsinoPanel(Component iframe) {
        super(iframe);
        try {
            super.setName("panel.tecnica");
            super.setTitlePanel("Dados do Nivel de Ensino");
            super.setController(ControllerFactory.createNivelEnsinoController());

            super.enableTablePanel();

            super.setFieldsPanel(new NivelEnsinoFields());
            super.showPanelInCard(CARD_LIST);
            getTable().setPreferredSize(new Dimension(640, 480));
        } catch (Exception ex) {
            Logger.getLogger(NivelEnsinoPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void resizeTableColumns() {
        JTable table = getTable();
        TableColumnModel tcm = table.getColumnModel();
        tcm.getColumn(0).setCellRenderer(new BaseObjectCellRenderer());
    }

    @Override
    public void onSearchButton(ActionEvent e) {

    }

    @Override
    public void addFiltersFields() {

    }

    @Override
    public void reloadTableData() {
        setTableModel(new NivelEnsinoTableModel(getController().listar()));
        resizeTableColumns();
    }

    @Override
    public void createSelectButton() {
        JButton button = createButton("selection-button-50px.png", "Selecionar", 1);
        button.addActionListener((ActionEvent e) -> {
            JTable t = getTable();
            if (t.getRowCount() > 0) {
                int row = t.getSelectedRow();
                NivelEnsinoTableModel model = (NivelEnsinoTableModel) t.getModel();
                selectedNivelEnsino = (NivelEnsino) model.getRow(row);
                JDialog dialog = (JDialog) getFrame();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(getParent(),
                        "Não existem dados a serem selecionados.\nFavor, cadastrar um dado primeiro.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        addButtonToToolBar(button, true);
    }

    @Override
    public Object getSelectedObject() {
        return selectedNivelEnsino;
    }
}
