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
public class InstrumentoAvaliacaoFactory implements BeanFactory<InstrumentoAvaliacao> {

    private static InstrumentoAvaliacaoFactory instance = null;
    
    private InstrumentoAvaliacaoFactory() {}
    
    public static InstrumentoAvaliacaoFactory getInstance() {
        if (instance == null) {
            instance = new InstrumentoAvaliacaoFactory();
        }
        return instance;
    }

    @Override
    public InstrumentoAvaliacao getObject(Object... args) {
        int i = 0;
        InstrumentoAvaliacao o = new InstrumentoAvaliacao();
        o.setId((Integer) args[i++]);
        o.setNome((String) args[i++]);
        
        return o;
    }

    @Override
    public InstrumentoAvaliacao getObject(Element e) {
        return getObject(new Integer(e.getAttribute("id")),
                e.getAttribute("nome"));
    }

    @Override
    public InstrumentoAvaliacao getObject(HashMap<String, Object> p) {
        return getObject(
                p.get("id"),
                p.get("nome"));
    }

    @Override
    public Node toXml(Document doc, InstrumentoAvaliacao o) {
        Element e = doc.createElement("instrumentoAvaliacao");
        e.setAttribute("id", o.getId().toString());
        e.setAttribute("nome", o.getNome());
        return e;
    }
    
}
