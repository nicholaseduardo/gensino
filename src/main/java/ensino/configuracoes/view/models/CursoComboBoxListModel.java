/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.model.Curso;
import javax.swing.ComboBoxModel;

/**
 *
 * @author nicho
 */
public class CursoComboBoxListModel extends CursoListModel implements ComboBoxModel {
    private Curso selection;
    
    @Override
    public void setSelectedItem(Object anItem) {
        selection = (Curso)anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selection;
    }
    
}
