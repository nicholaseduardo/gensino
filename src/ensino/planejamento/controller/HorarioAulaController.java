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
import ensino.planejamento.model.PlanoDeEnsino;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class HorarioAulaController extends AbstractController<HorarioAula> {
    private static HorarioAulaController instance = null;
    
    private HorarioAulaController() throws IOException, ParserConfigurationException, TransformerException {
        super(HorarioAulaDaoXML.getInstance(), HorarioAulaFactory.getInstance());
    }
    
    public static HorarioAulaController getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null)
            instance = new HorarioAulaController();
        return instance;
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
    
    public List<HorarioAula> listar(PlanoDeEnsino o) {
        DaoPattern<HorarioAula> dao = super.getDao();
        String filter = String.format("//HorarioAula/horarioAula[@planoDeEnsinoId=%d and "
                + "@unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]", 
                o.getId(),
                o.getUnidadeCurricular().getId(),
                o.getUnidadeCurricular().getCurso().getId(),
                o.getUnidadeCurricular().getCurso().getCampus().getId());
        return dao.list(filter, o);
    }
}
