/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.patterns.DaoPattern;
import ensino.patterns.factory.DaoFactory;
import ensino.planejamento.model.PermanenciaEstudantil;
import ensino.planejamento.model.PermanenciaEstudantilFatory;
import ensino.planejamento.model.PlanoDeEnsino;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nicho
 */
public class PermanenciaEstudantilController extends AbstractController<PermanenciaEstudantil> {
    private static PermanenciaEstudantilController instance = null;
    
    private PermanenciaEstudantilController() throws Exception {
        super(DaoFactory.createPermanenciaEstudantilDao(), PermanenciaEstudantilFatory.getInstance());
    }
    
    public static PermanenciaEstudantilController getInstance() throws Exception {
        if (instance == null)
            instance = new PermanenciaEstudantilController();
        return instance;
    }
    
    @Override
    public PermanenciaEstudantil salvar(PermanenciaEstudantil o) throws Exception {
        o = super.salvar(o);
        
        return o;
    }
    
    /**
     *
     * @param o
     * @return
     */
    @Override
    public PermanenciaEstudantil salvarSemCommit(PermanenciaEstudantil o) {
        try {
            o = super.salvarSemCommit(o);
            
            return o;
        } catch (Exception ex) {
            Logger.getLogger(PermanenciaEstudantilController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Buscar por sequência do diario
     * @param id                    Identificação do diário
     * @param planoId               Identificação do plano de ensino
     * @param unidadeCurricularId   Identificação da unidade curricular
     * @param cursoId               Identificação do curso
     * @param campusId              Identificação do campus
     * @return 
     * @throws java.text.ParseException 
     */
    public PermanenciaEstudantil buscarPor(Integer id, Integer planoId,
            Integer unidadeCurricularId, Integer cursoId, Integer campusId) throws ParseException {
        DaoPattern<PermanenciaEstudantil> dao = super.getDao();
        return dao.findById(id, planoId, unidadeCurricularId,
                cursoId, campusId);
    }
    
    public List<PermanenciaEstudantil> listar(PlanoDeEnsino o) {
        String filter;
        Integer id = o.getId();
        if (DaoFactory.isXML()) {
            filter = "";
        } else {
            filter = String.format(" AND pe.id.planoDeEnsino.id = %d ", id);
        }
        
        return super.getDao().list(filter, o);
    }
}
