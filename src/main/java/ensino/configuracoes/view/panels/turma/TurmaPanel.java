/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.turma;

import ensino.components.GenJPanel;
import ensino.configuracoes.controller.TurmaController;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.Turma;
import ensino.configuracoes.view.models.TurmaTableModel;
import ensino.configuracoes.view.renderer.TurmaCellRenderer;
import ensino.defaults.DefaultCleanFormPanel;
import ensino.patterns.factory.ControllerFactory;
import ensino.util.types.AcoesBotoes;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.EnumSet;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author nicho
 */
public class TurmaPanel extends DefaultCleanFormPanel {

    private Curso selectedCurso;
    private Turma selectedTurma;

    public TurmaPanel(Component frame) {
        this(frame, null);
    }

    public TurmaPanel(Component frame, Curso curso) {
        super(frame);
        this.selectedCurso = curso;
        initComponents();
    }

    private void initComponents() {
        try {
            super.setName("panel.turma");
            super.setTitlePanel("Dados da Turma");
            // para capturar os dados do curso, usa-se a estrutura do campus
            super.setController(ControllerFactory.createTurmaController());

            super.enableTablePanel();
            super.setFieldsPanel(new TurmaFields(this.selectedCurso, null));
            super.showPanelInCard(CARD_LIST);
            super.disableCloseButton();
            
        } catch (Exception ex) {
            showErrorMessage(ex);
            ex.printStackTrace();
        }
    }

    @Override
    public Turma getSelectedObject() {
        return selectedTurma;
    }

    public void setSelectedCurso(Curso selectedCurso) {
        this.selectedCurso = selectedCurso;
        reloadTableData();
    }

    /**
     * Cria um botão para selecionar um curso na tabela e fecha a janela do
     * curso
     */
    @Override
    public void createSelectButton() {
//        JButton button = createButton("selection-button-50px.png", "Selecionar", 1);
//        button.addActionListener((ActionEvent e) -> {
//            JTable t = getTable();
//            if (t.getRowCount() > 0) {
//                int row = t.getSelectedRow();
//                TurmaTableModel model = (TurmaTableModel) t.getModel();
//                selectedTurma = (Turma) model.getRow(row);
//                JDialog dialog = (JDialog) getFrame();
//                dialog.dispose();
//            } else {
//                JOptionPane.showMessageDialog(getParent(),
//                        "Não existem dados a serem selecionados.\nFavor, cadastrar um dado primeiro.",
//                        "Aviso", JOptionPane.WARNING_MESSAGE);
//            }
//        });
//        addButtonToToolBar(button, true);
    }

    private void resizeTableColumns() {
        JTable table = getTable();
        TableColumnModel tcm = table.getColumnModel();
        TableColumn col0 = tcm.getColumn(0);
        col0.setCellRenderer(new TurmaCellRenderer());

        EnumSet enumSet = EnumSet.of(AcoesBotoes.EDIT, AcoesBotoes.ESTUD, AcoesBotoes.DELETE);

        TableColumn col1 = tcm.getColumn(1);
        col1.setCellRenderer(new ButtonsRenderer(null, enumSet));
        col1.setCellEditor(new GenJPanel.ButtonsEditor(table, null, enumSet));
        table.repaint();
    }

    @Override
    public void reloadTableData() {
        try {
            
            TurmaController col = (TurmaController) getController();
            setTableModel(new TurmaTableModel(col.listar(selectedCurso)));
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
            Object obj = getObjectFromTable((JTable) o);
            if (obj instanceof Turma) {
                Turma turma = (Turma) obj;
                
                JDialog dialog = new JDialog();
                TurmaPanelEstudante panel = new TurmaPanelEstudante(dialog, turma);
                showDialog(dialog, panel);
            }
        }
    }

}
