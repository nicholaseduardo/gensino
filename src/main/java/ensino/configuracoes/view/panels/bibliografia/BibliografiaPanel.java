/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.bibliografia;

import ensino.components.GenJLabel;
import ensino.components.GenJTextField;
import ensino.configuracoes.controller.BibliografiaController;
import ensino.configuracoes.model.Bibliografia;
import ensino.configuracoes.view.models.BibliografiaTableModel;
import ensino.configuracoes.view.renderer.BibliografiaCellRenderer;
import ensino.defaults.DefaultCleanFormPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.ControllerFactory;
import ensino.util.types.AcoesBotoes;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.EnumSet;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author nicho
 */
public class BibliografiaPanel extends DefaultCleanFormPanel {

    private GenJTextField txtTitulo;
    private GenJTextField txtAutor;
    private EnumSet enumSet;

    private JButton btSearch;

    public BibliografiaPanel(Component frame) {
        super(frame);
        initComponents();
    }

    private void initComponents() {
        try {
            setName("panel.bibliografia");
            setTitlePanel("Dados de Bibliografia");

            setController(ControllerFactory.createBibliografiaController());

            enumSet = EnumSet.of(AcoesBotoes.EDIT, AcoesBotoes.DEL);

            super.enableTablePanel();
            setFieldsPanel(new BibliografiaFields());
            showPanelInCard(CARD_LIST);
            txtAutor.requestFocusInWindow();
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    /**
     * Cria um botão para selecionar um curso na tabela e fecha a janela da
     * bibliografia
     */
    @Override
    public void createSelectButton() {
        enumSet = EnumSet.of(AcoesBotoes.EDIT, AcoesBotoes.SELECTION);
        reloadTableData();
    }

    private void resizeTableColumns() {
        JTable table = getTable();
        TableColumnModel tcm = table.getColumnModel();
        TableColumn col0 = tcm.getColumn(0);
        col0.setCellRenderer(new BibliografiaCellRenderer());

        TableColumn col1 = tcm.getColumn(1);
        col1.setCellRenderer(new ButtonsRenderer(null, enumSet));
        col1.setCellEditor(new ButtonsEditor(table, null, enumSet));

        table.repaint();
    }

    @Override
    public void reloadTableData() {
        try {
            BibliografiaController col = (BibliografiaController) getController();
            
            String sAutor = txtAutor.getText();
            String sTitulo = txtTitulo.getText();
            List<Bibliografia> list;
            if (!"".equals(sTitulo)) {
                list = col.listarPorTitulo(sTitulo);
            } else if (!"".equals(sAutor)) {
                list = col.listarPorAutor(sAutor);
            } else {
                list = col.listar();
            }
            setTableModel(new BibliografiaTableModel(list));
            resizeTableColumns();
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    @Override
    public void addFiltersFields() {
        JPanel panel = getFilterPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        GenJLabel lblTitulo = new GenJLabel("Título: ", JLabel.TRAILING);
        GridLayoutHelper.setRight(c, 0, 0);
        panel.add(lblTitulo, c);

        txtTitulo = new GenJTextField(30, true);
        lblTitulo.setLabelFor(txtTitulo);
        GridLayoutHelper.set(c, 1, 0);
        panel.add(txtTitulo, c);

        GenJLabel lblAutor = new GenJLabel("Autor: ", JLabel.TRAILING);
        GridLayoutHelper.setRight(c, 0, 1);
        panel.add(lblAutor, c);

        txtAutor = new GenJTextField(30, true);
        lblAutor.setLabelFor(txtAutor);
        GridLayoutHelper.set(c, 1, 1);
        panel.add(txtAutor, c);

        btSearch = createButton(new ActionHandler(AcoesBotoes.SEARCH));
        GridLayoutHelper.set(c, 0, 2, 2, 1, GridBagConstraints.LINE_END);
        panel.add(btSearch, c);
    }

}
