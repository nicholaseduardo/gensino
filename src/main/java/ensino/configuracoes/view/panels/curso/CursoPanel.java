/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.curso;

import ensino.components.GenJButton;
import ensino.components.GenJLabel;
import ensino.components.GenJTextField;
import ensino.configuracoes.controller.CursoController;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.view.models.CursoTableModel;
import ensino.configuracoes.view.panels.turma.TurmaPanel;
import ensino.configuracoes.view.panels.unidadeCurricular.UnidadeCurricularPanel;
import ensino.configuracoes.view.renderer.CursoCellRenderer;
import ensino.defaults.DefaultCleanFormPanel;
import ensino.patterns.factory.ControllerFactory;
import ensino.util.types.AcoesBotoes;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.EnumSet;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author nicho
 */
public class CursoPanel extends DefaultCleanFormPanel {

    private Campus selectedCampus;
    private EnumSet enumSet;
    
    private GenJTextField txtNome;
    private GenJButton btSearch;

    public CursoPanel(Component frame) {
        this(frame, ControllerFactory.getCampusVigente());
    }

    public CursoPanel(Component frame, Campus campus) {
        super(frame);
        this.selectedCampus = campus;

        initComponents();
    }

    private void initComponents() {
        try {
            setName("panel.curso");
            setTitlePanel("Dados de Cursos");
            // para capturar os dados do curso, usa-se a estrutura do campus
            setController(ControllerFactory.createCursoController());

            enumSet = EnumSet.of(AcoesBotoes.EDIT, AcoesBotoes.UC,
                AcoesBotoes.TURMA, AcoesBotoes.DEL);
            
            enableTablePanel();
            setFieldsPanel(new CursoFieldsPanel());
            showPanelInCard(CARD_LIST);
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    public Campus getCampus() {
        return selectedCampus;
    }

    public void setCampus(Campus campus) {
        this.selectedCampus = campus;
        reloadTableData();
    }

    private void resizeTableColumns() {
        JTable table = getTable();
        TableColumnModel tcm = table.getColumnModel();
        TableColumn col0 = tcm.getColumn(0);
        col0.setCellRenderer(new CursoCellRenderer());

        TableColumn col1 = tcm.getColumn(1);
        col1.setCellRenderer(new ButtonsRenderer(null, enumSet));
        col1.setCellEditor(new ButtonsEditor(table, null, enumSet));

        table.repaint();
    }

    @Override
    public void reloadTableData() {
        try {
            CursoController col = (CursoController) getController();
            setTableModel(new CursoTableModel(col.listar(selectedCampus,
                    txtNome.getText())));
            resizeTableColumns();
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    /**
     * Cria um botão para selecionar um curso na tabela e fecha a janela do
     * curso
     */
    @Override
    public void createSelectButton() {
        enumSet = EnumSet.of(AcoesBotoes.EDIT, AcoesBotoes.SELECTION);
        reloadTableData();
    }

    @Override
    public void addFiltersFields() {
        GenJLabel lblNome = new GenJLabel("Nome: ");
        txtNome = new GenJTextField(30, false);
        
        btSearch = createButton(new ActionHandler(AcoesBotoes.SEARCH));
        
        JPanel panel = getFilterPanel();
        panel.add(lblNome);
        panel.add(txtNome);
        panel.add(btSearch);
    }

    @Override
    public void onDefaultButton(ActionEvent e, Object o) {
        if (o != null && o instanceof JTable) {
            Object obj = getObjectFromTable((JTable) o);
            if (obj instanceof Curso) {
                Curso curso = (Curso) obj;

                JDialog dialog = new JDialog();
                DefaultCleanFormPanel panel = null;

                String actionCommant = e.getActionCommand();
                if (actionCommant.equals(AcoesBotoes.TURMA.toString())) {
                    panel = new TurmaPanel(null, curso);
                } else if (actionCommant.equals(AcoesBotoes.UC.toString())) {
                    panel = new UnidadeCurricularPanel(null, curso);
                }
                
                if (panel != null) {
                    showDialog(dialog, panel);
                    reloadTableData();
                }

            }
        }
    }
}
