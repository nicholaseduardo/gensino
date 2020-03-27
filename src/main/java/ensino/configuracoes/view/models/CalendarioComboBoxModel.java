/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.model.Calendario;
import javax.swing.ComboBoxModel;

/**
 *
 * @author nicho
 */
public class CalendarioComboBoxModel extends CalendarioListModel implements ComboBoxModel {
    private Calendario selection;
    
    @Override
    public void setSelectedItem(Object anItem) {
        selection = (Calendario)anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selection;
    }
}
