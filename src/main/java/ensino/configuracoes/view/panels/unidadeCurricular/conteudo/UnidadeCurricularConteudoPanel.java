/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.unidadeCurricular.conteudo;

import ensino.components.GenJButton;
import ensino.components.GenJLabel;
import ensino.components.GenJTextField;
import ensino.configuracoes.controller.ConteudoController;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.configuracoes.view.models.ConteudoTableModel;
import ensino.configuracoes.view.renderer.ConteudoCellRenderer;
import ensino.defaults.DefaultCleanFormPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.ControllerFactory;
import ensino.util.types.AcoesBotoes;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.EnumSet;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

/**
 *
 * @author santos
 */
public class UnidadeCurricularConteudoPanel extends DefaultCleanFormPanel {

    private UnidadeCurricular unidadeCurricular;
    private GenJTextField txtDescricao;
    private GenJButton btSearch;
    
    public UnidadeCurricularConteudoPanel(Component frame) {
        this(frame, null);
    }
    
    public UnidadeCurricularConteudoPanel(Component frame, UnidadeCurricular unidadeCurricular) {
        super(frame);
        this.unidadeCurricular = unidadeCurricular;
        initComponents();
    }

    private void initComponents() {
        try {
            setName("panel.unidadeCurricular.conteudo");
            setTitlePanel("Dados do Conteúdo da U.C.");
            setController(ControllerFactory.createConteudoController());

            enableTablePanel();
            setFieldsPanel(new UnidadeCurricularConteudoFields(this.unidadeCurricular));
            showPanelInCard(CARD_LIST);
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    private void resizeTableColumns() {
        JTable table = getTable();

        TableColumn tc0 = table.getColumnModel().getColumn(0);
        tc0.setCellRenderer(new ConteudoCellRenderer());
        tc0.setMinWidth(300);

        EnumSet enumSet = EnumSet.of(AcoesBotoes.EDIT, AcoesBotoes.DEL);

        TableColumn col1 = table.getColumnModel().getColumn(1);
        col1.setCellRenderer(new ButtonsRenderer(null, enumSet));
        col1.setCellEditor(new ButtonsEditor(table, null, enumSet));
    }


    @Override
    public void reloadTableData() {
        ConteudoController col = (ConteudoController) getController();
        setTableModel(new ConteudoTableModel(col.listar(unidadeCurricular, 
                txtDescricao.getText())));
        resizeTableColumns();
    }

    @Override
    public Object getSelectedObject() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addFiltersFields() {
        GenJLabel lblDescricao = new GenJLabel("Descrição: ", JLabel.TRAILING);
        txtDescricao = new GenJTextField(30, false);

        btSearch = createButton(new ActionHandler(AcoesBotoes.SEARCH));
        
        JPanel panel = createPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        int col = 0, row = 0;
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblDescricao, c);
        GridLayoutHelper.set(c, col, row++);
        panel.add(txtDescricao, c);

        col = 0;
        GridLayoutHelper.set(c, col, row, 2, 1, GridBagConstraints.LINE_END);
        panel.add(btSearch, c);

        JPanel filterPanel = getFilterPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(panel);
    }
    
}
