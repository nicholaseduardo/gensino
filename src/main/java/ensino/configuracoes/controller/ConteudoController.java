/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.sqlite.ConteudoDaoSQL;
import ensino.configuracoes.model.Conteudo;
import ensino.configuracoes.model.ConteudoFactory;
import ensino.configuracoes.model.ConteudoId;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import java.util.List;

/**
 *
 * @author nicho
 */
public class ConteudoController extends AbstractController<Conteudo> {

    public ConteudoController() throws Exception {
        super(DaoFactory.createConteudoDao(), ConteudoFactory.getInstance());
    }

    /**
     * Buscar por id do conteudo
     *
     * @param id Id do conteudo
     * @return
     */
    public Conteudo buscarPor(ConteudoId id) {
        return this.dao.findById(id);
    }

    public List<Conteudo> listar(UnidadeCurricular o) {
        return listar(o, null);
    }

    public List<Conteudo> listar(UnidadeCurricular o, String descricao) {
        ConteudoDaoSQL d = (ConteudoDaoSQL) this.dao;
        return d.findBy(o, descricao);
    }
}
