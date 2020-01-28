/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.xml.InstrumentoAvaliacaoDaoXML;
import ensino.configuracoes.model.InstrumentoAvaliacaoFactory;
import ensino.patterns.AbstractController;
import java.io.IOException;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class InstrumentoAvaliacaoController  extends AbstractController {
    private static InstrumentoAvaliacaoController instance = null;
    
    private InstrumentoAvaliacaoController() throws IOException, ParserConfigurationException, TransformerException {
        super(InstrumentoAvaliacaoDaoXML.getInstance(), InstrumentoAvaliacaoFactory.getInstance());
    }
    
    public static InstrumentoAvaliacaoController getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null)
            instance = new InstrumentoAvaliacaoController();
        return instance;
    }
    
    public InstrumentoAvaliacaoController(URL url) throws IOException, ParserConfigurationException, TransformerException {
        super(new InstrumentoAvaliacaoDaoXML(url), InstrumentoAvaliacaoFactory.getInstance());
    }
}
