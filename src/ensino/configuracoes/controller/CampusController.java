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
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class CampusController extends AbstractController<Campus> {
    
    private static CampusController instance = null;
    
    private CampusController() throws IOException, ParserConfigurationException, TransformerException {
        super(CampusDaoXML.getInstance(), CampusFactory.getInstance());
    }
    
    public static CampusController getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null) 
            instance = new CampusController();
        return instance;
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
}
