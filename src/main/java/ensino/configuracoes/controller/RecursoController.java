/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.xml.RecursoDaoXML;
import ensino.configuracoes.model.RecursoFactory;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import java.net.URL;

/**
 *
 * @author nicho
 */
public class RecursoController extends AbstractController {
    private static RecursoController instance = null;
    
    private RecursoController() throws Exception {
        super(DaoFactory.createRecursoDao(), RecursoFactory.getInstance());
    }
    
    public static RecursoController getInstance() throws Exception {
        if (instance == null)
            instance = new RecursoController();
        return instance;
    }
    
    public RecursoController(URL url) throws Exception {
        super(new RecursoDaoXML(url), RecursoFactory.getInstance());
    }
}
