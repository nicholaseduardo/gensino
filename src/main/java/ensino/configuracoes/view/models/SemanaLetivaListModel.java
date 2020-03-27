/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.controller.SemanaLetivaController;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.PeriodoLetivo;
import ensino.configuracoes.model.SemanaLetiva;
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
public class SemanaLetivaListModel extends AbstractListModel {

    private SemanaLetivaController semanaLetivaCol;
    private List<SemanaLetiva> list;

    private PeriodoLetivo periodoLetivo;

    public SemanaLetivaListModel() {
        this(null);
    }

    public SemanaLetivaListModel(PeriodoLetivo periodoLetivo) {
        super();
        this.periodoLetivo = periodoLetivo;
        initComponents();
    }

    private void initComponents() {
        try {
            semanaLetivaCol = ControllerFactory.createSemanaLetivaController();
            list = new ArrayList();
            refresh();
        } catch (Exception ex) {
            Logger.getLogger(SemanaLetivaListModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void refresh() {
        if (periodoLetivo == null) {
            list = (List<SemanaLetiva>) semanaLetivaCol.listar();
        } else {
            Calendario cal = periodoLetivo.getId().getCalendario();
            list = semanaLetivaCol.listar(periodoLetivo);
        }
        if (!list.isEmpty()) {
            list.sort(Comparator.comparing(SemanaLetiva::getDescricao));
        }
        fireIntervalAdded(this, 0, 0);
    }

    public void setPeriodoLetivo(PeriodoLetivo periodoLetivo) {
        this.periodoLetivo = periodoLetivo;
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
