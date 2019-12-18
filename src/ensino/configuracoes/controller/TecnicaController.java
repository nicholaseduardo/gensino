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
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class TecnicaController  extends AbstractController {

    public TecnicaController() throws IOException, ParserConfigurationException, TransformerException {
        super(TecnicaDaoXML.getInstance(), TecnicaFactory.getInstance());
    }
}
