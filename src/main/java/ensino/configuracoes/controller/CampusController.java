/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.xml.CampusDaoXML;
import ensino.configuracoes.model.Calendario;
import ensino.patterns.AbstractController;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.CampusFactory;
import ensino.configuracoes.model.Curso;
import ensino.patterns.factory.ControllerFactory;
import ensino.patterns.factory.DaoFactory;
import java.net.URL;

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
        o = super.salvar(o);
        // Salvar cascade
        AbstractController<Calendario> colCalendario = ControllerFactory.createCalendarioController();
        colCalendario.salvarEmCascata(o.getCalendarios());
        
        AbstractController<Curso> colCurso = ControllerFactory.createCursoController();
        colCurso.salvarEmCascata(o.getCursos());
        return o;
    }
    
    @Override
    public Campus remover(Campus o) throws Exception {
        o = super.remover(o);
        return o;
    }
}
