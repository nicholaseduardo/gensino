/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.sqlite.ReferenciaBibliograficaDaoSQL;
import ensino.configuracoes.model.Bibliografia;
import ensino.configuracoes.model.ReferenciaBibliografica;
import ensino.configuracoes.model.ReferenciaBibliograficaFactory;
import ensino.configuracoes.model.ReferenciaBibliograficaId;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import java.util.List;

/**
 *
 * @author nicho
 */
public class ReferenciaBibliograficaController extends AbstractController<ReferenciaBibliografica> {
    
    public ReferenciaBibliograficaController() throws Exception {
        super(DaoFactory.createReferenciaBibliograficaDao(), ReferenciaBibliograficaFactory.getInstance());
    }
    
    /**
     * Buscar por id da atividade
     * @param sequencia     Sequencia da Referencia Bibliografica
     * @param unidadeCurricular     Identificação da unidade curricular
     * @param bibliografia          Bibliografia de referência
     * @return 
     */
    public ReferenciaBibliografica buscarPor(Long sequencia, 
            UnidadeCurricular unidadeCurricular, Bibliografia bibliografia) {
        return dao.findById(new ReferenciaBibliograficaId(sequencia, unidadeCurricular, bibliografia));
    }
    
    public List<ReferenciaBibliografica> listar(UnidadeCurricular o) {
        return this.listar(o, null);
    }
    
    public List<ReferenciaBibliografica> listar(UnidadeCurricular o, Integer tipoReferencia) {
        ReferenciaBibliograficaDaoSQL d = (ReferenciaBibliograficaDaoSQL) this.dao;
        return d.findBy(o, tipoReferencia);
    }
}
