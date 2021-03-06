/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import ensino.planejamento.dao.HorarioAulaDaoSQL;
import ensino.planejamento.model.HorarioAula;
import ensino.planejamento.model.HorarioAulaFactory;
import ensino.planejamento.model.PlanoDeEnsino;
import java.util.List;

/**
 *
 * @author nicho
 */
public class HorarioAulaController extends AbstractController<HorarioAula> {
    
    public HorarioAulaController() throws Exception {
        super(DaoFactory.createHorarioAulaDao(), HorarioAulaFactory.getInstance());
    }
    
    public List<HorarioAula> listar(PlanoDeEnsino o) {
        HorarioAulaDaoSQL d = (HorarioAulaDaoSQL) this.dao;
        return d.findBy(o);
    }
}
