/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.configuracoes.model.SemanaLetiva;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import ensino.planejamento.dao.DetalhamentoDaoSQL;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.DetalhamentoFactory;
import ensino.planejamento.model.PlanoDeEnsino;
import java.util.List;

/**
 *
 * @author nicho
 */
public class DetalhamentoController extends AbstractController<Detalhamento> {

    public DetalhamentoController() throws Exception {
        super(DaoFactory.createDetalhamentoDao(), DetalhamentoFactory.getInstance());
    }
    
    public List<Detalhamento> listar(PlanoDeEnsino o) {
        return listar(o, null);
    }

    public List<Detalhamento> listar(PlanoDeEnsino o, SemanaLetiva sl) {
        DetalhamentoDaoSQL d = (DetalhamentoDaoSQL) this.dao;
        return d.findBy(o, sl);
    }
}
