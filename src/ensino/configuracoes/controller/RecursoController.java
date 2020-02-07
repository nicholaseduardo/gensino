/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.xml.RecursoDaoXML;
import ensino.configuracoes.model.RecursoFactory;
import ensino.patterns.AbstractController;
import java.io.IOException;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class RecursoController extends AbstractController {
    private static RecursoController instance = null;
    
    private RecursoController() throws IOException, ParserConfigurationException, TransformerException {
        super(RecursoDaoXML.getInstance(), RecursoFactory.getInstance());
    }
    
    public static RecursoController getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null)
            instance = new RecursoController();
        return instance;
    }
    
    public RecursoController(URL url) throws IOException, ParserConfigurationException, TransformerException {
        super(new RecursoDaoXML(url), RecursoFactory.getInstance());
    }
}
