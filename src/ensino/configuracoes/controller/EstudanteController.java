/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.xml.EstudanteDaoXML;
import ensino.configuracoes.model.Estudante;
import ensino.configuracoes.model.EstudanteFactory;
import ensino.configuracoes.model.Turma;
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
public class EstudanteController extends AbstractController<Estudante> {
    
    public EstudanteController() throws IOException, ParserConfigurationException, TransformerException {
        super(new EstudanteDaoXML(), EstudanteFactory.getInstance());
    }
    
    /**
     * Buscar por id do estudante
     * @param id        Sequencia do estudante
     * @param turmaId   Identificação da turma
     * @param cursoId   Identificação do curso ao qual a unidade curricular está vinculada
     * @param campusId  Identificação do campos ao qual pertence o curso
     * @return 
     */
    public Estudante buscarPor(Integer id, Integer turmaId, Integer cursoId, Integer campusId) {
        DaoPattern<Estudante> estudanteDao = super.getDao();
        return estudanteDao.findById(id, turmaId, cursoId, campusId);
    }
    
    /**
     * Listar turmas por curso e campus
     * @param turma   Identificacao da turma
     * @return 
     */
    public List<Estudante> listar(Turma turma) {
        DaoPattern<Estudante> estudanteDao = super.getDao();
        String filter = String.format("//Estudante/estudante[@turmaId=%d and @cursoId=%d and @campusId=%d]", 
                turma.getId(), turma.getCurso().getId(),
                turma.getCurso().getCampus().getId());
        return estudanteDao.list(filter, turma);
    }

    @Override
    public Estudante salvar(Estudante o) throws Exception {
        return super.salvar(o);
    }
}
