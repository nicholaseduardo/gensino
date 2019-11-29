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
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class CampusController extends AbstractController<Campus> {
        
    public CampusController() throws IOException, ParserConfigurationException, TransformerException {
        super(new CampusDaoXML(), CampusFactory.getInstance());
    }
    
    @Override
    public Campus salvar(Campus o) throws Exception {
        o = super.salvar(o);
        // Salvar cascade
        AbstractController<Calendario> colCalendario = new CalendarioController();
        colCalendario.salvarEmCascata(o.getCalendarios());
        
        AbstractController<Curso> colCurso = new CursoController();
        colCurso.salvarEmCascata(o.getCursos());
        return o;
    }
}
