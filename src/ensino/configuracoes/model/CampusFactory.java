/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import ensino.patterns.factory.BeanFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
    public Campus getObject(Object... args) {
        Campus campus = new Campus();
        campus.setId((Integer) args[0]);
        campus.setNome((String) args[1]);
        return campus;
    }

    @Override
    public Campus getObject(Element e) {
        Integer id = Integer.parseInt(e.getAttribute("id"));
        Campus campus = getObject(id, e.getAttribute("nome"));
        if (e.hasChildNodes()) {
            NodeList nodeList = e.getChildNodes();
            for (int j = 0; j < nodeList.getLength(); j++) {
                Node child = nodeList.item(j);
                if ("curso".equals(child.getNodeName())) {
                    Curso curso = new Curso((Element) child);
                    curso.setCampus(campus);
                    campus.addCurso(curso);
                } else if ("calendario".equals(child.getNodeName())) {
                    Calendario calendario = new Calendario((Element) child);
                    calendario.setCampus(campus);
                    campus.addCalendario(calendario);
                }
            }
        }
        return campus;
    }

    @Override
    public Campus getObject(HashMap<String, Object> p) {
        Campus campus = getObject(p.get("id"), p.get("nome"));
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
        // Adiciona os cursos vinculados ao campus
        o.getCursos().forEach((curso) -> {
            e.appendChild(curso.toXml(doc));
        });
        // Adiciona os calendarios
        o.getCalendarios().forEach((cal) -> {
            e.appendChild(cal.toXml(doc));
        });
        return e;
    }
}
