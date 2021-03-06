/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.sqlite.ObjetivoUCConteudoDaoSQL;
import ensino.configuracoes.model.Conteudo;
import ensino.configuracoes.model.ObjetivoUC;
import ensino.configuracoes.model.ObjetivoUCConteudo;
import ensino.configuracoes.model.ObjetivoUCConteudoFactory;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import java.util.List;

/**
 *
 * @author nicho
 */
public class ObjetivoUCConteudoController extends AbstractController<ObjetivoUCConteudo> {
    
    public ObjetivoUCConteudoController() throws Exception {
        super(DaoFactory.createObjetivoUCConteudoDao(), ObjetivoUCConteudoFactory.getInstance());
    }
    
    public List<ObjetivoUCConteudo> listar(ObjetivoUC o) {
        ObjetivoUCConteudoDaoSQL d = (ObjetivoUCConteudoDaoSQL) this.dao;
        return d.findBy(o, null);
    }
    
    public List<ObjetivoUCConteudo> listar(Conteudo o) {
        ObjetivoUCConteudoDaoSQL d = (ObjetivoUCConteudoDaoSQL) this.dao;
        return d.findBy(null, o);
    }
}
