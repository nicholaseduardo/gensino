/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.patterns.AbstractController;
import ensino.patterns.BaseObject;
import ensino.patterns.factory.ControllerFactory;
import ensino.util.types.TipoMetodo;
import java.util.ArrayList;
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
public class MetodoComboBoxModel extends AbstractListModel implements ComboBoxModel {

    private TipoMetodo tipoMetodo;
    private List<BaseObject> list;

    private BaseObject selection;

    public MetodoComboBoxModel(TipoMetodo tipoMetodo) {
        this.list = new ArrayList();
        this.tipoMetodo = tipoMetodo;
        initComponents();
    }

    public MetodoComboBoxModel(List<BaseObject> list) {
        this.list = list;
        initComponents();
    }
    
    private AbstractController getController() throws Exception {
        switch(tipoMetodo) {
            case INSTRUMENTO: return ControllerFactory.createInstrumentoAvaliacaoController();
            case RECURSO: return ControllerFactory.createRecursoController();
            default: return ControllerFactory.createTecnicaController();
        }
    }

    private void initComponents() {
        refresh();
    }

    public void refresh() {
        int index = 0;
        if (tipoMetodo != null) {
            try {
                AbstractController col = getController();
                list = (List<BaseObject>) col.listar();
                col.close();
            } catch (Exception ex) {
                Logger.getLogger(MetodoComboBoxModel.class.getName()).log(Level.SEVERE, null, ex);
            }
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
