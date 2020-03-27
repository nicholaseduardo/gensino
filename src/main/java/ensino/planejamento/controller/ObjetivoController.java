/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import ensino.planejamento.model.Objetivo;
import ensino.planejamento.model.ObjetivoFactory;
import ensino.planejamento.model.PlanoDeEnsino;
import java.util.List;

/**
 *
 * @author nicho
 */
public class ObjetivoController extends AbstractController<Objetivo> {
    
    public ObjetivoController() throws Exception {
        super(DaoFactory.createObjetivoDao(), ObjetivoFactory.getInstance());
    }
    
    public List<Objetivo> listar(PlanoDeEnsino o) {
        String filter = "";
        Integer id = o.getId(),
                undId = o.getUnidadeCurricular().getId().getId(),
                cursoId = o.getUnidadeCurricular().getId().getCurso().getId().getId(),
                campusId = o.getUnidadeCurricular().getId().getCurso().getId().getCampus().getId();
        if (DaoFactory.isXML()) {
            filter = String.format("//Objetivo/objetivo[@planoDeEnsinoId=%d and "
                + "@unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]",
                    id, undId, cursoId, campusId);
        } else {
            filter = String.format(" AND o.id.planoDeEnsino.id = %d ", id);
        }
        
        return super.getDao().list(filter, o);
    }
}
