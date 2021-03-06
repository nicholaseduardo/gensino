/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.sqlite.PeriodoLetivoDaoSQL;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.PeriodoLetivo;
import ensino.configuracoes.model.PeriodoLetivoFactory;
import ensino.configuracoes.model.PeriodoLetivoId;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import java.util.List;

/**
 *
 * @author nicho
 */
public class PeriodoLetivoController extends AbstractController<PeriodoLetivo> {

    public PeriodoLetivoController() throws Exception {
        super(DaoFactory.createPeriodoLetivoDao(), PeriodoLetivoFactory.getInstance());
    }

    /**
     * Busca um periodoLetivo pela sua chave primária
     *
     * @param numero Numero do periodoLetivo
     * @param calendario Instância de um objeto da classe
     * <code>Calendario</code>
     * @return
     */
    public PeriodoLetivo buscarPor(Long numero, Calendario calendario) {
        PeriodoLetivoDaoSQL d = (PeriodoLetivoDaoSQL)this.dao;
        return d.findById(new PeriodoLetivoId(numero, calendario));
    }

    /**
     * Lista os periodoLetivos de um determinado calendario
     *
     * @param o Identificação do calendário
     * @return
     */
    public List<PeriodoLetivo> listar(Calendario o) {
        PeriodoLetivoDaoSQL d = (PeriodoLetivoDaoSQL)this.dao;
        return d.findBy(o);
    }
    
}
