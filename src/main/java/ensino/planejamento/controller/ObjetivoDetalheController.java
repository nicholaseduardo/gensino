/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import ensino.planejamento.dao.ObjetivoDetalheDaoSQL;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.ObjetivoDetalhe;
import ensino.planejamento.model.ObjetivoDetalheFactory;
import java.util.List;

/**
 *
 * @author nicho
 */
public class ObjetivoDetalheController extends AbstractController<ObjetivoDetalhe> {
    
    public ObjetivoDetalheController() throws Exception {
        super(DaoFactory.createObjetivoDetalheDao(), ObjetivoDetalheFactory.getInstance());
    }
    
    public List<ObjetivoDetalhe> listar(Detalhamento o) {
        ObjetivoDetalheDaoSQL d = (ObjetivoDetalheDaoSQL) this.dao;
        return d.findBy(o);
    }
}
