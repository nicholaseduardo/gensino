/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import ensino.patterns.factory.BeanFactory;
import java.util.HashMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author nicho
 */
public class EstudanteFactory implements BeanFactory<Estudante> {

    private static EstudanteFactory instance = null;

    private EstudanteFactory() {
    }

    public static EstudanteFactory getInstance() {
        if (instance == null) {
            instance = new EstudanteFactory();
        }
        return instance;
    }

    @Override
    public Estudante getObject(Object... args) {
        int i = 0;
        Estudante o = new Estudante();
        o.setId((Integer) args[i++]);
        o.setNome((String) args[i++]);
        o.setRegistro((String) args[i++]);
        return o;
    }

    @Override
    public Estudante getObject(Element e) {
        return getObject(
                new Integer(e.getAttribute("id")),
                e.getAttribute("nome"),
                e.getAttribute("registro")
        );
    }

    @Override
    public Estudante getObject(HashMap<String, Object> p) {
        Estudante o = getObject(
                p.get("id"),
                p.get("nome"),
                p.get("registro")
        );
        o.setTurma((Turma) p.get("turma"));
        return o;
    }

    @Override
    public Node toXml(Document doc, Estudante o) {
        Element e = (Element) doc.createElement("estudante");
        e.setAttribute("id", o.getId().toString());
        e.setAttribute("turmaId", o.getTurma().getId().toString());
        e.setAttribute("cursoId", o.getTurma().getCurso().getId().toString());
        e.setAttribute("campusId", o.getTurma().getCurso().getCampus().getId().toString());
        e.setAttribute("nome", o.getNome());
        e.setAttribute("registro", o.getRegistro());
        return e;
    }

}
