/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import ensino.planejamento.model.PlanoAvaliacao;
import ensino.planejamento.model.PlanoAvaliacaoFactory;
import ensino.planejamento.model.PlanoDeEnsino;
import java.util.List;

/**
 *
 * @author nicho
 */
public class PlanoAvaliacaoController extends AbstractController<PlanoAvaliacao> {
    
    public PlanoAvaliacaoController() throws Exception {
        super(DaoFactory.createPlanoAvaliacaoDao(), PlanoAvaliacaoFactory.getInstance());
    }
    
    public List<PlanoAvaliacao> listar(PlanoDeEnsino o) {
        String filter = "";
        Integer id = o.getId(),
                undId = o.getUnidadeCurricular().getId().getId(),
                cursoId = o.getUnidadeCurricular().getId().getCurso().getId().getId(),
                campusId = o.getUnidadeCurricular().getId().getCurso().getId().getCampus().getId();
        if (DaoFactory.isXML()) {
            filter = String.format("//PlanoAvaliacao/planoAvaliacao[@planoDeEnsinoId=%d and "
                + "@unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]", 
                    id, undId, cursoId, campusId);
        } else {
            filter = String.format(" AND p.id.planoDeEnsino.id = %d ", id);
        }
        
        return super.getDao().list(filter, o);
    }
}
