/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.configuracoes.dao.xml.InstrumentoAvaliacaoDaoXML;
import ensino.configuracoes.dao.xml.RecursoDaoXML;
import ensino.configuracoes.dao.xml.TecnicaDaoXML;
import ensino.patterns.BaseObject;
import ensino.patterns.DaoPattern;
import ensino.patterns.factory.BeanFactory;
import ensino.util.types.TipoMetodo;
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
public class MetodologiaFactory implements BeanFactory<Metodologia> {

    private static MetodologiaFactory instance = null;

    private MetodologiaFactory() {
    }

    public static MetodologiaFactory getInstance() {
        if (instance == null) {
            instance = new MetodologiaFactory();
        }
        return instance;
    }

    @Override
    public Metodologia getObject(Object... args) {
        int i = 0;
        Metodologia o = new Metodologia();
        o.setSequencia((Integer) args[i++]);
        o.setTipo((TipoMetodo) args[i++]);
        o.setMetodo((BaseObject) args[i++]);
        
        return o;
    }

    @Override
    public Metodologia getObject(Element e) {
        try {
            Metodologia o = getObject(
                    new Integer(e.getAttribute("sequencia")),
                    TipoMetodo.of(new Integer(e.getAttribute("tipoMetodo"))),
                    null);
            
            DaoPattern dao;
            switch(o.getTipo()) {
                default:
                case TECNICA: dao = new TecnicaDaoXML(); break;
                case RECURSO: dao = new RecursoDaoXML(); break;
                case INSTRUMENTO: dao = new InstrumentoAvaliacaoDaoXML(); break;
            }
            o.setMetodo((BaseObject) dao.findById(new Integer(e.getAttribute("baseObjectId"))));
            
            return o;
        } catch (Exception ex) {
            Logger.getLogger(MetodologiaFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Metodologia getObject(HashMap<String, Object> p) {
        Metodologia o = getObject(
                p.get("sequencia"),
                p.get("tipo"),
                p.get("metodo")
        );
        o.setDetalhamento((Detalhamento) p.get("detalhamento"));
        
        return o;
    }

    @Override
    public Node toXml(Document doc, Metodologia o) {
        Element e = (Element) doc.createElement("metodologia");
        e.setAttribute("sequencia", o.getSequencia().toString());
        e.setAttribute("tipoMetodo", String.valueOf(o.getTipo().getValue()));
        e.setAttribute("baseObjectId", o.getMetodo().getId().toString());
        Detalhamento detalhe = o.getDetalhamento();
        e.setAttribute("detalhamentoSequencia", detalhe.getSequencia().toString());
        e.setAttribute("planoDeEnsinoId", detalhe.getPlanoDeEnsino().getId().toString());
        e.setAttribute("unidadeCurricularId", detalhe.getPlanoDeEnsino().getUnidadeCurricular().getId().toString());
        e.setAttribute("cursoId", detalhe.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getId().toString());
        e.setAttribute("campusId", detalhe.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getCampus().getId().toString());
        
        return e;
    }
    
}
