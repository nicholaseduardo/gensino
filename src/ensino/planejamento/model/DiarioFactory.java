/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.helpers.DateHelper;
import ensino.patterns.factory.BeanFactory;
import ensino.util.types.TipoAula;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author nicho
 */
public class DiarioFactory implements BeanFactory<Diario> {

    private static DiarioFactory instance = null;

    private DiarioFactory() {

    }

    public static DiarioFactory getInstance() {
        if (instance == null) {
            instance = new DiarioFactory();
        }
        return instance;
    }

    @Override
    public Diario getObject(Object... args) {
        int i = 0;
        Diario o = new Diario();
        o.setId((Integer) args[i++]);
        o.setData((Date) args[i++]);
        o.setHorario((String) args[i++]);
        o.setObservacoes((String) args[i++]);
        o.setConteudo((String) args[i++]);
        o.setTipoAula((TipoAula) args[i++]);
        
        return o;
    }

    @Override
    public Diario getObject(Element e) {
        try {
            Diario o = getObject(
                    new Integer(e.getAttribute("id")),
                    DateHelper.stringToDate(e.getAttribute("data"), "dd/MM/yyyy"),
                    e.getAttribute("horario"),
                    e.getAttribute("obsevacoes"),
                    e.getAttribute("conteudo"),
                    TipoAula.of(e.getAttribute("tipoAula"))
            );
            
            return o;
        } catch (ParseException ex) {
            Logger.getLogger(DiarioFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Diario getObject(HashMap<String, Object> p) {
        Diario o = getObject(p.get("id"),
                p.get("data"),
                p.get("horario"),
                p.get("observacoes"),
                p.get("conteudo"),
                p.get("tipoAula"));
        o.setPlanoDeEnsino((PlanoDeEnsino) p.get("planoDeEnsino"));
        o.setFrequencias((List<DiarioFrequencia>) p.get("frequencias"));
        
        return o;
    }

    @Override
    public Node toXml(Document doc, Diario o) {
        Element e = doc.createElement("diario");
        e.setAttribute("id", o.getId().toString());
        e.setAttribute("data", DateHelper.dateToString(o.getData(), "dd/MM/yyyy"));
        e.setAttribute("horario", o.getHorario());
        e.setAttribute("observacoes", o.getObservacoes());
        e.setAttribute("conteudo", o.getConteudo());
        e.setAttribute("tipoAula", o.getTipoAula().getValue());
        
        e.setAttribute("planoDeEnsinoId", o.getPlanoDeEnsino().getId().toString());
        e.setAttribute("unidadeCurricularId", o.getPlanoDeEnsino().getUnidadeCurricular().getId().toString());
        e.setAttribute("cursoId", o.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getId().toString());
        e.setAttribute("campusId", o.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getCampus().getId().toString());

        return e;
    }
    
}
