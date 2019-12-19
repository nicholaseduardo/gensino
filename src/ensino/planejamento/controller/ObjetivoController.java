/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.patterns.DaoPattern;
import ensino.planejamento.dao.ObjetivoDaoXML;
import ensino.planejamento.model.Objetivo;
import ensino.planejamento.model.ObjetivoFactory;
import ensino.planejamento.model.PlanoDeEnsino;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class ObjetivoController extends AbstractController<Objetivo> {
    private static ObjetivoController instance = null;
    
    private ObjetivoController() throws IOException, ParserConfigurationException, TransformerException {
        super(ObjetivoDaoXML.getInstance(), ObjetivoFactory.getInstance());
    }
    
    public static ObjetivoController getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null)
            instance = new ObjetivoController();
        return instance;
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
        DaoPattern<Objetivo> dao = super.getDao();
        return dao.findById(sequencia, planoId, unidadeCurricularId,
                                    cursoId, campusId);
    }
    
    public List<Objetivo> listar(PlanoDeEnsino o) {
        DaoPattern<Objetivo> dao = super.getDao();
        String filter = String.format("//Objetivo/objetivo[@planoDeEnsinoId=%d and "
                + "@unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]", 
                o.getId(),
                o.getUnidadeCurricular().getId(),
                o.getUnidadeCurricular().getCurso().getId(),
                o.getUnidadeCurricular().getCurso().getCampus().getId());
        return dao.list(filter, o);
    }
}
