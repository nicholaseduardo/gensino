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
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class RecursoController extends AbstractController {

    public RecursoController() throws IOException, ParserConfigurationException, TransformerException {
        super(RecursoDaoXML.getInstance(), RecursoFactory.getInstance());
    }
}
