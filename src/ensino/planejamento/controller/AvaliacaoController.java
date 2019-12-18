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
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class AvaliacaoController extends AbstractController<Avaliacao> {
    
    public AvaliacaoController() throws IOException, ParserConfigurationException, TransformerException {
        super(AvaliacaoDaoXML.getInstance(), AvaliacaoFactory.getInstance());
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
}
