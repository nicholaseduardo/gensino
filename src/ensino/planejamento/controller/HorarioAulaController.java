/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.planejamento.dao.HorarioAulaDao;
import ensino.planejamento.model.HorarioAula;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class HorarioAulaController extends AbstractController {
    
    public HorarioAulaController() throws IOException, ParserConfigurationException, TransformerException {
        super(new HorarioAulaDao());
    }

    @Override
    public Object salvar(HashMap<String, Object> params) throws TransformerException {
        return super.salvar(new HorarioAula(params));
    }

    @Override
    public Object remover(HashMap<String, Object> params) throws TransformerException {
        return super.remover(new HorarioAula(params));
    }
    
    /**
     * Buscar por sequência de plano de avaliação
     * @param id                    Identificação do dia e horário da aula
     * @param planoId               Identificação do plano de ensino
     * @param unidadeCurricularId   Identificação da unidade curricular
     * @param cursoId               Identificação do curso
     * @param campusId              Identificação do campus
     * @return 
     */
    public HorarioAula buscarPor(Integer id, Integer planoId,
            Integer unidadeCurricularId, Integer cursoId, Integer campusId) {
        HorarioAulaDao planoAtividadeDao = (HorarioAulaDao)super.getDao();
        return planoAtividadeDao.findById(id, planoId, unidadeCurricularId,
                cursoId, campusId);
    }
}
