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
import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class ObjetivoController extends AbstractController<Objetivo> {
    
    public ObjetivoController() throws IOException, ParserConfigurationException, TransformerException {
        super(new ObjetivoDaoXML(), ObjetivoFactory.getInstance());
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
}
