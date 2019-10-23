/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.InstrumentoAvaliacaoDao;
import ensino.configuracoes.model.InstrumentoAvaliacao;
import ensino.patterns.AbstractController;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class InstrumentoAvaliacaoController  extends AbstractController {

    public InstrumentoAvaliacaoController() throws IOException, ParserConfigurationException, TransformerException {
        super(new InstrumentoAvaliacaoDao());
    }

    @Override
    public Object salvar(HashMap<String, Object> params) throws TransformerException {
        return super.salvar(new InstrumentoAvaliacao(params));
    }

    @Override
    public Object remover(HashMap<String, Object> params) throws TransformerException {
        return super.remover(new InstrumentoAvaliacao(params));
    }
}
