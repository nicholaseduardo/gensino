/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.controller.LegendaController;
import ensino.configuracoes.model.Legenda;
import ensino.patterns.factory.ControllerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractListModel;

/**
 *
 * @author nicho
 */
public class LegendaListModel extends AbstractListModel {

    private LegendaController legendaCol;
    private List<Legenda> list;

    public LegendaListModel() {
        super();
        initComponents();
    }

    private void initComponents() {
        try {
            legendaCol = ControllerFactory.createLegendaController();
            list = new ArrayList();
            refresh();
        } catch (Exception ex) {
            Logger.getLogger(LegendaListModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void refresh() {
        list = (List<Legenda>) legendaCol.listar();
        fireIntervalAdded(this, 0, 0);
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public Object getElementAt(int index) {
        return list.get(index);
    }

}
