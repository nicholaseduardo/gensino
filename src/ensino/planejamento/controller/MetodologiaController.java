/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.planejamento.dao.MetodologiaDao;
import ensino.planejamento.model.Metodologia;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class MetodologiaController extends AbstractController {
    
    public MetodologiaController() throws IOException, ParserConfigurationException, TransformerException {
        super(new MetodologiaDao());
    }

    @Override
    public Object salvar(HashMap<String, Object> params) throws TransformerException {
        return super.salvar(new Metodologia(params));
    }

    @Override
    public Object remover(HashMap<String, Object> params) throws TransformerException {
        return super.remover(new Metodologia(params));
    }
    
    /**
     * Buscar por id da atividade
     * @param sequencia         Sequencia da metodologia no detalhamento do plano
     * @param detalhamentoSeq   Sequencia do detalhamento do plano
     * @param planoId           Identificação do plano de ensino ao qual o detalhamento está vinculado
     * @return 
     */
    public Metodologia buscarPor(Integer sequencia, Integer detalhamentoSeq, Integer planoId) {
        MetodologiaDao metodologiaDao = (MetodologiaDao)super.getDao();
        return metodologiaDao.findById(sequencia, detalhamentoSeq, planoId);
    }
}
