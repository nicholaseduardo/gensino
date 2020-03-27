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
public class BibliografiaFactory implements BeanFactory<Bibliografia> {

    private static BibliografiaFactory instance = null;

    private BibliografiaFactory() {
    }

    public static BibliografiaFactory getInstance() {
        if (instance == null) {
            instance = new BibliografiaFactory();
        }
        return instance;
    }

    @Override
    public Bibliografia createObject(Object... args) {
        int i = 0;
        Bibliografia o = new Bibliografia();
        o.setId((Integer) args[i++]);
        o.setTitulo((String) args[i++]);
        o.setAutor((String) args[i++]);
        o.setReferencia((String) args[i++]);
        return o;
    }

    @Override
    public Bibliografia getObject(Element e) {
        return createObject(
                new Integer(e.getAttribute("id")),
                e.getAttribute("titulo"),
                e.getAttribute("autor"),
                e.getAttribute("referencia")
        );
    }

    @Override
    public Bibliografia getObject(HashMap<String, Object> p) {
        return createObject(
                p.get("id"),
                p.get("titulo"),
                p.get("autor"),
                p.get("referencia")
        );
    }

    @Override
    public Node toXml(Document doc, Bibliografia o) {
        Element e = doc.createElement("bibliografia");
        e.setAttribute("id", o.getId().toString());
        e.setAttribute("titulo", o.getTitulo());
        e.setAttribute("autor", o.getAutor());
        e.setAttribute("referencia", o.getReferencia());
        return e;
    }
    
}
