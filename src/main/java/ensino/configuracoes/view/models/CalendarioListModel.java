/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.controller.CalendarioController;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Calendario;
import ensino.patterns.factory.ControllerFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractListModel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class CalendarioListModel extends AbstractListModel {

    private CalendarioController calendarioCol;
    private List<Calendario> list;

    private Campus campus;

    public CalendarioListModel() {
        this(null);
    }

    public CalendarioListModel(Campus campus) {
        super();
        this.campus = campus;
        initComponents();
    }

    private void initComponents() {
        try {
            calendarioCol = ControllerFactory.createCalendarioController();
            list = new ArrayList();
            refresh();
        } catch (Exception ex) {
            Logger.getLogger(CalendarioListModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void refresh() {
        if (campus == null) {
            list = (List<Calendario>) calendarioCol.listar();
        } else {
            list = calendarioCol.listar(campus);
        }
        
        fireIntervalAdded(this, 0, 0);
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
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
