/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.sqlite.EstudanteDaoSQL;
import ensino.configuracoes.model.Estudante;
import ensino.configuracoes.model.EstudanteFactory;
import ensino.configuracoes.model.EstudanteId;
import ensino.configuracoes.model.Turma;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import ensino.util.types.SituacaoEstudante;
import java.util.List;

/**
 *
 * @author nicho
 */
public class EstudanteController extends AbstractController<Estudante> {

    public EstudanteController() throws Exception {
        super(DaoFactory.createEstudanteDao(), EstudanteFactory.getInstance());
    }

    /**
     * Buscar por id do estudante
     *
     * @param id Sequencia do estudante
     * @param turma Identificação da turma
     * @return
     */
    public Estudante buscarPor(Long id, Turma turma) {
        return this.dao.findById(new EstudanteId(id, turma));
    }

    /**
     * Listar estudantes por turma
     *
     * @param turma Identificacao da turma
     * @return
     */
    public List<Estudante> listar(Turma turma) {
        return this.listar(turma, null, null);
    }

    /**
     * Listar estudantes de acordo com a turma, nome ou situação
     *
     * @param turma
     * @param nome
     * @param situacao
     * @return
     */
    public List<Estudante> listar(Turma turma, String nome, SituacaoEstudante situacao) {
        EstudanteDaoSQL d = (EstudanteDaoSQL) this.dao;
        return d.findBy(turma, nome, situacao);
    }
}
