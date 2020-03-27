/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import ensino.planejamento.dao.ObjetivoDetalheDaoXML;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.ObjetivoDetalhe;
import ensino.planejamento.model.ObjetivoDetalheFactory;
import java.net.URL;
import java.util.List;

/**
 *
 * @author nicho
 */
public class ObjetivoDetalheController extends AbstractController<ObjetivoDetalhe> {
    private static ObjetivoDetalheController instance = null;
    
    private ObjetivoDetalheController() throws Exception {
        super(DaoFactory.createObjetivoDetalheDao(), ObjetivoDetalheFactory.getInstance());
    }
    
    public ObjetivoDetalheController(URL url) throws Exception {
        super(new ObjetivoDetalheDaoXML(url), ObjetivoDetalheFactory.getInstance());
    }
    
    public static ObjetivoDetalheController getInstance() throws Exception {
        if (instance == null)
            instance = new ObjetivoDetalheController();
        return instance;
    }
    
    public List<ObjetivoDetalhe> listar(Detalhamento o) {
        String filter = "";
        Integer id = o.getId().getSequencia(),
                planoId = o.getId().getPlanoDeEnsino().getId(),
                undId = o.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getId(),
                cursoId = o.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getCurso().getId().getId(),
                campusId = o.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getCurso().getId().getCampus().getId();
        if (DaoFactory.isXML()) {
            filter = String.format("//ObjetivoDetalhe/objetivoDetalhe"
                + "[@detalhamentoSequencia=%d and @planoDeEnsinoId=%d and "
                + "@unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]", 
                    id, planoId, undId, cursoId, campusId);
        } else {
            filter = String.format(" AND od.id.objetivo.id.sequencia = %d ", id);
        }
        
        return super.getDao().list(filter, o);
    }
}
