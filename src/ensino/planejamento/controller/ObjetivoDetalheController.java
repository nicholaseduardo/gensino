/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.patterns.DaoPattern;
import ensino.planejamento.dao.ObjetivoDetalheDaoXML;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.ObjetivoDetalhe;
import ensino.planejamento.model.ObjetivoDetalheFactory;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class ObjetivoDetalheController extends AbstractController<ObjetivoDetalhe> {
    private static ObjetivoDetalheController instance = null;
    
    private ObjetivoDetalheController() throws IOException, ParserConfigurationException, TransformerException {
        super(ObjetivoDetalheDaoXML.getInstance(), ObjetivoDetalheFactory.getInstance());
    }
    
    public static ObjetivoDetalheController getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null)
            instance = new ObjetivoDetalheController();
        return instance;
    }
    
    /**
     * Buscar por id da atividade
     * @param objetivoSequencia         Sequencia da metodologia no detalhamento do plano
     * @param detalhamentoSeq   Sequencia do detalhamento do plano
     * @param planoId           Identificação do plano de ensino ao qual o detalhamento está vinculado
     * @param undId
     * @param cursoId
     * @param campusId
     * @return 
     */
    public ObjetivoDetalhe buscarPor(Integer objetivoSequencia, Integer detalhamentoSeq, Integer planoId,
            Integer undId, Integer cursoId, Integer campusId) {
        DaoPattern<ObjetivoDetalhe> dao = super.getDao();
        return dao.findById(objetivoSequencia, detalhamentoSeq, planoId, undId, cursoId, campusId);
    }
    
    public List<ObjetivoDetalhe> listar(Detalhamento o) {
        DaoPattern<ObjetivoDetalhe> dao = super.getDao();
        String filter = String.format("//ObjetivoDetalhe/objetivoDetalhe"
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
