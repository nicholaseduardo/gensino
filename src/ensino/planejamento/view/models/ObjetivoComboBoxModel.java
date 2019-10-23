/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.models;

import ensino.planejamento.model.Objetivo;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;


/**
 *
 * @author nicho
 */
public class ObjetivoComboBoxModel extends AbstractListModel implements ComboBoxModel {

    private List<Objetivo> list;
    private Objetivo selection;

    public ObjetivoComboBoxModel() {
        this(new ArrayList());
    }
    
    public ObjetivoComboBoxModel(List<Objetivo> list) {
        super();
        this.list = list;
        initComponents();
    }

    private void initComponents() {
        refresh();
    }

    public void refresh() {
        int index = 0;
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
        selection = (Objetivo) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selection;
    }
}
