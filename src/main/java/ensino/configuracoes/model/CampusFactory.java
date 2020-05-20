/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import ensino.patterns.factory.BeanFactory;
import ensino.util.types.StatusCampus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author nicho
 */
public class CampusFactory implements BeanFactory<Campus> {

    private static CampusFactory instance = null;

    private CampusFactory() {

    }

    public static CampusFactory getInstance() {
        if (instance == null) {
            instance = new CampusFactory();
        }
        return instance;
    }

    @Override
    public Campus createObject(Object... args) {
        int i = 0;
        Campus campus = new Campus();
        campus.setId((Integer) args[i++]);
        campus.setNome((String) args[i++]);
        if (args.length > 2)
            campus.setStatus((StatusCampus)args[i]);
        return campus;
    }

    @Override
    public Campus getObject(Element e) {
        Integer id = Integer.parseInt(e.getAttribute("id"));
        Campus campus = createObject(id, e.getAttribute("nome"));
        
        return campus;
    }

    @Override
    public Campus getObject(HashMap<String, Object> p) {
        Campus campus = createObject(p.get("id"), p.get("nome"),
                p.get("status"));
        List<Curso> cursos = (List<Curso>) p.get("cursos");
        if (cursos == null) {
            cursos = new ArrayList();
        }
        campus.setCursos(cursos);
        List<Calendario> calendarios = (List<Calendario>) p.get("calendarios");
        if (calendarios == null) {
            calendarios = new ArrayList();
        }
        campus.setCalendarios(calendarios);
        return campus;
    }

    @Override
    public Node toXml(Document doc, Campus o) {
        Element e = doc.createElement("campus");
        e.setAttribute("id", o.getId().toString());
        e.setAttribute("nome", o.getNome());
        e.setAttribute("status", o.getStatus() != null ? o.getStatus().toString() : "");
        
        return e;
    }
}
