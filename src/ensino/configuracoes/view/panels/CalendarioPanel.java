/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels;

import ensino.configuracoes.controller.CalendarioController;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.view.models.CalendarioTableModel;
import ensino.defaults.DefaultFormPanel;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import ensino.configuracoes.view.panels.calendario.CalendarioFields;
import ensino.configuracoes.view.panels.filters.CampusFilter;
import ensino.configuracoes.view.renderer.CalendarioCellRenderer;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class CalendarioPanel extends DefaultFormPanel {

    private Campus selectedCampus;
    private CampusFilter campusFilter;

    private Calendario selectedCalendario;

    public CalendarioPanel(Component frame) {
        this(frame, null);
    }

    public CalendarioPanel(Component frame, Campus campus) {
        super(frame);
        this.selectedCampus = campus;
        try {
            setName("panel.calendario");
            setTitlePanel("Dados do Calendario");
            CalendarioController calendarioController = new CalendarioController();
            setController(calendarioController);

            enableTablePanel();
            reloadTableData();
            setFieldsPanel(new CalendarioFields(campus));

            showPanelInCard(CARD_LIST);
        } catch (IOException | ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(CalendarioPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Campus getCampus() {
        return selectedCampus;
    }

    /**
     * Recupera o curso selecionado na tabela quando o botao de selecao de
     * calendário é criado
     *
     * @return
     */
    public Calendario getSelectedObject() {
        return selectedCalendario;
    }

    /**
     * Cria um botão para selecionar um curso na tabela e fecha a janela do
     * calendario
     */
    @Override
    public void createSelectButton() {
        JButton button = createButton("selection-button-50px.png", "Selecionar", 1);
        button.addActionListener((ActionEvent e) -> {
            JTable t = getTable();
            if (t.getRowCount() > 0) {
                int row = t.getSelectedRow();
                CalendarioTableModel model = (CalendarioTableModel) t.getModel();
                selectedCalendario = (Calendario) model.getRow(row);
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

    public void setCampus(Campus campus) {
        this.selectedCampus = campus;
        reloadTableData();
    }

    private void resizeTableColumns() {
        javax.swing.JTable table = getTable();
        javax.swing.table.TableColumnModel tcm = table.getColumnModel();
        TableColumn tcNome = tcm.getColumn(0);
        tcNome.setMinWidth(50);
        tcNome.setCellRenderer(new CalendarioCellRenderer());
    }

    @Override
    public void reloadTableData() {
        selectedCampus = campusFilter.getSelectedCampus();
        List<Calendario> list;
        CalendarioController col = (CalendarioController) getController();
        if (selectedCampus == null) {
            list = col.listar();
        } else {
            list = col.listar(selectedCampus.getId());
        }
        setTableModel(new CalendarioTableModel(list));
        resizeTableColumns();
    }

    @Override
    public void onSearchButton(ActionEvent e) {

    }

    @Override
    public void addFiltersFields() {
        campusFilter = new CampusFilter(this, selectedCampus);
        JPanel panel = getFilterPanel();
        panel.add(campusFilter);
    }

}
