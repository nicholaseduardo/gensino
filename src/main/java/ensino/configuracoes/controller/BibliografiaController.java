/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.sqlite.BibliografiaDaoSQL;
import ensino.configuracoes.model.Bibliografia;
import ensino.configuracoes.model.BibliografiaFactory;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import java.util.List;

/**
 *
 * @author nicho
 */
public class BibliografiaController extends AbstractController<Bibliografia> {
    
    public BibliografiaController() throws Exception {
        super(DaoFactory.createBibliografiaDao(), BibliografiaFactory.getInstance());
    }
    
    /**
     * Recupera a lista de bibliografia por autor
     * @param autor
     * @return 
     */
    public List<Bibliografia> listarPorAutor(String autor) {
        BibliografiaDaoSQL d = (BibliografiaDaoSQL) this.dao;
        return d.findBy(null, autor);
    }
    
    /**
     * Recupera a lista de bibliografia por titulo
     * @param titulo
     * @return 
     */
    public List<Bibliografia> listarPorTitulo(String titulo) {
        BibliografiaDaoSQL d = (BibliografiaDaoSQL) this.dao;
        return d.findBy(titulo, null);
    }
}
