/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.controller.CampusController;
import ensino.configuracoes.model.Campus;
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
public class CampusComboBoxModel extends AbstractListModel implements ComboBoxModel {
    private CampusController campusCol;
    private List<Campus> list;
    
    private Campus selection;
    
    public CampusComboBoxModel() {
        initComponents();
    }
    
    private void initComponents() {
        try {
            campusCol = ControllerFactory.createCampusController();
            refresh();
        } catch (Exception ex) {
            Logger.getLogger(CampusComboBoxModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void refresh() {
        int index = 0;
        list = (List<Campus>) campusCol.listar();
        
        if (!list.isEmpty()) {
            list.sort(Comparator.comparing(Campus::getNome));
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
        selection = (Campus)anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selection;
    }
}
