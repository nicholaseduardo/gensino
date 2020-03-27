/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.xml.LegendaDaoXML;
import ensino.configuracoes.model.Legenda;
import ensino.configuracoes.model.LegendaFactory;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import java.net.URL;

/**
 *
 * @author nicho
 */
public class LegendaController extends AbstractController<Legenda> {
    private static LegendaController instance = null;
    
    private LegendaController() throws Exception {
        super(DaoFactory.createLegendaDao(), LegendaFactory.getInstance());
    }
    
    public static LegendaController getInstance() throws Exception {
        if (instance == null)
            instance = new LegendaController();
        return instance;
    }
    
    public LegendaController(URL url) throws Exception {
        super(new LegendaDaoXML(url), LegendaFactory.getInstance());
    }
}
