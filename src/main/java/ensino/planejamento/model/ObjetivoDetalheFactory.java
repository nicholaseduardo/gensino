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
    public ObjetivoDetalhe createObject(Object... args) {
        ObjetivoDetalhe o = new ObjetivoDetalhe();
        if (args != null && args.length == 2)
            o.setId(new ObjetivoDetalheId((Objetivo)args[0], (Detalhamento)args[1]));
        return o;
    }

    @Override
    public ObjetivoDetalhe getObject(Element e) {
        try {
            ObjetivoDetalhe o = createObject();
            
            DaoPattern<Objetivo> dao = ObjetivoDaoXML.getInstance();
            o.getId().setObjetivo(dao.findById(
                    Integer.parseInt(e.getAttribute("objetivoSequencia")),
                    Integer.parseInt(e.getAttribute("planoDeEnsinoId")),
                    Integer.parseInt(e.getAttribute("unidadeCurricularId")),
                    Integer.parseInt(e.getAttribute("cursoId")),
                    Integer.parseInt(e.getAttribute("campusId"))
            ));
            
            return o;
        } catch (Exception ex) {
            Logger.getLogger(ObjetivoDetalheFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public ObjetivoDetalhe getObject(HashMap<String, Object> p) {
        ObjetivoDetalhe o = createObject();
        o.setId(new ObjetivoDetalheId((Objetivo) p.get("objetivo"), 
                (Detalhamento) p.get("detalhamento")));
        
        return o;
    }

    @Override
    public Node toXml(Document doc, ObjetivoDetalhe o) {
        Element e = (Element) doc.createElement("objetivoDetalhe");
        e.setAttribute("objetivoSequencia", o.getId().getObjetivo().getId().getSequencia().toString());
        Detalhamento detalhe = o.getId().getDetalhamento();
        e.setAttribute("detalhamentoSequencia", detalhe.getId().getSequencia().toString());
        e.setAttribute("planoDeEnsinoId", detalhe.getId().getPlanoDeEnsino().getId().toString());
        e.setAttribute("unidadeCurricularId", detalhe.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getId().toString());
        e.setAttribute("cursoId", detalhe.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getCurso().getId().getId().toString());
        e.setAttribute("campusId", detalhe.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getCurso().getId().getCampus().getId().toString());
        
        return e;
    }
    
}
