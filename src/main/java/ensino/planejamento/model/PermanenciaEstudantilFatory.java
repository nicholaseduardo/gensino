/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.helpers.DateHelper;
import ensino.patterns.factory.BeanFactory;
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
 * @author santos
 */
public class PermanenciaEstudantilFatory implements BeanFactory<PermanenciaEstudantil> {

    private static PermanenciaEstudantilFatory instance = null;
    
    private PermanenciaEstudantilFatory() {
        
    }
    
    public static PermanenciaEstudantilFatory getInstance() {
        if (instance == null) {
            instance = new PermanenciaEstudantilFatory();
        }
        return instance;
    }
    
    @Override
    public PermanenciaEstudantil createObject(Object... args) {
        int i = 0;
        PermanenciaEstudantil o = new PermanenciaEstudantil();
        
        if (args[i] instanceof PermanenciaEstudantilId) {
            o.setId((PermanenciaEstudantilId)args[i++]);
        } else {
            o.getId().setSequencia((Integer) args[i++]);
        }
        o.setDataAtendimento((Date)args[i++]);
        o.setHoraAtendimento((Date)args[i++]);
        o.setDescricao((String)args[i++]);
        
        return o;
    }

    @Override
    public PermanenciaEstudantil getObject(Element e) {
        try {
            PermanenciaEstudantil o = createObject(
                    Integer.parseInt(e.getAttribute("id")),
                    DateHelper.stringToDate(e.getAttribute("dataAtendimento"), "dd/MM/yyyy H:i"),
                    e.getAttribute("descricao")
            );

            return o;
        } catch (ParseException ex) {
            Logger.getLogger(PermanenciaEstudantilFatory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public PermanenciaEstudantil getObject(HashMap<String, Object> p) {
        PermanenciaEstudantil o = createObject(
                new PermanenciaEstudantilId(
                        (Integer)p.get("id"), 
                        (PlanoDeEnsino)p.get("planoDeEnsino")),
                p.get("dataAtendimento"),
                p.get("horaAtendimento"),
                p.get("descricao")
        );
        o.setAtendimentos((List<AtendimentoEstudante>) p.get("atendimentos"));
        return o;
    }

    @Override
    public Node toXml(Document doc, PermanenciaEstudantil o) {
        Element e = doc.createElement("permanenciaEstudantil");
        e.setAttribute("id", o.getId().toString());
        e.setAttribute("dataAtendimento", DateHelper.dateToString(o.getDataAtendimento(), "dd/MM/yyyy H:i"));
        e.setAttribute("descricao", o.getDescricao());
        
        e.setAttribute("planoDeEnsinoId", o.getId().getPlanoDeEnsino().getId().toString());
        e.setAttribute("unidadeCurricularId", o.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getId().toString());
        e.setAttribute("cursoId", o.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getCurso().getId().getId().toString());
        e.setAttribute("campusId", o.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getCurso().getId().getCampus().getId().toString());

        return e;
    }
    
    
}
