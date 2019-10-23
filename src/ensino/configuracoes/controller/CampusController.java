/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.CampusDao;
import ensino.patterns.AbstractController;
import ensino.configuracoes.model.Campus;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class CampusController extends AbstractController {
        
    public CampusController() throws ParserConfigurationException, TransformerException, IOException {
        super(new CampusDao());
    }

    /**
     *
     * @param params
     * @return 
     * @throws TransformerException
     */
    @Override
    public Object salvar(HashMap<String, Object> params) throws TransformerException {
        return super.salvar(new Campus(params));
    }

    @Override
    public Object remover(HashMap<String, Object> params) throws TransformerException {
        return super.remover(new Campus(params));
    }
}
