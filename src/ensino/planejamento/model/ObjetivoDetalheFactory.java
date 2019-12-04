/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.patterns.DaoPattern;
import ensino.patterns.factory.BeanFactory;
import ensino.planejamento.dao.ObjetivoDaoXML;
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
public class ObjetivoDetalheFactory implements BeanFactory<ObjetivoDetalhe> {

    private static ObjetivoDetalheFactory instance = null;

    private ObjetivoDetalheFactory() {
    }

    public static ObjetivoDetalheFactory getInstance() {
        if (instance == null) {
            instance = new ObjetivoDetalheFactory();
        }
        return instance;
    }

    @Override
    public ObjetivoDetalhe getObject(Object... args) {
        return new ObjetivoDetalhe();
    }

    @Override
    public ObjetivoDetalhe getObject(Element e) {
        try {
            ObjetivoDetalhe o = getObject();
            
            DaoPattern<Objetivo> dao = new ObjetivoDaoXML();
            o.setObjetivo(dao.findById(
                    new Integer(e.getAttribute("detalhamentoSequencia")),
                    new Integer(e.getAttribute("planoDeEnsinoId")),
                    new Integer(e.getAttribute("unidadeCurricularId")),
                    new Integer(e.getAttribute("cursoId")),
                    new Integer(e.getAttribute("campusId"))
            ));
            
            return o;
        } catch (Exception ex) {
            Logger.getLogger(ObjetivoDetalheFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public ObjetivoDetalhe getObject(HashMap<String, Object> p) {
        ObjetivoDetalhe o = getObject();
        o.setDetalhamento((Detalhamento) p.get("detalhamento"));
        o.setObjetivo((Objetivo) p.get("objetivo"));
        
        return o;
    }

    @Override
    public Node toXml(Document doc, ObjetivoDetalhe o) {
        Element e = (Element) doc.createElement("objetivoDetalhe");
        e.setAttribute("objetivoSequencia", o.getObjetivo().getSequencia().toString());
        Detalhamento detalhe = o.getDetalhamento();
        e.setAttribute("detalhamentoSequencia", detalhe.getSequencia().toString());
        e.setAttribute("planoDeEnsinoId", detalhe.getPlanoDeEnsino().getId().toString());
        e.setAttribute("unidadeCurricularId", detalhe.getPlanoDeEnsino().getUnidadeCurricular().getId().toString());
        e.setAttribute("cursoId", detalhe.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getId().toString());
        e.setAttribute("campusId", detalhe.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getCampus().getId().toString());
        
        return e;
    }
    
}
