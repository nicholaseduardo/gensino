/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.TurmaDao;
import ensino.configuracoes.dao.UnidadeCurricularDao;
import ensino.configuracoes.model.Turma;
import ensino.configuracoes.model.UnidadeCurricular;
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
public class TurmaController extends AbstractController {
    
    public TurmaController() throws IOException, ParserConfigurationException, TransformerException {
        super(new TurmaDao());
    }

    @Override
    public Object salvar(HashMap<String, Object> params) throws TransformerException {
        return super.salvar(new Turma(params));
    }

    @Override
    public Object remover(HashMap<String, Object> params) throws TransformerException {
        return super.remover(new Turma(params));
    }
    
    @Override
    public List<Turma> listar() {
        TurmaDao d = (TurmaDao) getDao();
        return d.list("");
    }
    
    /**
     * Listar turmas por curso e campus
     * @param cursoId   Identificacao do curso
     * @param campusId  Identificacao do campus
     * @return 
     */
    public List<Turma> listar(Integer cursoId, Integer campusId) {
        TurmaDao turmaDao = (TurmaDao) super.getDao();
        return turmaDao.list(cursoId, campusId);
    }
    
    /**
     * Buscar por id da turma
     * @param id        Identificacao da turma
     * @param cursoId   Identificação do curso ao qual a unidade curricular está vinculada
     * @param campusId  Identificação do campos ao qual o curso está vinculado
     * @return 
     */
    public Turma buscarPor(Integer id, Integer cursoId, Integer campusId) {
        TurmaDao turma = (TurmaDao)super.getDao();
        return turma.findById(id, cursoId, campusId);
    }
}
