/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import ensino.planejamento.dao.ObjetivoDaoSQL;
import ensino.planejamento.model.Objetivo;
import ensino.planejamento.model.ObjetivoFactory;
import ensino.planejamento.model.PlanoDeEnsino;
import java.util.List;

/**
 *
 * @author nicho
 */
public class ObjetivoController extends AbstractController<Objetivo> {
    
    public ObjetivoController() throws Exception {
        super(DaoFactory.createObjetivoDao(), ObjetivoFactory.getInstance());
    }
    
    public List<Objetivo> listar(PlanoDeEnsino o) {
        ObjetivoDaoSQL d = (ObjetivoDaoSQL) this.dao;
        return d.findBy(o);
    }
}
