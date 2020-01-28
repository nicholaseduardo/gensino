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
import java.io.IOException;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class LegendaController extends AbstractController<Legenda> {
    private static LegendaController instance = null;
    
    private LegendaController() throws IOException, ParserConfigurationException, TransformerException {
        super(LegendaDaoXML.getInstance(), LegendaFactory.getInstance());
    }
    
    public static LegendaController getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null)
            instance = new LegendaController();
        return instance;
    }
    
    public LegendaController(URL url) throws IOException, ParserConfigurationException, TransformerException {
        super(new LegendaDaoXML(url), LegendaFactory.getInstance());
    }
}
