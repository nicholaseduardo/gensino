/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import ensino.planejamento.dao.HorarioAulaDaoXML;
import ensino.planejamento.model.HorarioAula;
import ensino.planejamento.model.HorarioAulaFactory;
import ensino.planejamento.model.PlanoDeEnsino;
import java.net.URL;
import java.util.List;

/**
 *
 * @author nicho
 */
public class HorarioAulaController extends AbstractController<HorarioAula> {
    
    public HorarioAulaController() throws Exception {
        super(DaoFactory.createHorarioAulaDao(), HorarioAulaFactory.getInstance());
    }
    
    public HorarioAulaController(URL url) throws Exception {
        super(new HorarioAulaDaoXML(url), HorarioAulaFactory.getInstance());
    }
    
    public List<HorarioAula> listar(PlanoDeEnsino o) {
        String filter = "";
        Integer id = o.getId(),
                undId = o.getUnidadeCurricular().getId().getId(),
                cursoId = o.getUnidadeCurricular().getId().getCurso().getId().getId(),
                campusId = o.getUnidadeCurricular().getId().getCurso().getId().getCampus().getId();
        if (DaoFactory.isXML()) {
            filter = String.format("//HorarioAula/horarioAula[@planoDeEnsinoId=%d and "
                + "@unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]", 
                    id, undId, cursoId, campusId);
        } else {
            filter = String.format(" AND ha.id.planoDeEnsino.id = %d ", id);
        }
        
        return super.getDao().list(filter, o);
    }
}
