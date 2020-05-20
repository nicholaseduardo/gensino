/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.controller.LegendaController;
import ensino.configuracoes.model.Legenda;
import ensino.patterns.factory.ControllerFactory;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 *
 * @author nicho
 */
public class LegendaComboBoxModel extends AbstractListModel implements ComboBoxModel {
    private LegendaController legendaCol;
    private List<Legenda> list;
    
    private Legenda selection;
    
    public LegendaComboBoxModel() {
        initComponents();
    }
    
    private void initComponents() {
        try {
            legendaCol = ControllerFactory.createLegendaController();
            refresh();
        } catch (Exception ex) {
            Logger.getLogger(LegendaComboBoxModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void refresh() {
        int index = 0;
        list = (List<Legenda>) legendaCol.listar();
        
        if (!list.isEmpty()) {
            list.sort(Comparator.comparing(Legenda::getNome));
            index = list.size() - 1;
        }
        setSelectedItem(null);
        fireIntervalAdded(this, 0, index);
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public Object getElementAt(int index) {
        return list.get(index);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selection = (Legenda)anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selection;
    }
}
