/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.planejamento.dao.DetalhamentoDao;
import ensino.planejamento.model.Detalhamento;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class DetalhamentoController extends AbstractController {
    
    public DetalhamentoController() throws IOException, ParserConfigurationException, TransformerException {
        super(new DetalhamentoDao());
    }

    @Override
    public Object salvar(HashMap<String, Object> params) throws TransformerException {
        return super.salvar(new Detalhamento(params));
    }

    @Override
    public Object remover(HashMap<String, Object> params) throws TransformerException {
        return super.remover(new Detalhamento(params));
    }
    
    /**
     * Buscar por id da atividade
     * @param sequencia             Sequencia do detalhamento do plano de ensino
     * @param planoId               Identificação do plano ao qual a atividade está vinculada
     * @param unidadeCurricularId   Identificação da Unidade Curricular
     * @param cursoId               Identificação do curso
     * @param campusId              Identificação do campus
     * @return 
     */
    public Detalhamento buscarPor(Integer sequencia, Integer planoId, 
            Integer unidadeCurricularId, Integer cursoId, Integer campusId) {
        DetalhamentoDao atividadeDao = (DetalhamentoDao)super.getDao();
        return atividadeDao.findById(sequencia, planoId, unidadeCurricularId,
                                    cursoId, campusId);
    }
}
