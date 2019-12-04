/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.patterns.DaoPattern;
import ensino.planejamento.dao.HorarioAulaDaoXML;
import ensino.planejamento.model.HorarioAula;
import ensino.planejamento.model.HorarioAulaFactory;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class HorarioAulaController extends AbstractController<HorarioAula> {
    
    public HorarioAulaController() throws IOException, ParserConfigurationException, TransformerException {
        super(new HorarioAulaDaoXML(), HorarioAulaFactory.getInstance());
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
        DaoPattern<HorarioAula> dao = super.getDao();
        return dao.findById(id, planoId, unidadeCurricularId,
                                    cursoId, campusId);
    }
}
