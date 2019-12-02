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
import ensino.patterns.DaoPattern;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class BibliografiaController extends AbstractController<Bibliografia> {
    
    public BibliografiaController() throws IOException, ParserConfigurationException, TransformerException {
        super(new BibliografiaDaoXML(), BibliografiaFactory.getInstance());
    }
    
    /**
     * Recupera a lista de bibliografia por autor
     * @param autor
     * @return 
     */
    public List<Bibliografia> listarPorAutor(String autor) {
        DaoPattern<Bibliografia> dao = getDao();
        String filter = String.format("%s[contains(@autor, '%s')]",
                "//Bibliografia/bibliografia", autor);
        return dao.list(filter);
    }
    
    /**
     * Recupera a lista de bibliografia por titulo
     * @param titulo
     * @return 
     */
    public List<Bibliografia> listarPorTitulo(String titulo) {
        DaoPattern<Bibliografia> dao = getDao();
        String filter = String.format("%s[contains(@titulo, '%s')]",
                "//Bibliografia/bibliografia", titulo);
        return dao.list(filter);
    }
}
