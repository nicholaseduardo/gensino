/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.patterns.factory.BeanFactory;
import java.util.HashMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author nicho
 */
public class ObjetivoFactory implements BeanFactory<Objetivo> {

    private static ObjetivoFactory instance = null;

    private ObjetivoFactory() {

    }

    public static ObjetivoFactory getInstance() {
        if (instance == null) {
            instance = new ObjetivoFactory();
        }
        return instance;
    }

    @Override
    public Objetivo getObject(Object... args) {
        int i = 0;
        Objetivo o = new Objetivo();
        o.setSequencia((Integer) args[i++]);
        o.setDescricao((String) args[i++]);
        
        return o;
    }

    @Override
    public Objetivo getObject(Element e) {
        return getObject(
            new Integer(e.getAttribute("sequencia")),
            e.getAttribute("descricao"));
    }

    @Override
    public Objetivo getObject(HashMap<String, Object> p) {
        Objetivo o = getObject(
                p.get("sequencia"),
                p.get("descricao"));
        o.setPlanoDeEnsino((PlanoDeEnsino) p.get("planoDeEnsino"));
        
        return o;
    }

    @Override
    public Node toXml(Document doc, Objetivo o) {
        Element e = doc.createElement("objetivo");
        e.setAttribute("sequencia", o.getSequencia().toString());
        e.setAttribute("descricao", o.getDescricao());
        e.setAttribute("planoDeEnsinoId", o.getPlanoDeEnsino().getId().toString());
        e.setAttribute("unidadeCurricularId", o.getPlanoDeEnsino().getUnidadeCurricular().getId().toString());
        e.setAttribute("cursoId", o.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getId().toString());
        e.setAttribute("campusId", o.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getCampus().getId().toString());

        return e;
    }
    
}
