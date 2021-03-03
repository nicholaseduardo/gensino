/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import ensino.planejamento.model.AtendimentoEstudante;
import ensino.planejamento.model.AtendimentoEstudanteFactory;
import ensino.planejamento.model.PermanenciaEstudantil;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nicho
 */
public class AtendimentoEstudanteController extends AbstractController<AtendimentoEstudante> {
    private static AtendimentoEstudanteController instance = null;
    
    private AtendimentoEstudanteController() throws Exception {
        super(DaoFactory.createAtendimentoEstudanteDao(), AtendimentoEstudanteFactory.getInstance());
    }
    
    public static AtendimentoEstudanteController getInstance() throws Exception {
        if (instance == null)
            instance = new AtendimentoEstudanteController();
        return instance;
    }
    
    @Override
    public AtendimentoEstudante salvar(AtendimentoEstudante o) throws Exception {
        o = super.salvar(o);
        
        return o;
    }
    
    /**
     *
     * @param o
     * @return
     */
    @Override
    public AtendimentoEstudante salvarSemCommit(AtendimentoEstudante o) {
        try {
            o = super.salvarSemCommit(o);
            
            return o;
        } catch (Exception ex) {
            Logger.getLogger(AtendimentoEstudanteController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public List<AtendimentoEstudante> listar(PermanenciaEstudantil o) {
        String filter;
        Integer id = o.getId().getSequencia();
        if (DaoFactory.isXML()) {
            filter = "";
        } else {
            filter = String.format(" AND pe.id.planoDeEnsino.id = %d ", id);
        }
        
        return super.getDao().list(filter, o);
    }
}
