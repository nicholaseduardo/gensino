/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.xml.CursoDaoXML;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.CursoFactory;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import java.net.URL;
import java.util.List;

/**
 *
 * @author nicho
 */
public class CursoController extends AbstractController<Curso> {
    
    public CursoController() throws Exception {
        super(DaoFactory.createCursoDao(), CursoFactory.getInstance());
    }
    
    public CursoController(URL url) throws Exception {
        super(new CursoDaoXML(url), CursoFactory.getInstance());
    }
    
    /**
     * Busca um curso pela sua chave primária
     * @param id        Id do curso
     * @param campus  Instância da classe <code>Campus</code>
     * @return 
     */
    public Curso buscarPor(Integer id, Campus campus) {
        return super.getDao().findById(id, campus);
    }
    
    /**
     * Lista os curso de um determinado campus
     * @param campus  Identificação do campus
     * @return 
     */
    public List<Curso> listar(Campus campus) {
        String filter = "";
        Integer id = campus.getId();
        if (DaoFactory.isXML()) {
            filter = String.format("//Curso/curso[@campusId=%d]", campus.getId());
        } else {
            filter = String.format(" AND c.id.campus.id = %d ", id);
        }
        
        return super.getDao().list(filter, campus);
    }
    
    public List<Curso> listar(Campus campus, String text) {
        String filter = "";
        Integer id = campus.getId();
        
        filter = String.format(" AND c.id.campus.id = %d ", id);
        if (text != null && !"".equals(text)) {
            filter += " AND UPPER(c.nome) LIKE UPPER('%"+text+"%') ";
        }
        
        return super.getDao().list(filter, campus);
        
    }
    
    @Override
    public Curso salvar(Curso o) throws Exception {
        o = super.salvar(o);
        return o;
    }
    
}
