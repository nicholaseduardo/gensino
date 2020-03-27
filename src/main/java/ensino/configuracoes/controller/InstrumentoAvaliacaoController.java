/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.sqlite.InstrumentoAvaliacaoDaoSQL;
import ensino.configuracoes.dao.xml.InstrumentoAvaliacaoDaoXML;
import ensino.configuracoes.model.InstrumentoAvaliacaoFactory;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import java.net.URL;

/**
 *
 * @author nicho
 */
public class InstrumentoAvaliacaoController  extends AbstractController {
    private static InstrumentoAvaliacaoController instance = null;
    
    private InstrumentoAvaliacaoController() throws Exception {
        super(DaoFactory.createInstrumentoAvaliacaoDao(), InstrumentoAvaliacaoFactory.getInstance());
    }
    
    public static InstrumentoAvaliacaoController getInstance() throws Exception {
        if (instance == null)
            instance = new InstrumentoAvaliacaoController();
        return instance;
    }
    
    public InstrumentoAvaliacaoController(URL url) throws Exception {
        super(new InstrumentoAvaliacaoDaoXML(url), InstrumentoAvaliacaoFactory.getInstance());
    }
}
