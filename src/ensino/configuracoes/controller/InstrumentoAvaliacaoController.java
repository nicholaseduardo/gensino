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
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class InstrumentoAvaliacaoController  extends AbstractController {

    public InstrumentoAvaliacaoController() throws IOException, ParserConfigurationException, TransformerException {
        super(new InstrumentoAvaliacaoDaoXML(), InstrumentoAvaliacaoFactory.getInstance());
    }
}
