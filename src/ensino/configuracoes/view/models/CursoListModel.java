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
public class CursoListModel extends AbstractListModel {

    private CursoController cursoCol;
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
            cursoCol = ControllerFactory.createCursoController();
            list = new ArrayList();
            refresh();
        } catch (IOException | ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(CursoListModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void refresh() {
        if (campus == null) {
            list = (List<Curso>) cursoCol.listar();
        } else {
            list = cursoCol.listar(campus);
        }
        if (!list.isEmpty()) {
            list.sort(Comparator.comparing(Curso::getNome));
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
