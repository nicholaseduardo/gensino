/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import ensino.planejamento.dao.MetodologiaDaoSQL;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.Metodologia;
import ensino.planejamento.model.MetodologiaFactory;
import java.util.List;

/**
 *
 * @author nicho
 */
public class MetodologiaController extends AbstractController<Metodologia> {
    
    public MetodologiaController() throws Exception {
        super(DaoFactory.createMetodologiaDao(), MetodologiaFactory.getInstance());
    }
    
    public List<Metodologia> listar(Detalhamento o) {
        MetodologiaDaoSQL d = (MetodologiaDaoSQL) this.dao;
        return d.findBy(o);
    }
}
