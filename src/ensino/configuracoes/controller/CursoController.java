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
import ensino.configuracoes.model.Turma;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.patterns.AbstractController;
import ensino.patterns.DaoPattern;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class CursoController extends AbstractController<Curso> {
    
    public CursoController() throws IOException, ParserConfigurationException, TransformerException {
        super(new CursoDaoXML(), CursoFactory.getInstance());
    }
    
    /**
     * Busca um curso pela sua chave primária
     * @param id        Id do curso
     * @param campusId  Id do campus
     * @return 
     */
    public Curso buscarPor(Integer id, Integer campusId) {
        DaoPattern<Curso> cursoDao = super.getDao();
        return cursoDao.findById(id, campusId);
    }
    
    /**
     * Lista os curso de um determinado campus
     * @param campus  Identificação do campus
     * @return 
     */
    public List<Curso> listar(Campus campus) {
        DaoPattern<Curso> cursoDao = super.getDao();
        String filter = String.format("//Curso/curso[@campusId=%d]", campus.getId());
        return cursoDao.list(filter, campus);
    }
    
    @Override
    public Curso salvar(Curso o) throws Exception {
        o = super.salvar(o);
        // salvar cascade
        AbstractController<Turma> turmaCol = new TurmaController();
        turmaCol.salvarEmCascata(o.getTurmas());
        
        AbstractController<UnidadeCurricular> unidadeCurricularCol = new UnidadeCurricularController();
        unidadeCurricularCol.salvarEmCascata(o.getUnidadesCurriculares());

        return o;
    }
    
}
