/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.sqlite.TurmaDaoSQL;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.Turma;
import ensino.configuracoes.model.TurmaFactory;
import ensino.configuracoes.model.TurmaId;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import java.util.List;

/**
 *
 * @author nicho
 */
public class TurmaController extends AbstractController<Turma> {

    public TurmaController() throws Exception {
        super(DaoFactory.createTurmaDao(), TurmaFactory.getInstance());
    }

    /**
     * Buscar por id da turma
     *
     * @param id Identificacao da turma
     * @param curso Identificação do curso ao qual a unidade curricular está
     * vinculada
     * @return
     */
    public Turma buscarPor(Long id, Curso curso) {
        return this.dao.findById(new TurmaId(id, curso));
    }

    /**
     * Listar turmas por curso e campus
     *
     * @param curso Identificacao do curso
     * @return
     */
    public List<Turma> listar(Curso curso) {
        TurmaDaoSQL d = (TurmaDaoSQL)this.dao;
        return d.findBy(curso);
    }
}
