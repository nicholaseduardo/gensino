/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.LegendaDao;
import ensino.configuracoes.model.Legenda;
import ensino.patterns.AbstractController;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class LegendaController extends AbstractController {
    public LegendaController() throws IOException, ParserConfigurationException, TransformerException {
        super(new LegendaDao());
    }
    
    @Override
    public List<Legenda> listar() {
        return (List<Legenda>) getDao().list();
    }

    /**
     *
     * @param params
     * @return 
     * @throws TransformerException
     */
    @Override
    public Object salvar(HashMap<String, Object> params) throws TransformerException {
        return super.salvar(new Legenda(params));
    }

    @Override
    public Object remover(HashMap<String, Object> params) throws TransformerException {
        return super.remover(new Legenda(params));
    }
}
