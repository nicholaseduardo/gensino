/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.unidadeCurricular;

import ensino.components.GenJComboBox;
import ensino.components.GenJLabel;
import ensino.configuracoes.controller.UnidadeCurricularController;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.configuracoes.view.models.CampusComboBoxModel;
import ensino.configuracoes.view.models.CursoComboBoxListModel;
import ensino.configuracoes.view.models.CursoListModel;
import ensino.configuracoes.view.models.UnidadeCurricularTableModel;
import ensino.configuracoes.view.panels.CursoPanel;
import ensino.configuracoes.view.renderer.UnidadeCurricularCellRenderer;
import ensino.defaults.DefaultFormPanel;
import ensino.helpers.GridLayoutHelper;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class UnidadeCurricularPanel extends DefaultFormPanel {

    private GenJComboBox comboCampus;
    private GenJComboBox comboCurso;
    private JButton btSearch;
    private JButton btClear;
    
    private Curso selectedCurso;
    private UnidadeCurricular selectedUnidadeCurricular;

    public UnidadeCurricularPanel(Component frame) {
        this(frame, null);
    }
    
    public UnidadeCurricularPanel(Component frame, Curso curso) {
        super(frame);
        this.selectedCurso = curso;
        try {
            super.setName("panel.unidadeCurricular");
            super.setTitlePanel("Dados da Unidade Curricular");
            // para capturar os dados do curso, usa-se a estrutura do campus
            super.setController(new UnidadeCurricularController());

            super.enableTablePanel();
            super.setFieldsPanel(new UnidadeCurricularFieldsPanel());
            super.showPanelInCard(CARD_LIST);
        } catch (IOException | ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(CursoPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public UnidadeCurricular getSelectedUnidadeCurricular() {
        return this.selectedUnidadeCurricular;
    }

    @Override
    public Curso getSelectedObject() {
        return selectedCurso;
    }

    public void setSelectedCurso(Curso selectedCurso) {
        this.selectedCurso = selectedCurso;
        reloadTableData();
    }
    /**
     * Cria um botão para selecionar um curso na tabela e fecha 
     * a janela do curso
     */
    @Override
    public void createSelectButton() {
        JButton button = createButton("selection-button-50px.png", "Selecionar", 1);
        button.addActionListener((ActionEvent e) -> {
            JTable t = getTable();
            if (t.getRowCount() > 0) {
                int row = t.getSelectedRow();
                UnidadeCurricularTableModel model = (UnidadeCurricularTableModel) t.getModel();
                selectedUnidadeCurricular = (UnidadeCurricular) model.getRow(row);
                JDialog dialog = (JDialog)getFrame();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(getParent(), 
                        "Não existem dados a serem selecionados.\nFavor, cadastrar um dado primeiro.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        addButtonToToolBar(button, true);
    }
    
    private void resizeTableColumns() {
        JTable table = getTable();
        TableColumnModel tcm = table.getColumnModel();
        TableColumn tcNome = tcm.getColumn(0);
        tcNome.setMinWidth(50);
        tcNome.setCellRenderer(new UnidadeCurricularCellRenderer());
    }

    @Override
    public void reloadTableData() {
        Campus campus = (Campus) comboCampus.getSelectedItem();
        selectedCurso = (Curso) comboCurso.getSelectedItem();
        UnidadeCurricularController col = (UnidadeCurricularController) getController();
        UnidadeCurricularTableModel model;
        List<UnidadeCurricular> list = new ArrayList<>();
        if (campus != null && selectedCurso != null) {
            list = col.listar(selectedCurso);
        } else if (campus != null) {
            list = col.listar(campus.getId());
        } else {
            list = col.listar();
        }
        model = new UnidadeCurricularTableModel(list);
        setTableModel(model);
        resizeTableColumns();
    }

    @Override
    public void onSearchButton(ActionEvent e) {
        reloadTableData();
    }

    @Override
    public void onClearButton(ActionEvent e) {
        comboCampus.setSelectedItem(null);
        comboCurso.setSelectedItem(null);
    }

    @Override
    public void addFiltersFields() {
        boolean activeFilters = this.selectedCurso == null;
        JPanel panel = getFilterPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        GenJLabel lblCampus = new GenJLabel("Campus: ", JLabel.TRAILING);
        GridLayoutHelper.set(c, 0, 0);
        panel.add(lblCampus, c);
        comboCampus = new GenJComboBox(new CampusComboBoxModel());
        if (!activeFilters) {
            comboCampus.setSelectedItem(this.selectedCurso.getCampus());
        }
        comboCampus.setEnabled(activeFilters);
        comboCampus.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Campus campus = (Campus) e.getItem();
                CursoListModel cursoModel = (CursoListModel) comboCurso.getModel();
                cursoModel.setCampus(campus);
                cursoModel.refresh();
                comboCurso.setSelectedItem(null);
            }
        });
        GridLayoutHelper.set(c, 1, 0, 3, 1, GridBagConstraints.LINE_START);
        panel.add(comboCampus, c);

        GenJLabel lblCurso = new GenJLabel("Curso:", JLabel.TRAILING);
        GridLayoutHelper.setRight(c, 0, 1);
        panel.add(lblCurso, c);
        comboCurso = new GenJComboBox(new CursoComboBoxListModel());
        comboCurso.setEnabled(activeFilters);
        comboCurso.setSelectedItem(this.selectedCurso);
        GridLayoutHelper.set(c, 1, 1);
        panel.add(comboCurso, c);

        btSearch = createButton("search", "Buscar", 0);
        btSearch.setEnabled(activeFilters);
        GridLayoutHelper.set(c, 2, 1);
        panel.add(btSearch, c);
        btClear = createButton("clear", "Limpar filtro", 0);
        btClear.setEnabled(activeFilters);
        GridLayoutHelper.set(c, 3, 1);
        panel.add(btClear, c);
    }

}
