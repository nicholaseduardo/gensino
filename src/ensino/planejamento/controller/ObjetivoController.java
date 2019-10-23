/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.planejamento.dao.ObjetivoDao;
import ensino.planejamento.model.Objetivo;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class ObjetivoController extends AbstractController {
    
    public ObjetivoController() throws IOException, ParserConfigurationException, TransformerException {
        super(new ObjetivoDao());
    }

    @Override
    public Object salvar(HashMap<String, Object> params) throws TransformerException {
        return super.salvar(new Objetivo(params));
    }

    @Override
    public Object remover(HashMap<String, Object> params) throws TransformerException {
        return super.remover(new Objetivo(params));
    }
    
    /**
     * Buscar por id da atividade
     * @param sequencia             Sequencia do objetivo no plano
     * @param planoId               Identificação do plano ao qual a atividade está vinculada
     * @param unidadeCurricularId   Identificação da Unidade Curricular
     * @param cursoId               Identificação do curso
     * @param campusId              Identificação do campus
     * @return 
     */
    public Objetivo buscarPor(Integer sequencia, Integer planoId, 
            Integer unidadeCurricularId, Integer cursoId, Integer campusId) {
        ObjetivoDao objetivoDao = (ObjetivoDao)super.getDao();
        return objetivoDao.findById(sequencia, planoId, unidadeCurricularId,
                                    cursoId, campusId);
    }
}
