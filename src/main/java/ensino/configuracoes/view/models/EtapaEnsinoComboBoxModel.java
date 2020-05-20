/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.controller.EtapaEnsinoController;
import ensino.configuracoes.model.EtapaEnsino;
import ensino.configuracoes.model.NivelEnsino;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 *
 * @author nicho
 */
public class EtapaEnsinoComboBoxModel extends AbstractListModel implements ComboBoxModel {

    private EtapaEnsinoController col;
    private List<EtapaEnsino> list;
    private NivelEnsino nivelEnsino;

    private EtapaEnsino selection;

    public EtapaEnsinoComboBoxModel(List<EtapaEnsino> list) {
        this.list = list;
    }

    public EtapaEnsinoComboBoxModel(EtapaEnsinoController controller, NivelEnsino nivelEnsino) {
        col = controller;
        this.nivelEnsino = nivelEnsino;
        initComponents();
    }

    private void initComponents() {
        refresh();
    }

    public void refresh() {
        int index = 0;
        if (col != null && nivelEnsino != null) {
            list = (List<EtapaEnsino>) col.listar(nivelEnsino);
        }

        setSelectedItem(null);
        fireIntervalAdded(this, 0, index);
    }
    
    public void addElement(EtapaEnsino o) {
        list.add(o);
        refresh();
    }
    
    public void removeElement(EtapaEnsino o) {
        list.remove(o);
        refresh();
    }
    
    public void updateElement(EtapaEnsino o ) {
        for (int i = 0; i < list.size(); i++) {
            EtapaEnsino ee = list.get(i);
            if (ee.getId().equals(o.getId())) {
                list.set(i, o);
                break;
            }
        }
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
        selection = (EtapaEnsino) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selection;
    }
}
