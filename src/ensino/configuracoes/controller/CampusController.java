/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.xml.CampusDaoXML;
import ensino.patterns.AbstractController;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.CampusFactory;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class CampusController extends AbstractController<Campus> {
        
    public CampusController() throws ParserConfigurationException, TransformerException, IOException {
        super(new CampusDaoXML());
    }

    /**
     *
     * @param params
     * @return 
     * @throws TransformerException
     */
    @Override
    public Campus salvar(HashMap<String, Object> params) throws TransformerException {
        return super.salvar(CampusFactory.getInstance().getObject(params));
    }

    @Override
    public Campus remover(HashMap<String, Object> params) throws TransformerException {
        return super.remover(CampusFactory.getInstance().getObject(params));
    }
}
