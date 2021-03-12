/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.controller.CursoController;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.patterns.factory.ControllerFactory;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractListModel;

/**
 *
 * @author nicho
 */
public class CursoListModel extends AbstractListModel {

    private List<Curso> list;

    private Campus campus;

    public CursoListModel() {
        this(null);
    }

    public CursoListModel(Campus campus) {
        super();
        this.campus = campus;
        initComponents();
    }

    private void initComponents() {
        try {
            list = new ArrayList();
            refresh();
        } catch (Exception ex) {
            Logger.getLogger(CursoListModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void refresh() {
        try {
            CursoController cursoCol = ControllerFactory.createCursoController();
            list = cursoCol.listar(campus);
            cursoCol.close();
            
            if (!list.isEmpty()) {
                list.sort(Comparator.comparing(Curso::getNome));
            }
            fireIntervalAdded(this, 0, 0);
        } catch (Exception ex) {
            Logger.getLogger(CursoListModel.class.getName()).log(Level.SEVERE, null, ex);
        }
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
