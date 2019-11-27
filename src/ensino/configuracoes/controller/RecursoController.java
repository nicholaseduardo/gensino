/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.xml.RecursoDao;
import ensino.configuracoes.model.Recurso;
import ensino.patterns.AbstractController;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class RecursoController extends AbstractController {

    public RecursoController() throws IOException, ParserConfigurationException, TransformerException {
        super(new RecursoDao());
    }

    @Override
    public Object salvar(HashMap<String, Object> params) throws TransformerException {
        return super.salvar(new Recurso(params));
    }

    @Override
    public Object remover(HashMap<String, Object> params) throws TransformerException {
        return super.remover(new Recurso(params));
    }
    
}
