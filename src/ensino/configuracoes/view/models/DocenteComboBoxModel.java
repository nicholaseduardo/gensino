/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.controller.DocenteController;
import ensino.configuracoes.model.Docente;
import ensino.patterns.factory.ControllerFactory;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class DocenteComboBoxModel extends AbstractListModel implements ComboBoxModel {
    private DocenteController docenteCol;
    private List<Docente> list;
    
    private Docente selection;
    
    public DocenteComboBoxModel() {
        initComponents();
    }
    
    private void initComponents() {
        try {
            docenteCol = ControllerFactory.createDocenteController();
            refresh();
        } catch (IOException | ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(DocenteComboBoxModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void refresh() {
        int index = 0;
        list = (List<Docente>) docenteCol.listar();
        
        if (!list.isEmpty()) {
            list.sort(Comparator.comparing(Docente::getNome));
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
        selection = (Docente)anItem;
    }

    @Override
    public Object getSelectedItem() {
        return selection;
    }
}
