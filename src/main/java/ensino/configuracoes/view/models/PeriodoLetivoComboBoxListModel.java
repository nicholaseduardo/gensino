/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.model.PeriodoLetivo;
import javax.swing.ComboBoxModel;

/**
 *
 * @author nicho
 */
public class PeriodoLetivoComboBoxListModel extends PeriodoLetivoListModel implements ComboBoxModel {
    private PeriodoLetivo selection;
    
    @Override
    public void setSelectedItem(Object anItem) {
        selection = (PeriodoLetivo)anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selection;
    }
    
}
