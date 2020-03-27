/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import ensino.planejamento.dao.DetalhamentoDaoXML;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.DetalhamentoFactory;
import ensino.planejamento.model.PlanoDeEnsino;
import java.net.URL;
import java.util.List;

/**
 *
 * @author nicho
 */
public class DetalhamentoController extends AbstractController<Detalhamento> {
    
    public DetalhamentoController() throws Exception {
        super(DaoFactory.createDetalhamentoDao(), DetalhamentoFactory.getInstance());
    }
    
    public DetalhamentoController(URL url) throws Exception {
        super(new DetalhamentoDaoXML(url), DetalhamentoFactory.getInstance());
    }
    
    @Override
    public Detalhamento salvar(Detalhamento o) throws Exception {
        o = super.salvar(o);
        
        return o;
    }
    
    public List<Detalhamento> listar(PlanoDeEnsino o) {        
        String filter = "";
        Integer id = o.getId(),
                undId = o.getUnidadeCurricular().getId().getId(),
                cursoId = o.getUnidadeCurricular().getId().getCurso().getId().getId(),
                campusId = o.getUnidadeCurricular().getId().getCurso().getId().getCampus().getId();
        if (DaoFactory.isXML()) {
            filter = String.format("//Detalhamento/detalhamento[@planoDeEnsinoId=%d and "
                + "@unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]", 
                    id, undId, cursoId, campusId);
        } else {
            filter = String.format(" AND d.id.planoDeEnsino.id = %d ", id);
        }
        
        return super.getDao().list(filter, o);
    }
}
