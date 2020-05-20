/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.permanenciaEstudantil;

import ensino.defaults.DefaultFormPanel;
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.controller.PermanenciaEstudantilController;
import ensino.planejamento.model.PermanenciaEstudantil;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.models.PermanenciaEstudantilTableModel;
import ensino.planejamento.view.renderer.PermanenciaEstudantilCellRenderer;
import ensino.reports.ChartsFactory;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author nicho
 */
public class PermanenciaEstudantilPanel extends DefaultFormPanel {

    private JButton btSearch;
    
    private PlanoDeEnsino selectedPlanoDeEnsino;
    private PermanenciaEstudantil selectedPermanenciaEstudantil;

    public PermanenciaEstudantilPanel(Component frame) {
        this(frame, null);
    }
    
    public PermanenciaEstudantilPanel(Component frame, PlanoDeEnsino planoDeEnsino) {
        super(frame);

        backColor = ChartsFactory.ligthGreen;
        foreColor = ChartsFactory.darkGreen;
        setBackground(backColor);
        
        this.selectedPlanoDeEnsino = planoDeEnsino;
        try {
            super.setName("panel.permanenciaEstudantil");
            super.setTitlePanel("Dados da Permanencia Estudantil");
            // para capturar os dados do planoDeEnsino, usa-se a estrutura do campus
            super.setController(ControllerFactory.createPermanenciaEstudantilController());

            super.enableTablePanel();
            super.setFieldsPanel(new PermanenciaEstudantilFieldsPanel(selectedPlanoDeEnsino));
            super.showPanelInCard(CARD_LIST);
        } catch (Exception ex) {
            Logger.getLogger(PermanenciaEstudantilPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public PermanenciaEstudantil getSelectedObject() {
        return selectedPermanenciaEstudantil;
    }

    public void setSelectedPlanoDeEnsino(PlanoDeEnsino selectedPlanoDeEnsino) {
        this.selectedPlanoDeEnsino = selectedPlanoDeEnsino;
        reloadTableData();
    }
    /**
     * Cria um botão para selecionar um planoDeEnsino na tabela e fecha 
     * a janela do planoDeEnsino
     */
    @Override
    public void createSelectButton() {
        JButton button = createButton("selection-button-50px.png", "Selecionar", 1);
        button.addActionListener((ActionEvent e) -> {
            JTable t = getTable();
            if (t.getRowCount() > 0) {
                int row = t.getSelectedRow();
                PermanenciaEstudantilTableModel model = (PermanenciaEstudantilTableModel) t.getModel();
                selectedPermanenciaEstudantil = (PermanenciaEstudantil) model.getRow(row);
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
        tcNome.setCellRenderer(new PermanenciaEstudantilCellRenderer());;
    }

    @Override
    public void reloadTableData() {
        try {
            setController(ControllerFactory.createPermanenciaEstudantilController());
            
            PermanenciaEstudantilController col = (PermanenciaEstudantilController) getController();
            PermanenciaEstudantilTableModel model;
            List<PermanenciaEstudantil> list;
            list = col.listar(selectedPlanoDeEnsino);
            
            model = new PermanenciaEstudantilTableModel(list);
            setTableModel(model);
            resizeTableColumns();
        } catch (Exception ex) {
            Logger.getLogger(PermanenciaEstudantilPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onSearchButton(ActionEvent e) {
        reloadTableData();
    }

    @Override
    public void onClearButton(ActionEvent e) {
        
    }

    @Override
    public void addFiltersFields() {
        
    }    

//    public static void main(String args[]) {
//        try {
//            PlanoDeEnsino plano = ControllerFactory.createPlanoDeEnsinoController().buscarPorId(13);
//            
//            JFrame f = new JFrame();
//            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//            PermanenciaEstudantilPanel p = new PermanenciaEstudantilPanel(f, plano);
//
//            f.getContentPane().add(p);
//            f.pack();
//            f.setLocationRelativeTo(null);
//            f.setVisible(true);
//        } catch (Exception ex) {
//            Logger.getLogger(PermanenciaEstudantilPanel.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

}
