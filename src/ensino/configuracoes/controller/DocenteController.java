/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.xml.DocenteDaoXML;
import ensino.patterns.AbstractController;
import ensino.configuracoes.model.Docente;
import ensino.configuracoes.model.DocenteFactory;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class DocenteController extends AbstractController<Docente> {

    public DocenteController() throws ParserConfigurationException, TransformerException, IOException {
        super(DocenteDaoXML.getInstance(), DocenteFactory.getInstance());
    }
}
