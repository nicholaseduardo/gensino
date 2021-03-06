/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.sqlite.CalendarioDaoSQL;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.CalendarioFactory;
import ensino.configuracoes.model.CalendarioId;
import ensino.configuracoes.model.Campus;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import java.util.List;

/**
 *
 * @author nicho
 */
public class CalendarioController extends AbstractController<Calendario> {

    public CalendarioController() throws Exception {
        super(DaoFactory.createCalendarioDao(), CalendarioFactory.getInstance());
    }

    /**
     * Busca um calendario pela sua chave primária
     *
     * @param ano ano do calendario
     * @param campus Instância de um objeto da classe <code>Campus</code>
     * @return
     */
    public Calendario buscarPor(Integer ano, Campus campus) {
        return this.dao.findById(new CalendarioId(ano, campus));
    }

    /**
     * Lista os calendarios de um determinado campus
     *
     * @param campus Instância de um objeto da classe <code>Campus</code>
     * @return
     */
    public List<Calendario> listar(Campus campus) {
        CalendarioDaoSQL d = (CalendarioDaoSQL)this.dao;
        return d.findBy(campus);
    }
    
}
