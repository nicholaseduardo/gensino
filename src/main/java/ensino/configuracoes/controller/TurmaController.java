/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.xml.TurmaDaoXML;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.Turma;
import ensino.configuracoes.model.TurmaFactory;
import ensino.patterns.AbstractController;
import ensino.patterns.DaoPattern;
import ensino.patterns.factory.DaoFactory;
import java.net.URL;
import java.util.List;

/**
 *
 * @author nicho
 */
public class TurmaController extends AbstractController<Turma> {

    public TurmaController() throws Exception {
        super(DaoFactory.createTurmaDao(), TurmaFactory.getInstance());
    }

    public TurmaController(URL url) throws Exception {
        super(new TurmaDaoXML(url), TurmaFactory.getInstance());
    }

    /**
     * Buscar por id da turma
     *
     * @param id Identificacao da turma
     * @param curso Identificação do curso ao qual a unidade curricular está
     * vinculada
     * @return
     */
    public Turma buscarPor(Integer id, Curso curso) {
        DaoPattern<Turma> turmaDao = super.getDao();
        return turmaDao.findById(id, curso);
    }

    /**
     * Listar turmas por curso e campus
     *
     * @param curso Identificacao do curso
     * @return
     */
    public List<Turma> listar(Curso curso) {
        String filter = "";
        Integer id = curso.getId().getId(),
                campusId = curso.getId().getCampus().getId();
        if (DaoFactory.isXML()) {
            filter = String.format("//Turma/turma[@cursoId=%d and @campusId=%d]",
                id, campusId);
        } else {
            filter = String.format(" AND t.id.curso.id.id = %d "
                    + " AND t.id.curso.id.campus.id = %d ", id, campusId);
        }
        
        return super.getDao().list(filter, curso);
    }

    @Override
    public Turma salvar(Turma o) throws Exception {
        startTransaction();
        o = super.salvarSemCommit(o);
        commit();
        return o;
    }
}
