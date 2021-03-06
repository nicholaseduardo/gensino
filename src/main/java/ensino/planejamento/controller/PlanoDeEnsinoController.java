/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.configuracoes.model.UnidadeCurricular;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import ensino.planejamento.dao.PlanoDeEnsinoDaoSQL;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.model.PlanoDeEnsinoFactory;
import java.util.List;

/**
 *
 * @author nicho
 */
public class PlanoDeEnsinoController extends AbstractController<PlanoDeEnsino> {
    
    public PlanoDeEnsinoController() throws Exception {
        super(DaoFactory.createPlanoDeEnsinoDao(), PlanoDeEnsinoFactory.getInstance());
    }
    
    /**
     * Listagem de planos de ensino
     * 
     * @param o Identificação da unidade curricular
     * @return 
     */
    public List<PlanoDeEnsino> listar(UnidadeCurricular o) {
        PlanoDeEnsinoDaoSQL dao = (PlanoDeEnsinoDaoSQL) this.dao;
        
        return dao.findBy(o);
    }
}
