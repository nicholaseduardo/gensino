/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.patterns.DaoPattern;
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.dao.PlanoAvaliacaoDaoXML;
import ensino.planejamento.model.Avaliacao;
import ensino.planejamento.model.PlanoAvaliacao;
import ensino.planejamento.model.PlanoAvaliacaoFactory;
import ensino.planejamento.model.PlanoDeEnsino;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class PlanoAvaliacaoController extends AbstractController<PlanoAvaliacao> {
    private static PlanoAvaliacaoController instance = null;
    
    private PlanoAvaliacaoController() throws IOException, ParserConfigurationException, TransformerException {
        super(PlanoAvaliacaoDaoXML.getInstance(), PlanoAvaliacaoFactory.getInstance());
    }
    
    public static PlanoAvaliacaoController getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null)
            instance = new PlanoAvaliacaoController();
        return instance;
    }

    @Override
    public PlanoAvaliacao salvar(PlanoAvaliacao o) throws Exception {
        o = super.salvar(o);
        // Salvar cascade
        AbstractController<Avaliacao> colAvaliacao = ControllerFactory.createAvaliacaoController();
        colAvaliacao.salvarEmCascata(o.getAvaliacoes());
        
        return o;
    }
    
    /**
     * Buscar por sequência de plano de avaliação
     * @param sequencia             Sequencia da atividade no plano de avaliação
     * @param planoId               Identificação do plano de ensino
     * @param unidadeCurricularId   Identificação da unidade curricular
     * @param cursoId               Identificação do curso
     * @param campusId              Identificação do campus
     * @return 
     */
    public PlanoAvaliacao buscarPor(Integer sequencia, Integer planoId,
            Integer unidadeCurricularId, Integer cursoId, Integer campusId) {
        DaoPattern<PlanoAvaliacao> dao = super.getDao();
        return dao.findById(sequencia, planoId, unidadeCurricularId,
                cursoId, campusId);
    }
    
    public List<PlanoAvaliacao> listar(PlanoDeEnsino o) {
        DaoPattern<PlanoAvaliacao> dao = super.getDao();
        String filter = String.format("//PlanoAvaliacao/planoAvaliacao[@planoDeEnsinoId=%d and "
                + "@unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]", 
                o.getId(),
                o.getUnidadeCurricular().getId(),
                o.getUnidadeCurricular().getCurso().getId(),
                o.getUnidadeCurricular().getCurso().getCampus().getId());
        return dao.list(filter, o);
    }
}
