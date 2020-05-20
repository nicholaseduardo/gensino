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
public class EtapaEnsinoFactory implements BeanFactory<EtapaEnsino> {

    private static EtapaEnsinoFactory instance = null;

    private EtapaEnsinoFactory() {
    }

    public static EtapaEnsinoFactory getInstance() {
        if (instance == null) {
            instance = new EtapaEnsinoFactory();
        }
        return instance;
    }

    @Override
    public EtapaEnsino createObject(Object... args) {
        int i = 0;
        EtapaEnsino o = new EtapaEnsino();
        if (args[i] instanceof EtapaEnsinoId) {
            o.setId((EtapaEnsinoId) args[i++]);
        } else {
            o.getId().setId((Integer) args[i++]);
        }
        o.setNome((String) args[i++]);
        o.setRecuperacao((Boolean) args[i++]);
        if (i < args.length) {
            o.setNivelDependente((EtapaEnsino) args[i++]);
        }

        return o;
    }

    @Override
    public EtapaEnsino getObject(Element e) {
        return createObject(Integer.parseInt(e.getAttribute("id")),
                e.getAttribute("nome"));
    }

    public EtapaEnsino updateObject(EtapaEnsino o, HashMap<String, Object> p) {
        o.setNome((String)p.get("nome"));
        o.setRecuperacao((Boolean) p.get("recuperacao"));
        o.setNivelDependente((EtapaEnsino) p.get("nivelDependente"));
        
        return o;
    }

    @Override
    public EtapaEnsino getObject(HashMap<String, Object> p) {
        return createObject(
                p.get("id"),
                p.get("nome"),
                p.get("recuperacao"),
                p.get("nivelDependente"));
    }

    @Override
    public Node toXml(Document doc, EtapaEnsino o) {
        Element e = doc.createElement("tecnica");
        e.setAttribute("id", o.getId().toString());
        e.setAttribute("nome", o.getNome());
        return e;
    }

}
