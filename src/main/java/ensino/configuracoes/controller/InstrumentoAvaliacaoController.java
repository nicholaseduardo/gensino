/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.model.InstrumentoAvaliacaoFactory;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;

/**
 *
 * @author nicho
 */
public class InstrumentoAvaliacaoController  extends AbstractController {
    
    public InstrumentoAvaliacaoController() throws Exception {
        super(DaoFactory.createInstrumentoAvaliacaoDao(), InstrumentoAvaliacaoFactory.getInstance());
    }
}
