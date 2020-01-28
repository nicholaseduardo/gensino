/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.xml.TecnicaDaoXML;
import ensino.configuracoes.model.TecnicaFactory;
import ensino.patterns.AbstractController;
import java.io.IOException;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class TecnicaController  extends AbstractController {
    private static TecnicaController instance = null;
    
    private TecnicaController() throws IOException, ParserConfigurationException, TransformerException {
        super(TecnicaDaoXML.getInstance(), TecnicaFactory.getInstance());
    }
    
    public static TecnicaController getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null)
            instance = new TecnicaController();
        return instance;
    }
    
    public TecnicaController(URL url) throws IOException, ParserConfigurationException, TransformerException {
        super(new TecnicaDaoXML(url), TecnicaFactory.getInstance());
    }
}
