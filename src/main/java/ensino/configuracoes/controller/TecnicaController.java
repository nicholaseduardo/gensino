/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.model.TecnicaFactory;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;

/**
 *
 * @author nicho
 */
public class TecnicaController  extends AbstractController {
    
    public TecnicaController() throws Exception {
        super(DaoFactory.createTecnicaDao(), TecnicaFactory.getInstance());
    }
}
