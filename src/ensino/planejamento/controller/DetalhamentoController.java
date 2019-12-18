/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.patterns.DaoPattern;
import ensino.planejamento.dao.DetalhamentoDaoXML;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.DetalhamentoFactory;
import ensino.planejamento.model.Metodologia;
import ensino.planejamento.model.ObjetivoDetalhe;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class DetalhamentoController extends AbstractController<Detalhamento> {
    
    public DetalhamentoController() throws IOException, ParserConfigurationException, TransformerException {
        super(DetalhamentoDaoXML.getInstance(), DetalhamentoFactory.getInstance());
    }
    
    @Override
    public Detalhamento salvar(Detalhamento o) throws Exception {
        o = super.salvar(o);
        // Salvar cascade
        AbstractController<Metodologia> colMetodologia = new MetodologiaController();
        colMetodologia.salvarEmCascata(o.getMetodologias());
        
        AbstractController<ObjetivoDetalhe> colObjetivoDetalhe = new ObjetivoDetalheController();
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
}
