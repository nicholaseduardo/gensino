/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.patterns.AbstractController;
import ensino.configuracoes.model.Docente;
import ensino.configuracoes.model.DocenteFactory;
import ensino.patterns.factory.DaoFactory;

/**
 *
 * @author nicho
 */
public class DocenteController extends AbstractController<Docente> {
    
    public DocenteController() throws Exception {
        super(DaoFactory.createDocenteDao(), DocenteFactory.getInstance());
    }
}
