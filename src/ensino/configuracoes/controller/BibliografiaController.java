/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.BibliografiaDao;
import ensino.configuracoes.model.Bibliografia;
import ensino.patterns.AbstractController;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class BibliografiaController extends AbstractController {
    
    public BibliografiaController() throws IOException, ParserConfigurationException, TransformerException {
        super(new BibliografiaDao());
    }

    @Override
    public Object salvar(HashMap<String, Object> params) throws TransformerException {
        return super.salvar(new Bibliografia(params));
    }

    @Override
    public Object remover(HashMap<String, Object> params) throws TransformerException {
        return super.remover(new Bibliografia(params));
    }
    
    @Override
    public List<Bibliografia> listar() {
        BibliografiaDao bDao = (BibliografiaDao) getDao();
        return bDao.list("");
    }
    
    /**
     * Recupera a lista de bibliografia por autor
     * @param autor
     * @return 
     */
    public List<Bibliografia> listarPorAutor(String autor) {
        BibliografiaDao bDao = (BibliografiaDao) getDao();
        return bDao.listByAutor(autor);
    }
    
    /**
     * Recupera a lista de bibliografia por titulo
     * @param titulo
     * @return 
     */
    public List<Bibliografia> listarPorTitulo(String titulo) {
        BibliografiaDao bDao = (BibliografiaDao) getDao();
        return bDao.listByTitulo(titulo);
    }
}
