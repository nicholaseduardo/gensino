/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.sqlite.SemanaLetivaDaoSQL;
import ensino.configuracoes.model.PeriodoLetivo;
import ensino.configuracoes.model.SemanaLetiva;
import ensino.configuracoes.model.SemanaLetivaFactory;
import ensino.configuracoes.model.SemanaLetivaId;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import java.util.List;

/**
 *
 * @author nicho
 */
public class SemanaLetivaController extends AbstractController<SemanaLetiva> {

    public SemanaLetivaController() throws Exception {
        super(DaoFactory.createSemanaLetivaDao(), SemanaLetivaFactory.getInstance());
    }

    /**
     * Busca um periodoLetivo pela sua chave primária
     *
     * @param id Id da semana letiva
     * @param periodoLetivo Instância de um objeto da classe
     * <code>PeriodoLetivo</code>
     * @return
     */
    public SemanaLetiva buscarPor(Long id, PeriodoLetivo periodoLetivo) {
        SemanaLetivaDaoSQL d = (SemanaLetivaDaoSQL) this.dao;
        return d.findById(new SemanaLetivaId(id, periodoLetivo));
    }

    /**
     * Lista os periodoLetivos de um determinado calendario
     *
     * @param o Número do período letivo
     * @return
     */
    public List<SemanaLetiva> listar(PeriodoLetivo o) {
        SemanaLetivaDaoSQL d = (SemanaLetivaDaoSQL) this.dao;
        return d.findBy(o);
    }

}
