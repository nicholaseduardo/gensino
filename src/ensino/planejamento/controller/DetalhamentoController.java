/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.patterns.DaoPattern;
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.dao.DetalhamentoDaoXML;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.DetalhamentoFactory;
import ensino.planejamento.model.Metodologia;
import ensino.planejamento.model.ObjetivoDetalhe;
import ensino.planejamento.model.PlanoDeEnsino;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class DetalhamentoController extends AbstractController<Detalhamento> {
    private static DetalhamentoController instance = null;
    
    private DetalhamentoController() throws IOException, ParserConfigurationException, TransformerException {
        super(DetalhamentoDaoXML.getInstance(), DetalhamentoFactory.getInstance());
    }
    
    public static DetalhamentoController getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null)
            instance = new DetalhamentoController();
        return instance;
    }
    
    @Override
    public Detalhamento salvar(Detalhamento o) throws Exception {
        o = super.salvar(o);
        // Salvar cascade
        AbstractController<Metodologia> colMetodologia = ControllerFactory.createMetodologiaController();
        colMetodologia.salvarEmCascata(o.getMetodologias());
        
        AbstractController<ObjetivoDetalhe> colObjetivoDetalhe = ControllerFactory.createObjetivoDetalheController();
        colObjetivoDetalhe.salvarEmCascata(o.getObjetivoDetalhes());
        
        return o;
    }
    
    /**
     * Buscar por id da atividade
     * @param sequencia             Sequencia do detalhamento do plano de ensino
     * @param planoId               Identificação do plano ao qual a atividade está vinculada
     * @param unidadeCurricularId   Identificação da Unidade Curricular
     * @param cursoId               Identificação do curso
     * @param campusId              Identificação do campus
     * @return 
     */
    public Detalhamento buscarPor(Integer sequencia, Integer planoId, 
            Integer unidadeCurricularId, Integer cursoId, Integer campusId) {
        DaoPattern<Detalhamento> dao = super.getDao();
        return dao.findById(sequencia, planoId, unidadeCurricularId,
                                    cursoId, campusId);
    }
    
    public List<Detalhamento> listar(PlanoDeEnsino o) {
        DaoPattern<Detalhamento> dao = super.getDao();
        String filter = String.format("//Detalhamento/detalhamento[@planoDeEnsinoId=%d and "
                + "@unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]", 
                o.getId(),
                o.getUnidadeCurricular().getId(),
                o.getUnidadeCurricular().getCurso().getId(),
                o.getUnidadeCurricular().getCurso().getCampus().getId());
        return dao.list(filter, o);
    }
}
