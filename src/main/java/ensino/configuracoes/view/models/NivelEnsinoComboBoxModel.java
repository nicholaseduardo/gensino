/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;


import ensino.configuracoes.model.NivelEnsino;
import ensino.patterns.AbstractController;
import java.util.Comparator;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 *
 * @author nicho
 */
public class NivelEnsinoComboBoxModel extends AbstractListModel implements ComboBoxModel {
    private AbstractController col;
    private List<NivelEnsino> list;
    
    private NivelEnsino selection;
    
    public NivelEnsinoComboBoxModel(AbstractController controller) {
        col = controller;
        initComponents();
    }
    
    private void initComponents() {
        refresh();
    }
    
    public void refresh() {
        int index = 0;
        list = (List<NivelEnsino>) col.listar();
        
        if (!list.isEmpty()) {
            list.sort(Comparator.comparing(NivelEnsino::getNome));
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
        selection = (NivelEnsino)anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selection;
    }
}
