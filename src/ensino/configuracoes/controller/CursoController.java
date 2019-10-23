/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.CursoDao;
import ensino.configuracoes.model.Curso;
import ensino.patterns.AbstractController;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class CursoController extends AbstractController {
    
    public CursoController() throws IOException, ParserConfigurationException, TransformerException {
        super(new CursoDao());
    }

    @Override
    public Object salvar(HashMap<String, Object> params) throws TransformerException {
        return super.salvar(new Curso(params));
    }

    @Override
    public Object remover(HashMap<String, Object> params) throws TransformerException {
        return super.remover(new Curso(params));
    }
    
    /**
     * Busca um curso pela sua chave primária
     * @param id        Id do curso
     * @param campusId  Id do campus
     * @return 
     */
    public Curso buscarPor(Integer id, Integer campusId) {
        CursoDao cursoDao = (CursoDao) super.getDao();
        return cursoDao.findById(id, campusId);
    }
    
    @Override
    public List<Curso> listar() {
        return (List<Curso>) super.getDao().list();
    }
    
    /**
     * Lista os curso de um determinado campus
     * @param campusId  Identificação do campus
     * @return 
     */
    public List<Curso> listar(Integer campusId) {
        CursoDao cursoDao = (CursoDao) super.getDao();
        return cursoDao.list(campusId);
    }
    
}
