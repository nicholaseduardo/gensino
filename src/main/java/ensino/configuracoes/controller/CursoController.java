/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.sqlite.CursoDaoSQL;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.CursoFactory;
import ensino.configuracoes.model.CursoId;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import java.util.List;

/**
 *
 * @author nicho
 */
public class CursoController extends AbstractController<Curso> {
    
    public CursoController() throws Exception {
        super(DaoFactory.createCursoDao(), CursoFactory.getInstance());
    }
    
    /**
     * Busca um curso pela sua chave primária
     * @param id        Id do curso
     * @param campus  Instância da classe <code>Campus</code>
     * @return 
     */
    public Curso buscarPor(Long id, Campus campus) {
        return this.dao.findById(new CursoId(id, campus));
    }
    
    /**
     * Lista os curso de um determinado campus
     * @param campus  Identificação do campus
     * @return 
     */
    public List<Curso> listar(Campus campus) {
        CursoDaoSQL d = (CursoDaoSQL) this.dao;
        return d.findBy(campus, null);
    }
    
    public List<Curso> listar(Campus campus, String text) {
        CursoDaoSQL d = (CursoDaoSQL) this.dao;
        return d.findBy(campus, text);
    }
    
    @Override
    public Curso salvar(Curso o) throws Exception {
        o = super.salvar(o);
        return o;
    }
    
}
