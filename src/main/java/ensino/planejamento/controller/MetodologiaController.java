/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import ensino.planejamento.dao.MetodologiaDaoXML;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.Metodologia;
import ensino.planejamento.model.MetodologiaFactory;
import java.net.URL;
import java.util.List;

/**
 *
 * @author nicho
 */
public class MetodologiaController extends AbstractController<Metodologia> {
    
    public MetodologiaController() throws Exception {
        super(DaoFactory.createMetodologiaDao(), MetodologiaFactory.getInstance());
    }
    
    public MetodologiaController(URL url) throws Exception {
        super(new MetodologiaDaoXML(url), MetodologiaFactory.getInstance());
    }
    
    public List<Metodologia> listar(Detalhamento o) {
        String filter = "";
        Integer id = o.getId().getSequencia(),
                planoId = o.getId().getPlanoDeEnsino().getId(),
                undId = o.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getId(),
                cursoId = o.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getCurso().getId().getId(),
                campusId = o.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getCurso().getId().getCampus().getId();
        if (DaoFactory.isXML()) {
            filter = String.format("//Metodologia/metodologia"
                + "[@detalhamentoSequencia=%d and @planoDeEnsinoId=%d and "
                + "@unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]",
                    id, planoId, undId, cursoId, campusId);
        } else {
            filter = String.format(" AND m.id.detalhamento.id.sequencia = %d ", id);
        }
        
        return super.getDao().list(filter, o);
    }
}
