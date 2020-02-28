/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.configuracoes.dao.xml.EstudanteDaoXML;
import ensino.configuracoes.model.Estudante;
import ensino.patterns.DaoPattern;
import ensino.patterns.factory.BeanFactory;
import ensino.util.types.Presenca;
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
public class DiarioFrequenciaFactory implements BeanFactory<DiarioFrequencia> {

    private static DiarioFrequenciaFactory instance = null;

    private DiarioFrequenciaFactory() {

    }

    public static DiarioFrequenciaFactory getInstance() {
        if (instance == null) {
            instance = new DiarioFrequenciaFactory();
        }
        return instance;
    }

    @Override
    public DiarioFrequencia getObject(Object... args) {
        int i = 0;
        DiarioFrequencia o = new DiarioFrequencia();
        o.setId((Integer) args[i++]);
        o.setPresenca((Presenca) args[i++]);
        
        return o;
    }

    @Override
    public DiarioFrequencia getObject(Element e) {
        try {
            DiarioFrequencia o = getObject(
                    new Integer(e.getAttribute("id")),
                    Presenca.of(e.getAttribute("presenca")));
            
            DaoPattern<Estudante> dao = EstudanteDaoXML.getInstance();
            o.setEstudante(dao.findById(
                    new Integer(e.getAttribute("estudanteId")),
                    new Integer(e.getAttribute("turmaId")),
                    new Integer(e.getAttribute("cursoId")),
                    new Integer(e.getAttribute("campusId"))));
            
            return o;
        } catch (Exception ex) {
            Logger.getLogger(DiarioFrequenciaFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public DiarioFrequencia getObject(HashMap<String, Object> p) {
        DiarioFrequencia o = getObject(p.get("id"),
                p.get("presenca"));
        o.setEstudante((Estudante) p.get("estudante"));
        o.setDiario((Diario) p.get("diario"));
        
        return o;
    }

    @Override
    public Node toXml(Document doc, DiarioFrequencia o) {
        Element e = doc.createElement("diarioFrequencia");
        e.setAttribute("id", o.getId().toString());
        e.setAttribute("presenca", o.getPresenca().getValue());
        Diario d = o.getDiario();
        e.setAttribute("diarioId", d.getId().toString());
        e.setAttribute("planoDeEnsinoId", d.getPlanoDeEnsino().getId().toString());
        e.setAttribute("unidadeCurricularId", d.getPlanoDeEnsino().getUnidadeCurricular().getId().toString());
        e.setAttribute("estudanteId", o.getEstudante().getId().toString());
        e.setAttribute("turmaId", o.getEstudante().getTurma().getId().toString());
        e.setAttribute("cursoId", o.getEstudante().getTurma().getCurso().getId().toString());
        e.setAttribute("campusId", o.getEstudante().getTurma().getCurso().getCampus().getId().toString());
        
        
        return e;
    }
    
}
