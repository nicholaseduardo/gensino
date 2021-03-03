/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import ensino.planejamento.model.Avaliacao;
import ensino.planejamento.model.AvaliacaoFactory;
import ensino.planejamento.model.PlanoAvaliacao;
import java.util.List;

/**
 *
 * @author nicho
 */
public class AvaliacaoController extends AbstractController<Avaliacao> {
    
    public AvaliacaoController() throws Exception {
        super(DaoFactory.createAvaliacaoDao(), AvaliacaoFactory.getInstance());
    }
    
    public List<Avaliacao> listar(PlanoAvaliacao o) {
        String filter = "";
        Integer id = o.getId().getSequencia(),
                planoId = o.getId().getPlanoDeEnsino().getId(),
                undId = o.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getId(),
                cursoId = o.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getCurso().getId().getId(),
                campusId = o.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getCurso().getId().getCampus().getId();
        if (DaoFactory.isXML()) {
            filter = String.format("//Avaliacao/avaliacao"
                + "[@planoAvaliacaoSequencia=%d and @planoDeEnsinoId=%d and "
                + "@unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]", 
                    id, planoId, undId, cursoId, campusId);
        } else {
            filter = String.format(" AND od.id.planoAvaliacao.id.sequencia = %d ", id);
        }
        
        return super.getDao().list(filter, o);
    }
}
