/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.xml.CursoDaoXML;
import ensino.configuracoes.model.Curso;
import ensino.patterns.AbstractController;
import ensino.patterns.DaoPattern;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class CursoController extends AbstractController<Curso> {
    
    public CursoController() throws IOException, ParserConfigurationException, TransformerException {
        super(new CursoDaoXML());
    }
    
    /**
     * Busca um curso pela sua chave primária
     * @param id        Id do curso
     * @param campusId  Id do campus
     * @return 
     */
    public Curso buscarPor(Integer id, Integer campusId) {
        CursoDaoXML cursoDao = (CursoDaoXML) super.getDao();
        return cursoDao.findById(id, campusId);
    }
    
    /**
     * Lista os curso de um determinado campus
     * @param campusId  Identificação do campus
     * @return 
     */
    public List<Curso> listar(Integer campusId) {
        DaoPattern<Curso> cursoDao = super.getDao();
        String filter = String.format("//Curso/curso[@campus=%d]", campusId);
        return cursoDao.list(filter);
    }
    
}
