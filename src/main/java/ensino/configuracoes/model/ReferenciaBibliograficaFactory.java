/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import ensino.configuracoes.dao.xml.BibliografiaDaoXML;
import ensino.patterns.DaoPattern;
import ensino.patterns.factory.BeanFactory;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author nicho
 */
public class ReferenciaBibliograficaFactory implements BeanFactory<ReferenciaBibliografica> {

    private static ReferenciaBibliograficaFactory instance = null;

    private ReferenciaBibliograficaFactory() {
    }

    public static ReferenciaBibliograficaFactory getInstance() {
        if (instance == null) {
            instance = new ReferenciaBibliograficaFactory();
        }
        return instance;
    }

    @Override
    public ReferenciaBibliografica createObject(Object... args) {
        int i = 0;
        ReferenciaBibliografica o = new ReferenciaBibliografica();
        o.getId().setSequencia((Integer) args[i++]);
        o.setTipo((Integer) args[i++]);
        return o;
    }

    @Override
    public ReferenciaBibliografica getObject(Element e) {
        try {
            ReferenciaBibliografica o = createObject(Integer.parseInt(e.getAttribute("sequencia")),
                    Integer.parseInt(e.getAttribute("tipo")));
            // Recupear a bibliografia
            DaoPattern<Bibliografia> dao = BibliografiaDaoXML.getInstance();
            Bibliografia b = dao.findById(Integer.parseInt(e.getAttribute("bibliografiaId")));
            o.getId().setBibliografia(b);
            return o;
        } catch (Exception ex) {
            Logger.getLogger(ReferenciaBibliograficaFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public ReferenciaBibliografica getObject(HashMap<String, Object> p) {
        ReferenciaBibliografica o = createObject(p.get("sequencia"),
                p.get("tipo"));
        o.getId().setUnidadeCurricular((UnidadeCurricular) p.get("unidadeCurricular"));
        o.getId().setBibliografia((Bibliografia) p.get("bibliografia"));
        return o;
    }

    @Override
    public Node toXml(Document doc, ReferenciaBibliografica o) {
        Element e = doc.createElement("referenciaBibliografica");
        e.setAttribute("sequencia", o.getId().getSequencia().toString());
        e.setAttribute("tipo", o.getTipo().toString());
        UnidadeCurricular und = o.getId().getUnidadeCurricular();
        e.setAttribute("unidadeCurricularId", und.getId().getId().toString());
        e.setAttribute("cursoId", und.getId().getCurso().getId().getId().toString());
        e.setAttribute("campusId", und.getId().getCurso().getId().getCampus().getId().toString());
        e.setAttribute("bibliografiaId", o.getId().getBibliografia().getId().toString());
        return e;
    }

}