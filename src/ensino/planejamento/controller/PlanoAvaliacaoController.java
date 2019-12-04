/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.patterns.DaoPattern;
import ensino.planejamento.dao.PlanoAvaliacaoDaoXML;
import ensino.planejamento.model.Avaliacao;
import ensino.planejamento.model.PlanoAvaliacao;
import ensino.planejamento.model.PlanoAvaliacaoFactory;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class PlanoAvaliacaoController extends AbstractController<PlanoAvaliacao> {
    
    public PlanoAvaliacaoController() throws IOException, ParserConfigurationException, TransformerException {
        super(new PlanoAvaliacaoDaoXML(), PlanoAvaliacaoFactory.getInstance());
    }

    @Override
    public PlanoAvaliacao salvar(PlanoAvaliacao o) throws Exception {
        o = super.salvar(o);
        // Salvar cascade
        AbstractController<Avaliacao> colAvaliacao = new AvaliacaoController();
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
}
