/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.patterns.DaoPattern;
import ensino.planejamento.dao.AvaliacaoDaoXML;
import ensino.planejamento.model.Avaliacao;
import ensino.planejamento.model.AvaliacaoFactory;
import ensino.planejamento.model.PlanoAvaliacao;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class AvaliacaoController extends AbstractController<Avaliacao> {
    private static AvaliacaoController instance = null;
    
    private AvaliacaoController() throws IOException, ParserConfigurationException, TransformerException {
        super(AvaliacaoDaoXML.getInstance(), AvaliacaoFactory.getInstance());
    }
    
    public static AvaliacaoController getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null)
            instance = new AvaliacaoController();
        return instance;
    }
    
    /**
     * Buscar por id da atividade
     * @param estudanteId         id do estudante
     * @param planoAvaliacaoSeq   Sequencia do planoAvaliacao do plano
     * @param planoId           Identificação do plano de ensino ao qual o planoAvaliacao está vinculado
     * @param undId
     * @param cursoId
     * @param campusId
     * @return 
     */
    public Avaliacao buscarPor(Integer estudanteId, Integer planoAvaliacaoSeq, Integer planoId,
            Integer undId, Integer cursoId, Integer campusId) {
        DaoPattern<Avaliacao> dao = super.getDao();
        return dao.findById(estudanteId, planoAvaliacaoSeq, planoId, undId, cursoId, campusId);
    }
    
    public List<Avaliacao> listar(PlanoAvaliacao o) {
        DaoPattern<Avaliacao> dao = super.getDao();
        String filter = String.format("//Avaliacao/avaliacao"
                + "[@planoAvaliacaoSequencia=%d and @planoDeEnsinoId=%d and "
                + "@unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]", 
                o.getSequencia(),
                o.getPlanoDeEnsino().getId(),
                o.getPlanoDeEnsino().getUnidadeCurricular().getId(),
                o.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getId(),
                o.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getCampus().getId());
        return dao.list(filter, o);
    }
}
