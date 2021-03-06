/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.sqlite.ObjetivoUCDaoSQL;
import ensino.configuracoes.model.ObjetivoUC;
import ensino.configuracoes.model.ObjetivoUCFactory;
import ensino.configuracoes.model.ObjetivoUCId;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import java.util.List;

/**
 *
 * @author nicho
 */
public class ObjetivoUCController extends AbstractController<ObjetivoUC> {

    public ObjetivoUCController() throws Exception {
        super(DaoFactory.createObjetivoUCDao(), ObjetivoUCFactory.getInstance());
    }

    /**
     * Buscar por id do conteudo
     *
     * @param id Id do conteudo
     * @return
     */
    public ObjetivoUC buscarPor(ObjetivoUCId id) {
        return this.dao.findById(id);
    }

    public List<ObjetivoUC> listar(UnidadeCurricular o) {
        ObjetivoUCDaoSQL d = (ObjetivoUCDaoSQL) this.dao;
        return d.findBy(o);
    }
}
