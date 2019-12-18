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
public class AvaliacaoFactory implements BeanFactory<Avaliacao> {

    private static AvaliacaoFactory instance = null;

    private AvaliacaoFactory() {

    }

    public static AvaliacaoFactory getInstance() {
        if (instance == null) {
            instance = new AvaliacaoFactory();
        }
        return instance;
    }

    @Override
    public Avaliacao getObject(Object... args) {
        int i = 0;
        Avaliacao o = new Avaliacao();
        o.setNota((Double) args[i++]);
        
        return o;
    }

    @Override
    public Avaliacao getObject(Element e) {
        try {
            Avaliacao o = getObject(new Double(e.getAttribute("nota")));
            
            DaoPattern<Estudante> dao = EstudanteDaoXML.getInstance();
            o.setEstudante(dao.findById(
                    new Integer(e.getAttribute("estudanteId")),
                    new Integer(e.getAttribute("turmaId")),
                    new Integer(e.getAttribute("cursoId")),
                    new Integer(e.getAttribute("campusId"))
            ));
            return o;
        } catch (Exception ex) {
            Logger.getLogger(AvaliacaoFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Avaliacao getObject(HashMap<String, Object> p) {
        Avaliacao o = getObject(p.get("nota"));
        o.setEstudante((Estudante) p.get("estudante"));
        o.setPlanoAvaliacao((PlanoAvaliacao) p.get("planoAvaliacao"));
        
        return o;
    }

    @Override
    public Node toXml(Document doc, Avaliacao o) {
        Element e = doc.createElement("avaliacao");
        e.setAttribute("nota", o.getNota().toString());
        e.setAttribute("estudanteId", o.getEstudante().getId().toString());
        e.setAttribute("turmaId", o.getEstudante().getTurma().toString());
        e.setAttribute("cursoId", o.getEstudante().getTurma().getCurso().getId().toString());
        e.setAttribute("campusId", o.getEstudante().getTurma().getCurso().getCampus().getId().toString());
        
        return e;
    }
    
}
