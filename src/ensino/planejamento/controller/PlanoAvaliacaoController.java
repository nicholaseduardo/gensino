/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.planejamento.dao.PlanoAvaliacaoDao;
import ensino.planejamento.model.PlanoAvaliacao;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class PlanoAvaliacaoController extends AbstractController {
    
    public PlanoAvaliacaoController() throws IOException, ParserConfigurationException, TransformerException {
        super(new PlanoAvaliacaoDao());
    }

    @Override
    public Object salvar(HashMap<String, Object> params) throws TransformerException {
        return super.salvar(new PlanoAvaliacao(params));
    }

    @Override
    public Object remover(HashMap<String, Object> params) throws TransformerException {
        return super.remover(new PlanoAvaliacao(params));
    }
    
    /**
     * Buscar por sequência de plano de avaliação
     * @param sequencia             Sequencia da atividade no plano de avaliação
     * @param planoId               Identificação do plano de ensino
     * @param unidadeCurricularId   Identificação da unidade curricular
     * @param cursoId               Identificação do curso
     * @param campusId              Identificação do campus
     * @return 
     */
    public PlanoAvaliacao buscarPor(Integer sequencia, Integer planoId,
            Integer unidadeCurricularId, Integer cursoId, Integer campusId) {
        PlanoAvaliacaoDao planoAtividadeDao = (PlanoAvaliacaoDao)super.getDao();
        return planoAtividadeDao.findById(sequencia, planoId, unidadeCurricularId,
                cursoId, campusId);
    }
}
