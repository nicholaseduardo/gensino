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
public class DocenteFactory implements BeanFactory<Docente> {

    private static DocenteFactory instance = null;
    
    private DocenteFactory() {}
    
    public static DocenteFactory getInstance() {
        if (instance == null) {
            instance = new DocenteFactory();
        }
        return instance;
    }

    @Override
    public Docente getObject(Object... args) {
        int i = 0;
        Docente o = new Docente();
        o.setId((Integer) args[i++]);
        o.setNome((String) args[i++]);
        
        return o;
    }

    @Override
    public Docente getObject(Element e) {
        return getObject(new Integer(e.getAttribute("id")),
                e.getAttribute("nome"));
    }

    @Override
    public Docente getObject(HashMap<String, Object> p) {
        return getObject(
                p.get("id"),
                p.get("nome"));
    }

    @Override
    public Node toXml(Document doc, Docente o) {
        Element e = doc.createElement("docente");
        e.setAttribute("id", o.getId().toString());
        e.setAttribute("nome", o.getNome());
        return e;
    }
    
}
