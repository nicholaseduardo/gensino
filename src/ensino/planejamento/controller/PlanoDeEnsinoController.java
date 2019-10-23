/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.planejamento.dao.PlanoDeEnsinoDao;
import ensino.planejamento.model.PlanoDeEnsino;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class PlanoDeEnsinoController extends AbstractController {
    
    public PlanoDeEnsinoController() throws IOException, ParserConfigurationException, TransformerException {
        super(new PlanoDeEnsinoDao());
    }
    
    @Override
    public List<PlanoDeEnsino> listar() {
        PlanoDeEnsinoDao d = (PlanoDeEnsinoDao) getDao();
        return d.list("");
    }
    
    /**
     * Listagem de planos de ensino
     * 
     * @param campusId  Identificação do campus
     * @param cursoId   Identificação do curso
     * @param unidadeId Identificação da unidade curricular
     * @return 
     */
    public List<PlanoDeEnsino> listar(Integer campusId, Integer cursoId,
            Integer unidadeId) {
        PlanoDeEnsinoDao d = (PlanoDeEnsinoDao) getDao();
        return d.list(unidadeId, cursoId, campusId);
    }

    @Override
    public Object salvar(HashMap<String, Object> params) throws TransformerException {
        PlanoDeEnsino planoDeEnsino = new PlanoDeEnsino(params);
        planoDeEnsino.criarDiarios();
        planoDeEnsino.criarAvaliacoes();
        return super.salvar(planoDeEnsino);
    }

    @Override
    public Object remover(HashMap<String, Object> params) throws TransformerException {
        return super.remover(new PlanoDeEnsino(params));
    }
    
    /**
     * Buscar por sequência de plano de avaliação
     * @param id                    Identificação do plano de ensino
     * @param unidadeCurricularId   Identificação da unidade curricular
     * @param cursoId               Identificação do curso
     * @param campusId              Identificação do campus
     * @return 
     * @throws java.text.ParseException 
     */
    public PlanoDeEnsino buscarPor(Integer id, 
            Integer unidadeCurricularId, Integer cursoId,
            Integer campusId) throws ParseException {
        PlanoDeEnsinoDao planoDeEnsino = (PlanoDeEnsinoDao)super.getDao();
        return planoDeEnsino.findById(id, unidadeCurricularId, cursoId, campusId);
    }
}
