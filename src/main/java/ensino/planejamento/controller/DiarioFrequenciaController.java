/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import ensino.planejamento.dao.DiarioFrequenciaDaoSQL;
import ensino.planejamento.model.Diario;
import ensino.planejamento.model.DiarioFrequencia;
import ensino.planejamento.model.DiarioFrequenciaFactory;
import java.util.List;

/**
 *
 * @author nicho
 */
public class DiarioFrequenciaController extends AbstractController<DiarioFrequencia> {
    
    public DiarioFrequenciaController() throws Exception {
        super(DaoFactory.createDiarioFrequenciaDao(), DiarioFrequenciaFactory.getInstance());
    }
    
    /**
     * Listagem de diários por data
     * 
     * @param o         Identificação do diário
     * @return 
     */
    public List<DiarioFrequencia> list(Diario o) {
        DiarioFrequenciaDaoSQL d = (DiarioFrequenciaDaoSQL) this.dao;
        return d.findBy(o);
    }
}
