/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import ensino.patterns.BaseObject;
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
public class Campus extends BaseObject {

    private List<Curso> cursos;
    private List<Calendario> calendarios;

    public Campus() {
        super();
        cursos = new ArrayList();
        calendarios = new ArrayList();
    }

    public Campus(Integer id, String nome) {
        super(id, nome);
        cursos = new ArrayList();
        calendarios = new ArrayList();
    }

    public Campus(Element element) {
        super(element);
        cursos = new ArrayList();
        calendarios = new ArrayList();

        if (element.hasChildNodes()) {
            NodeList nodeList = element.getChildNodes();
            for (int j = 0; j < nodeList.getLength(); j++) {
                Node child = nodeList.item(j);
                if ("curso".equals(child.getNodeName())) {
                    Curso curso = new Curso((Element) child);
                    curso.setCampus(this);
                    this.addCurso(curso);
                } else if ("calendario".equals(child.getNodeName())) {
                    Calendario calendario = new Calendario((Element) child);
                    calendario.setCampus(this);
                    this.addCalendario(calendario);
                }
            }
        }
    }

    public List<Curso> getCursos() {
        return this.cursos;
    }

    public void setCursos(List<Curso> cursos) {
        this.cursos = cursos;
    }

    public void addCurso(Curso c) {
        c.setCampus(this);
        cursos.add(c);
    }

    public void removeCurso(Curso c) {
        c.setCampus(null);
        cursos.remove(c);
    }

    public List<Calendario> getCalendarios() {
        return this.calendarios;
    }

    public void setCalendarios(List<Calendario> calendarios) {
        this.calendarios = calendarios;
    }

    public void addCalendario(Calendario c) {
        c.setCampus(this);
        calendarios.add(c);
    }

    public void removeCalendario(Calendario c) {
        c.setCampus(null);
        calendarios.remove(c);
    }

    public boolean hasCalendario() {
        return calendarios.size() > 0;
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public Node toXml(Document doc) {
        Element elem = (Element) super.toXml(doc, "campus");
        // Adiciona os cursos vinculados ao campus
        cursos.forEach((curso) -> {
            elem.appendChild(curso.toXml(doc));
        });
        // Adiciona os calendarios
        calendarios.forEach((cal) -> {
            elem.appendChild(cal.toXml(doc));
        });
        return elem;
    }

}
