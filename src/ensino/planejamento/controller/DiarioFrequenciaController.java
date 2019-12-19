/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.patterns.DaoPattern;
import ensino.planejamento.dao.DiarioFrequenciaDaoXML;
import ensino.planejamento.model.Diario;
import ensino.planejamento.model.DiarioFrequencia;
import ensino.planejamento.model.DiarioFrequenciaFactory;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class DiarioFrequenciaController extends AbstractController<DiarioFrequencia> {
    private static DiarioFrequenciaController instance = null;
    
    private DiarioFrequenciaController() throws IOException, ParserConfigurationException, TransformerException {
        super(DiarioFrequenciaDaoXML.getInstance(), DiarioFrequenciaFactory.getInstance());
    }
    
    public static DiarioFrequenciaController getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null)
            instance = new DiarioFrequenciaController();
        return instance;
    }
    
    /**
     * Buscar por sequência da frequência do diario
     * @param id                    Identificação da frequência do diário
     * @param diarioId              Identificação do diário
     * @param planoId               Identificação do plano de ensino
     * @param unidadeCurricularId   Identificação da unidade curricular
     * @param cursoId               Identificação do curso
     * @param campusId              Identificação do campus
     * @return 
     * @throws java.text.ParseException 
     */
    public DiarioFrequencia buscarPor(Integer id, Integer diarioId, Integer planoId,
            Integer unidadeCurricularId, Integer cursoId, Integer campusId) throws ParseException {
        DaoPattern<DiarioFrequencia> dao = super.getDao();
        return dao.findById(id, diarioId, planoId, unidadeCurricularId,
                cursoId, campusId);
    }
    
    /**
     * Listagem de diários por data
     * 
     * @param o         Identificação do diário
     * @return 
     */
    public List<DiarioFrequencia> list(Diario o) {
        DaoPattern<DiarioFrequencia> dao = super.getDao();
        String filter = String.format("//DiarioFrequencia/diarioFrequencia"
                + "[@diarioId=%d and @planoDeEnsinoId=%d and "
                + "@unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]", 
                o.getId(),
                o.getPlanoDeEnsino().getId(),
                o.getPlanoDeEnsino().getUnidadeCurricular().getId(),
                o.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getId(),
                o.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getCampus().getId());
        return dao.list(filter, o);
    }
}
