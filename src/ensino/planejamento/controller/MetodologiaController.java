/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.patterns.DaoPattern;
import ensino.planejamento.dao.MetodologiaDaoXML;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.Metodologia;
import ensino.planejamento.model.MetodologiaFactory;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class MetodologiaController extends AbstractController<Metodologia> {
    private static MetodologiaController instance = null;
    
    private MetodologiaController() throws IOException, ParserConfigurationException, TransformerException {
        super(MetodologiaDaoXML.getInstance(), MetodologiaFactory.getInstance());
    }
    
    public static MetodologiaController getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null)
            instance = new MetodologiaController();
        return instance;
    }
    
    /**
     * Buscar por id da atividade
     * @param sequencia         Sequencia da metodologia no detalhamento do plano
     * @param detalhamentoSeq   Sequencia do detalhamento do plano
     * @param planoId           Identificação do plano de ensino ao qual o detalhamento está vinculado
     * @param undId
     * @param cursoId
     * @param campusId
     * @return 
     */
    public Metodologia buscarPor(Integer sequencia, Integer detalhamentoSeq, Integer planoId,
            Integer undId, Integer cursoId, Integer campusId) {
        DaoPattern<Metodologia> dao = super.getDao();
        return dao.findById(sequencia, detalhamentoSeq, planoId, undId, cursoId, campusId);
    }
    
    public List<Metodologia> listar(Detalhamento o) {
        DaoPattern<Metodologia> dao = super.getDao();
        String filter = String.format("//Metodologia/metodologia"
                + "[@detalhamentoSequencia=%d and @planoDeEnsinoId=%d and "
                + "@unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]", 
                o.getSequencia(),
                o.getPlanoDeEnsino().getId(),
                o.getPlanoDeEnsino().getUnidadeCurricular().getId(),
                o.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getId(),
                o.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getCampus().getId());
        return dao.list(filter, o);
    }
}
