/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import ensino.planejamento.dao.DiarioDaoSQL;
import ensino.planejamento.model.Diario;
import ensino.planejamento.model.DiarioFactory;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.util.types.TipoAula;
import java.util.Date;
import java.util.List;

/**
 *
 * @author nicho
 */
public class DiarioController extends AbstractController<Diario> {

    public DiarioController() throws Exception {
        super(DaoFactory.createDiarioDao(), DiarioFactory.getInstance());
    }

    /**
     * Listagem de diários por data
     *
     * @param data Data a ser procurada
     * @param planoDeEnsino Identificação do plano de ensino
     * @return
     */
    public List<Diario> list(Date data, PlanoDeEnsino planoDeEnsino) {
        return this.listar(planoDeEnsino, data, null);
    }

    public List<Diario> listar(PlanoDeEnsino o, Date data, TipoAula tipo) {
        DiarioDaoSQL dao = (DiarioDaoSQL) this.dao;

        return dao.findBy(o, data, tipo);
    }
}
