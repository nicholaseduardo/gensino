/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import ensino.planejamento.model.Diario;
import ensino.planejamento.model.DiarioFrequencia;
import ensino.planejamento.model.DiarioFrequenciaFactory;
import java.util.List;

/**
 *
 * @author nicho
 */
public class DiarioFrequenciaController extends AbstractController<DiarioFrequencia> {
    
    public DiarioFrequenciaController() throws Exception {
        super(DaoFactory.createDiarioFrequenciaDao(), DiarioFrequenciaFactory.getInstance());
    }
    
    /**
     * Listagem de diários por data
     * 
     * @param o         Identificação do diário
     * @return 
     */
    public List<DiarioFrequencia> list(Diario o) {
        String filter = "";
        Integer id = o.getId().getId(),
                planoId = o.getId().getPlanoDeEnsino().getId(),
                undId = o.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getId(),
                cursoId = o.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getCurso().getId().getId(),
                campusId = o.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getCurso().getId().getCampus().getId();
        if (DaoFactory.isXML()) {
            filter = String.format("//DiarioFrequencia/diarioFrequencia"
                + "[@diarioId=%d and @planoDeEnsinoId=%d and "
                + "@unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]", 
                    id, planoId, undId, cursoId, campusId);
        } else {
            filter = String.format(" AND o.id.diario.id = %d ", id);
        }
        
        return super.getDao().list(filter, o);
    }
}
