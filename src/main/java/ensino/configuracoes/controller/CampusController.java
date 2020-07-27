/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.xml.CampusDaoXML;
import ensino.patterns.AbstractController;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.CampusFactory;
import ensino.patterns.factory.DaoFactory;
import ensino.util.types.StatusCampus;
import java.net.URL;
import java.util.List;

/**
 *
 * @author nicho
 */
public class CampusController extends AbstractController<Campus> {
    
    public CampusController() throws Exception {
        super(DaoFactory.createCampusDao(), CampusFactory.getInstance());
    }
    
    public CampusController(URL url) throws Exception {
        super(new CampusDaoXML(url), CampusFactory.getInstance());
    }
    
    @Override
    public Campus salvar(Campus o) throws Exception {
        /**
         * Antes de salvar, verifica se já existe um campus vigente.
         * Caso positivo, substitua a vigência dos campi
         */
        if (o.isVigente()) {
            Campus vigente = getCampusVigente();
            if (vigente != null && !vigente.equals(o)) {
                vigente.setStatus(StatusCampus.ANTERIOR);
                super.salvar(vigente);
            }
        }
        o = super.salvar(o);
        
        return o;
    }
    
    @Override
    public Campus remover(Campus o) throws Exception {
        o = super.remover(o);
        return o;
    }
    
    public Campus getCampusVigente() {
        List<Campus> l = super.listar(" AND c.status = 'V' ");
        /**
         * Se encontrar, retorna o campus
         */
        if (!l.isEmpty()) {
            return l.get(0);
        }
        /**
         * Se não encontrar, retorna null
         */
        return null;
    }
}
