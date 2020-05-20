/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.curso;

import ensino.configuracoes.controller.CursoController;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.view.models.CursoTableModel;
import ensino.defaults.DefaultFormPanel;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import ensino.configuracoes.view.panels.filters.CampusFilter;
import ensino.configuracoes.view.renderer.CursoCellRenderer;
import ensino.patterns.factory.ControllerFactory;
import java.awt.Component;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

/**
 *
 * @author nicho
 */
public class CursoPanel extends DefaultFormPanel {

    private Campus selectedCampus;
    private CampusFilter campusFilter;
    private Curso selectedCurso;

    public CursoPanel(Component frame, Campus campus) {
        super(frame);
        this.selectedCampus = campus;

        try {
            setName("panel.curso");
            setTitlePanel("Dados de Cursos");
            // para capturar os dados do curso, usa-se a estrutura do campus
            setController(ControllerFactory.createCursoController());

            enableTablePanel();
            setFieldsPanel(new CursoFieldsPanel());
            showPanelInCard(CARD_LIST);
        } catch (Exception ex) {
            Logger.getLogger(CursoPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CursoPanel(Component frame) {
        this(frame, null);
    }

    public Campus getCampus() {
        return selectedCampus;
    }

    /**
     * Recupera o curso selecionado na tabela quando o botao de selecao de curso
     * é criado
     *
     * @return
     */
    @Override
    public Curso getSelectedObject() {
        return selectedCurso;
    }

    /**
     * Cria um botão para selecionar um curso na tabela e fecha a janela do
     * curso
     */
    @Override
    public void createSelectButton() {
        JButton button = createButton("selection-button-50px.png", "Selecionar", 1);
        button.addActionListener((ActionEvent e) -> {
            JTable t = getTable();
            if (t.getRowCount() > 0) {
                int row = t.getSelectedRow();
                CursoTableModel model = (CursoTableModel) t.getModel();
                selectedCurso = (Curso) model.getRow(row);
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
        JTable table = getTable();
        table.getColumnModel().getColumn(0).setCellRenderer(new CursoCellRenderer());
    }

    @Override
    public void reloadTableData() {
        try {
            setController(ControllerFactory.createCursoController());

            selectedCampus = campusFilter.getSelectedCampus();

            CursoController col = (CursoController) getController();
            List<Curso> list;
            if (selectedCampus == null) {
                list = col.listar();
            } else {
                // recupera a lista de cursos pelo campus
                list = col.listar(selectedCampus);
            }
            setTableModel(new CursoTableModel(list));
            resizeTableColumns();
        } catch (Exception ex) {
            Logger.getLogger(CursoPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
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
