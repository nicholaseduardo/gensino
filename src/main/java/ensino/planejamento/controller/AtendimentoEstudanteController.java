/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import ensino.planejamento.dao.AtendimentoEstudanteDaoSQL;
import ensino.planejamento.model.AtendimentoEstudante;
import ensino.planejamento.model.AtendimentoEstudanteFactory;
import ensino.planejamento.model.PermanenciaEstudantil;
import java.util.List;

/**
 *
 * @author nicho
 */
public class AtendimentoEstudanteController extends AbstractController<AtendimentoEstudante> {
    public AtendimentoEstudanteController() throws Exception {
        super(DaoFactory.createAtendimentoEstudanteDao(), AtendimentoEstudanteFactory.getInstance());
    }
    
    public List<AtendimentoEstudante> listar(PermanenciaEstudantil o) {
        AtendimentoEstudanteDaoSQL d = (AtendimentoEstudanteDaoSQL) this.dao;
        return d.findBy(o);
    }
}
