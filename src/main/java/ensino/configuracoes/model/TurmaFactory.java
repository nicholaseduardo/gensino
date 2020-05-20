/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import ensino.patterns.factory.BeanFactory;
import java.util.HashMap;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author nicho
 */
public class TurmaFactory implements BeanFactory<Turma> {

    private static TurmaFactory instance = null;

    private TurmaFactory() {
    }

    public static TurmaFactory getInstance() {
        if (instance == null) {
            instance = new TurmaFactory();
        }
        return instance;
    }

    @Override
    public Turma createObject(Object... args) {
        Turma o = new Turma();
        int i = 0;
        o.getId().setId((Integer) args[i++]);
        o.setNome((String) args[i++]);
        o.setAno((Integer) args[i++]);
        return o;
    }

    @Override
    public Turma getObject(Element e) {
        return createObject(
                Integer.valueOf(e.getAttribute("id")),
                e.getAttribute("nome"),
                Integer.valueOf(e.getAttribute("ano")));
    }

    public Turma updateObject(Turma o, HashMap<String, Object> p) {
        o.setNome((String) p.get("nome"));
        o.setAno((Integer) p.get("ano"));

        if (p.get("estudantes") != null) {
            ((List<Estudante>) p.get("estudantes")).forEach((estudante) -> {
                o.addEstudante(estudante);
            });
        }
        return o;
    }

    @Override
    public Turma getObject(HashMap<String, Object> p) {
        Turma o = createObject(
                (Integer) p.get("id"),
                (String) p.get("nome"),
                (Integer) p.get("ano")
        );
        o.getId().setCurso((Curso) p.get("curso"));
        ((List<Estudante>) p.get("estudantes")).forEach((estudante) -> {
            o.addEstudante(estudante);
        });
        return o;
    }

    @Override
    public Node toXml(Document doc, Turma o) {
        Element e = (Element) doc.createElement("turma");
        e.setAttribute("id", o.getId().toString());
        e.setAttribute("cursoId", o.getId().getCurso().getId().getId().toString());
        e.setAttribute("campusId", o.getId().getCurso().getId().getCampus().getId().toString());
        e.setAttribute("nome", o.getNome());
        e.setAttribute("ano", o.getAno().toString());

        return e;
    }

}
