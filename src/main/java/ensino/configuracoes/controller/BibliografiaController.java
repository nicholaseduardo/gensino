/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.xml.BibliografiaDaoXML;
import ensino.configuracoes.model.Bibliografia;
import ensino.configuracoes.model.BibliografiaFactory;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import java.net.URL;
import java.util.List;

/**
 *
 * @author nicho
 */
public class BibliografiaController extends AbstractController<Bibliografia> {
    
    public BibliografiaController() throws Exception {
        super(DaoFactory.createBibliografiaDao(), BibliografiaFactory.getInstance());
    }
    
    public BibliografiaController(URL url) throws Exception {
        super(new BibliografiaDaoXML(url), BibliografiaFactory.getInstance());
    }
    
    /**
     * Recupera a lista de bibliografia por autor
     * @param autor
     * @return 
     */
    public List<Bibliografia> listarPorAutor(String autor) {
        String filter = "";
        if (DaoFactory.isXML()) {
            filter = String.format("%s[contains(@autor, '%s')]",
                "//Bibliografia/bibliografia", autor);
        } else {
            filter = " AND UPPER(b.autor) LIKE UPPER('"+autor+"') ";
        }
        
        return super.getDao().list(filter);
    }
    
    /**
     * Recupera a lista de bibliografia por titulo
     * @param titulo
     * @return 
     */
    public List<Bibliografia> listarPorTitulo(String titulo) {
        String filter = "";
        if (DaoFactory.isXML()) {
            filter = String.format("%s[contains(@titulo, '%s')]",
                "//Bibliografia/bibliografia", titulo);
        } else {
            filter = " AND UPPER(b.titulo) LIKE UPPER('"+titulo+"') ";
        }
        
        return super.getDao().list(filter);
    }
}
