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
    public Avaliacao createObject(Object... args) {
        int i = 0;
        Avaliacao o = new Avaliacao();
        if (args[i] instanceof AvaliacaoId) {
            o.setId((AvaliacaoId) args[i++]);
        }
        o.setNota((Double) args[i++]);

        return o;
    }

    @Override
    public Avaliacao getObject(Element e) {
        try {
            Avaliacao o = createObject(new Double(e.getAttribute("nota")));

            DaoPattern<Estudante> dao = EstudanteDaoXML.getInstance();
            o.getId().setEstudante(dao.findById(
                    Integer.parseInt(e.getAttribute("estudanteId")),
                    Integer.parseInt(e.getAttribute("turmaId")),
                    Integer.parseInt(e.getAttribute("cursoId")),
                    Integer.parseInt(e.getAttribute("campusId"))
            ));
            return o;
        } catch (Exception ex) {
            Logger.getLogger(AvaliacaoFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Avaliacao getObject(HashMap<String, Object> p) {
        Avaliacao o = createObject(p.get("nota"));
        o.setId(new AvaliacaoId((PlanoAvaliacao) p.get("planoAvaliacao"),
                (Estudante) p.get("estudante")));

        return o;
    }

    @Override
    public Node toXml(Document doc, Avaliacao o) {
        Element e = doc.createElement("avaliacao");
        e.setAttribute("nota", o.getNota().toString());

        e.setAttribute("planoAvaliacaoSequencia", o.getId().getPlanoAvaliacao().getId().getSequencia().toString());
        e.setAttribute("planoDeEnsinoId", o.getId().getPlanoAvaliacao().getId().getPlanoDeEnsino().getId().toString());
        e.setAttribute("unidadeCurricularId", o.getId().getPlanoAvaliacao().getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getId().toString());

        e.setAttribute("estudanteId", o.getId().getEstudante().getId().getId().toString());
        e.setAttribute("turmaId", o.getId().getEstudante().getId().getTurma().getId().getId().toString());
        e.setAttribute("cursoId", o.getId().getEstudante().getId().getTurma().getId().getCurso().getId().getId().toString());
        e.setAttribute("campusId", o.getId().getEstudante().getId().getTurma().getId().getCurso().getId().getCampus().getId().toString());

        return e;
    }

}
