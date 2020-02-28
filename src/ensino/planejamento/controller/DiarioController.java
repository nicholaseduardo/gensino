/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.patterns.DaoPattern;
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.dao.DiarioDaoXML;
import ensino.planejamento.model.Diario;
import ensino.planejamento.model.DiarioFactory;
import ensino.planejamento.model.DiarioFrequencia;
import ensino.planejamento.model.PlanoDeEnsino;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class DiarioController extends AbstractController<Diario> {
    private static DiarioController instance = null;
    
    private DiarioController() throws IOException, ParserConfigurationException, TransformerException {
        super(DiarioDaoXML.getInstance(), DiarioFactory.getInstance());
    }
    
    public static DiarioController getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null)
            instance = new DiarioController();
        return instance;
    }
    
    @Override
    public Diario salvar(Diario o) throws Exception {
        o = super.salvar(o);
        // Salvar cascade
        AbstractController<DiarioFrequencia> colDiarioFrequencia = ControllerFactory.createDiarioFrequenciaController();
        colDiarioFrequencia.salvarEmCascata(o.getFrequencias());
        
        return o;
    }
    
    /**
     *
     * @param o
     * @return
     */
    @Override
    public Diario salvarSemCommit(Diario o) {
        try {
            o = super.salvarSemCommit(o);
            // Salvar cascade
            AbstractController<DiarioFrequencia> colDiarioFrequencia = ControllerFactory.createDiarioFrequenciaController();
            colDiarioFrequencia.salvarEmCascataSemCommit(o.getFrequencias());
            
            return o;
        } catch (Exception ex) {
            Logger.getLogger(DiarioController.class.getName()).log(Level.SEVERE, null, ex);
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
    public Diario buscarPor(Integer id, Integer planoId,
            Integer unidadeCurricularId, Integer cursoId, Integer campusId) throws ParseException {
        DaoPattern<Diario> dao = super.getDao();
        return dao.findById(id, planoId, unidadeCurricularId,
                cursoId, campusId);
    }
    
    /**
     * Listagem de diários por data
     * 
     * @param data                  Data a ser procurada
     * @param planoDeEnsino         Identificação do plano de ensino
     * @return 
     */
    public List<Diario> list(Date data, PlanoDeEnsino planoDeEnsino) {
        DiarioDaoXML diarioDao = (DiarioDaoXML)super.getDao();
        return diarioDao.list(data, planoDeEnsino);
    }
    
    public List<Diario> listar(PlanoDeEnsino o) {
        DaoPattern<Diario> dao = super.getDao();
        String filter = String.format("//Diario/diario[@planoDeEnsinoId=%d and "
                + "@unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]", 
                o.getId(),
                o.getUnidadeCurricular().getId(),
                o.getUnidadeCurricular().getCurso().getId(),
                o.getUnidadeCurricular().getCurso().getCampus().getId());
        return dao.list(filter, o);
    }
}
