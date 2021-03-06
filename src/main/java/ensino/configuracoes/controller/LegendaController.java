/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.model.Legenda;
import ensino.configuracoes.model.LegendaFactory;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;

/**
 *
 * @author nicho
 */
public class LegendaController extends AbstractController<Legenda> {
    
    public LegendaController() throws Exception {
        super(DaoFactory.createLegendaDao(), LegendaFactory.getInstance());
    }
}
