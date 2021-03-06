/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import ensino.planejamento.dao.AvaliacaoDaoSQL;
import ensino.planejamento.model.Avaliacao;
import ensino.planejamento.model.AvaliacaoFactory;
import ensino.planejamento.model.PlanoAvaliacao;
import java.util.List;

/**
 *
 * @author nicho
 */
public class AvaliacaoController extends AbstractController<Avaliacao> {
    
    public AvaliacaoController() throws Exception {
        super(DaoFactory.createAvaliacaoDao(), AvaliacaoFactory.getInstance());
    }
    
    public List<Avaliacao> listar(PlanoAvaliacao o) {
        AvaliacaoDaoSQL d = (AvaliacaoDaoSQL) this.dao;
        return d.findBy(o);
    }
}
