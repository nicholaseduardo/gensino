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
import java.util.List;
import ensino.configuracoes.view.panels.calendario.CalendarioFields;
import ensino.configuracoes.view.panels.calendario.atividade.CalendarioAtividadesPanel;
import ensino.configuracoes.view.panels.calendario.periodosLetivos.CalendarioPeriodosLetivosPanel;
import ensino.configuracoes.view.renderer.CalendarioCellRenderer;
import ensino.defaults.DefaultCleanFormPanel;
import ensino.patterns.factory.ControllerFactory;
import ensino.util.types.AcoesBotoes;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.EnumSet;
import java.util.Iterator;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author nicho
 */
public class CalendarioPanel extends DefaultCleanFormPanel {

    private Campus selectedCampus;

    private Calendario selectedCalendario;
    
    private EnumSet enumSet;

    public CalendarioPanel(Component frame) {
        this(frame, null);
    }

    public CalendarioPanel(Component frame, Campus campus) {
        super(frame);
        this.selectedCampus = campus;
        initComponents();
    }
    
    private void initComponents() {
        try {
            setName("panel.calendario");
            setTitlePanel("Dados do Calendario");
            
            setController(ControllerFactory.createCalendarioController());

            enumSet = EnumSet.of(AcoesBotoes.EDIT, AcoesBotoes.ATIVIDADE, 
                    AcoesBotoes.PERIODO_LETIVO, AcoesBotoes.DEL);
            
            enableTablePanel();
            setFieldsPanel(new CalendarioFields(this.selectedCampus));

            showPanelInCard(CARD_LIST);
            setPreferredSize(new Dimension(800,600));
        } catch (Exception ex) {
            showErrorMessage(ex);
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
    @Override
    public Calendario getSelectedObject() {
        return selectedCalendario;
    }

    /**
     * Cria um botão para selecionar um calendario na tabela e fecha a janela do
     * calendario
     */
    @Override
    public void createSelectButton() {
        enumSet = EnumSet.of(AcoesBotoes.EDIT, AcoesBotoes.SELECTION, AcoesBotoes.DEL);
        reloadTableData();
    }

    public void setCampus(Campus campus) {
        this.selectedCampus = campus;
        reloadTableData();
    }

    private void resizeTableColumns() {
        JTable table = getTable();
        TableColumnModel tcm = table.getColumnModel();
        TableColumn col0 = tcm.getColumn(0);
        col0.setCellRenderer(new CalendarioCellRenderer());
        col0.setMinWidth(300);

        TableColumn col1 = tcm.getColumn(1);
        col1.setCellRenderer(new ButtonsRenderer(null, enumSet));
        col1.setCellEditor(new ButtonsEditor(table, null, enumSet));
        
        int buttonWidth = 0;
        Iterator it = enumSet.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof AcoesBotoes) {
                buttonWidth += 120;
            }
        }
        col1.setMaxWidth(buttonWidth);
        col1.setMinWidth(buttonWidth);
        table.repaint();
    }

    @Override
    public void reloadTableData() {
        try {
            setController(ControllerFactory.createCalendarioController());
            selectedCampus = ControllerFactory.getCampusVigente();
            
            CalendarioController col = (CalendarioController) getController();
            List<Calendario> list = col.listar(selectedCampus);
            setTableModel(new CalendarioTableModel(list));
            resizeTableColumns();
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    @Override
    public void addFiltersFields() {
        
    }
    
    @Override
    public void onDefaultButton(ActionEvent e, Object o) {
        if (o != null && o instanceof JTable) {
            Calendario calendario = null;
            Object obj = getObjectFromTable((JTable) o);
            calendario = (Calendario) obj;

            JDialog dialog = new JDialog();

            String command = e.getActionCommand();

            if (command.equals(AcoesBotoes.ATIVIDADE.toString())) {
                CalendarioAtividadesPanel p = new CalendarioAtividadesPanel(dialog, calendario);
                showDialog(dialog, p);
            } else if (command.equals(AcoesBotoes.PERIODO_LETIVO.toString())) {
                CalendarioPeriodosLetivosPanel p = new CalendarioPeriodosLetivosPanel(dialog, calendario);
                showDialog(dialog, p);
            }
            reloadTableData();
        }
    }

}
