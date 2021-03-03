/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.controller.PeriodoLetivoController;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.PeriodoLetivo;
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
public class PeriodoLetivoListModel extends AbstractListModel {

    private PeriodoLetivoController periodoLetivoCol;
    private List<PeriodoLetivo> list;

    private Calendario calendario;

    public PeriodoLetivoListModel() {
        this(null);
    }

    public PeriodoLetivoListModel(Calendario calendario) {
        super();
        this.calendario = calendario;
        initComponents();
    }

    private void initComponents() {
        try {
            periodoLetivoCol = ControllerFactory.createPeriodoLetivoController();
            list = new ArrayList();
            refresh();
        } catch (Exception ex) {
            Logger.getLogger(PeriodoLetivoListModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void refresh() {
        if (calendario == null) {
            list = (List<PeriodoLetivo>) periodoLetivoCol.listar();
        } else {
            list = periodoLetivoCol.listar(calendario);
        }
        if (!list.isEmpty()) {
            list.sort(Comparator.comparing(PeriodoLetivo::getDescricao));
        }
        fireIntervalAdded(this, 0, 0);
    }

    public void setCalendario(Calendario calendario) {
        this.calendario = calendario;
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
