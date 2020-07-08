/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

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
        String filter = "";
        Integer id = o.getId().getSequencia();
        if (!DaoFactory.isXML()) {
            filter = String.format(" AND oc.id.objetivo.id.sequencia = %d ", id);
        }
        
        return super.getDao().list(filter, o);
    }
    
    public List<ObjetivoUCConteudo> listar(Conteudo o) {
        String filter = "";
        Integer id = o.getId().getId();
        if (!DaoFactory.isXML()) {
            filter = String.format(" AND oc.id.conteudo.id.id = %d ", id);
        }
        
        return super.getDao().list(filter, o);
    }
}
