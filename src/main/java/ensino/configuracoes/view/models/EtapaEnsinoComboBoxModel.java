/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.controller.EtapaEnsinoController;
import ensino.configuracoes.model.EtapaEnsino;
import ensino.configuracoes.model.NivelEnsino;
import ensino.patterns.factory.ControllerFactory;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 *
 * @author nicho
 */
public class EtapaEnsinoComboBoxModel extends AbstractListModel implements ComboBoxModel {

    private List<EtapaEnsino> list;
    private NivelEnsino nivelEnsino;

    private EtapaEnsino selection;

    public EtapaEnsinoComboBoxModel(List<EtapaEnsino> list) {
        this.list = list;
    }

    public EtapaEnsinoComboBoxModel(NivelEnsino nivelEnsino) {
        this.nivelEnsino = nivelEnsino;
        initComponents();
    }

    private void initComponents() {
        refresh();
    }

    public void refresh() {
        int index = 0;
        if (nivelEnsino != null) {
            try {
                EtapaEnsinoController controller = ControllerFactory.createEtapaEnsinoController();
                list = (List<EtapaEnsino>) controller.listar(nivelEnsino);
                controller.close();
            } catch (Exception ex) {
                Logger.getLogger(EtapaEnsinoComboBoxModel.class.getName()).log(Level.SEVERE, null, ex);
            }
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
