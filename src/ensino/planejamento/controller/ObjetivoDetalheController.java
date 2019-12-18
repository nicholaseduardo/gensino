/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.patterns.DaoPattern;
import ensino.planejamento.dao.ObjetivoDetalheDaoXML;
import ensino.planejamento.model.ObjetivoDetalhe;
import ensino.planejamento.model.ObjetivoDetalheFactory;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class ObjetivoDetalheController extends AbstractController<ObjetivoDetalhe> {
    
    public ObjetivoDetalheController() throws IOException, ParserConfigurationException, TransformerException {
        super(ObjetivoDetalheDaoXML.getInstance(), ObjetivoDetalheFactory.getInstance());
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
}
