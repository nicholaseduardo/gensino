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
public class NivelEnsinoFactory implements BeanFactory<NivelEnsino> {

    private static NivelEnsinoFactory instance = null;
    
    private NivelEnsinoFactory() {}
    
    public static NivelEnsinoFactory getInstance() {
        if (instance == null) {
            instance = new NivelEnsinoFactory();
        }
        return instance;
    }

    @Override
    public NivelEnsino createObject(Object... args) {
        int i = 0;
        NivelEnsino o = new NivelEnsino();
        o.setId((Integer) args[i++]);
        o.setNome((String) args[i++]);
        
        return o;
    }

    @Override
    public NivelEnsino getObject(Element e) {
        NivelEnsino o = createObject(Integer.parseInt(e.getAttribute("id")),
                e.getAttribute("nome"));
        return o;
    }

    @Override
    public NivelEnsino getObject(HashMap<String, Object> p) {
        NivelEnsino o = createObject(p.get("id"), p.get("nome"));
        o.setEtapas((List<EtapaEnsino>) p.get("etapas"));
        return o;
    }

    @Override
    public Node toXml(Document doc, NivelEnsino o) {
        Element e = doc.createElement("tecnica");
        e.setAttribute("id", o.getId().toString());
        e.setAttribute("nome", o.getNome());
        return e;
    }
    
}
