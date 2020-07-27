/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.patterns.AbstractController;
import ensino.patterns.BaseObject;
import java.util.Comparator;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 *
 * @author nicho
 */
public class MetodoComboBoxModel extends AbstractListModel implements ComboBoxModel {

    private AbstractController metodoCol;
    private List<BaseObject> list;

    private BaseObject selection;

    public MetodoComboBoxModel(AbstractController controller) {
        metodoCol = controller;
        initComponents();
    }

    public MetodoComboBoxModel(List<BaseObject> list) {
        this.list = list;
        initComponents();
    }

    private void initComponents() {
        refresh();
    }

    public void refresh() {
        int index = 0;
        if (metodoCol != null) {
            list = (List<BaseObject>) metodoCol.listar();
        }

        if (!list.isEmpty()) {
            list.sort(Comparator.comparing(BaseObject::getNome));
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
        selection = (BaseObject) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selection;
    }
}
