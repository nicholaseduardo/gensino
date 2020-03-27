/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import ensino.patterns.factory.BeanFactory;
import java.awt.Color;
import java.util.HashMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author nicho
 */
public class LegendaFactory implements BeanFactory<Legenda> {

    private static LegendaFactory instance = null;
    
    private LegendaFactory() {}
    
    public static LegendaFactory getInstance() {
        if (instance == null) {
            instance = new LegendaFactory();
        }
        return instance;
    }
    
    @Override
    public Legenda createObject(Object... args) {
        Legenda l = new Legenda();
        int index = 0;
        l.setId((Integer) args[index++]);
        l.setNome((String) args[index++]);
        l.setLetivo((Boolean) args[index++]);
        l.setInformativo((Boolean) args[index++]);
        l.setCor((Color) args[index++]);
        return l;
    }

    @Override
    public Legenda getObject(Element e) {
        return createObject(new Integer(e.getAttribute("id")),
                e.getAttribute("nome"),
                Boolean.parseBoolean(e.getAttribute("letivo")),
                Boolean.parseBoolean(e.getAttribute("informativo")),
                new Color(Integer.parseInt(e.getAttribute("cor"))));
    }

    @Override
    public Legenda getObject(HashMap<String, Object> p) {
        return createObject(
                p.get("id"),
                p.get("nome"),
                p.get("letivo"),
                p.get("informativo"),
                p.get("cor")
        );
    }

    @Override
    public Node toXml(Document doc, Legenda o) {
        Element e = doc.createElement("legenda");
        e.setAttribute("id", o.getId().toString());
        e.setAttribute("nome", o.getNome());
        e.setAttribute("letivo", o.isLetivo().toString());
        e.setAttribute("informativo", o.isInformativo().toString());
        e.setAttribute("cor", String.valueOf(o.getCor().getRGB()));
        return e;
    }
    
}
