/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.model.SemanaLetiva;
import javax.swing.ComboBoxModel;

/**
 *
 * @author nicho
 */
public class SemanaLetivaComboBoxListModel extends SemanaLetivaListModel implements ComboBoxModel {
    private SemanaLetiva selection;
    
    @Override
    public void setSelectedItem(Object anItem) {
        selection = (SemanaLetiva)anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selection;
    }
    
}
