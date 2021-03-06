/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import ensino.planejamento.dao.PermanenciaEstudantilDaoSQL;
import ensino.planejamento.model.PermanenciaEstudantil;
import ensino.planejamento.model.PermanenciaEstudantilFatory;
import ensino.planejamento.model.PermanenciaEstudantilId;
import ensino.planejamento.model.PlanoDeEnsino;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author nicho
 */
public class PermanenciaEstudantilController extends AbstractController<PermanenciaEstudantil> {
    
    public PermanenciaEstudantilController() throws Exception {
        super(DaoFactory.createPermanenciaEstudantilDao(), PermanenciaEstudantilFatory.getInstance());
    }
    
    /**
     * Buscar por sequência do diario
     * @param id                    Identificação do diário
     * @param planoDeEnsino               Identificação do plano de ensino
     * @return 
     * @throws java.text.ParseException 
     */
    public PermanenciaEstudantil buscarPor(Long id, PlanoDeEnsino planoDeEnsino) throws ParseException {
        return this.dao.findById(new PermanenciaEstudantilId(id, planoDeEnsino));
    }
    
    public List<PermanenciaEstudantil> listar(PlanoDeEnsino o) {
        return this.listar(o, null);
    }
    
    public List<PermanenciaEstudantil> listar(PlanoDeEnsino o, Date data) {
        PermanenciaEstudantilDaoSQL d = (PermanenciaEstudantilDaoSQL) this.dao;
        return d.findBy(o, data);
    }
}
