/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import ensino.patterns.factory.BeanFactory;
import ensino.util.types.Periodo;
import java.text.ParseException;
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
public class SemanaLetivaFactory implements BeanFactory<SemanaLetiva> {

    private static SemanaLetivaFactory instance = null;

    private SemanaLetivaFactory() {
    }

    public static SemanaLetivaFactory getInstance() {
        if (instance == null) {
            instance = new SemanaLetivaFactory();
        }
        return instance;
    }

    @Override
    public SemanaLetiva getObject(Object... args) {
        SemanaLetiva o = new SemanaLetiva();
        int i = 0;
        o.setId((Integer) args[i++]);
        o.setDescricao((String) args[i++]);
        o.setPeriodo((Periodo) args[i++]);
        return o;
    }

    @Override
    public SemanaLetiva getObject(Element e) {
        try {
            return getObject(Integer.parseInt(e.getAttribute("numero")),
                    e.getAttribute("descricao"),
                    new Periodo(e.getAttribute("periodoDe"),
                            e.getAttribute("periodoAte")));
        } catch (ParseException ex) {
            Logger.getLogger(SemanaLetivaFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public SemanaLetiva getObject(HashMap<String, Object> p) {
        SemanaLetiva o = getObject(p.get("numero"),
                p.get("descricao"),
                p.get("periodo"));
        o.setPeriodoLetivo((PeriodoLetivo) p.get("periodoLetivo"));
        return o;
    }

    @Override
    public Node toXml(Document doc, SemanaLetiva o) {
        PeriodoLetivo p = o.getPeriodoLetivo();
        Element e = doc.createElement("semanaLetiva");
        e.setAttribute("numero", o.getId().toString());
        e.setAttribute("pNumero", p.getNumero().toString());
        e.setAttribute("ano", p.getCalendario().getAno().toString());
        e.setAttribute("campusId", p.getCalendario().getCampus().getId().toString());
        e.setAttribute("descricao", o.getDescricao());
        e.setAttribute("periodoDe", o.getPeriodo().getDeText());
        e.setAttribute("periodoAte", o.getPeriodo().getAteText());
        
        return e;
    }

}
