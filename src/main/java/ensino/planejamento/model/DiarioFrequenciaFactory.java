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
    public DiarioFrequencia createObject(Object... args) {
        int i = 0;
        DiarioFrequencia o = new DiarioFrequencia();
        if (args[i] instanceof DiarioFrequenciaId) {
            o.setId((DiarioFrequenciaId) args[i++]);
        } else {
            o.getId().setId((Integer) args[i++]);
        }
        o.setPresenca((Presenca) args[i++]);

        return o;
    }

    @Override
    public DiarioFrequencia getObject(Element e) {
        try {
            DiarioFrequencia o = createObject(
                    Integer.parseInt(e.getAttribute("id")),
                    Presenca.of(e.getAttribute("presenca")));

            DaoPattern<Estudante> dao = EstudanteDaoXML.getInstance();
            o.getId().setEstudante(dao.findById(
                    Integer.parseInt(e.getAttribute("estudanteId")),
                    Integer.parseInt(e.getAttribute("turmaId")),
                    Integer.parseInt(e.getAttribute("cursoId")),
                    Integer.parseInt(e.getAttribute("campusId"))));

            return o;
        } catch (Exception ex) {
            Logger.getLogger(DiarioFrequenciaFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public DiarioFrequencia getObject(HashMap<String, Object> p) {
        DiarioFrequencia o = createObject(p.get("id"),
                p.get("presenca"));
        o.getId().setEstudante((Estudante) p.get("estudante"));
        o.getId().setDiario((Diario) p.get("diario"));

        return o;
    }

    @Override
    public Node toXml(Document doc, DiarioFrequencia o) {
        Element e = doc.createElement("diarioFrequencia");
        e.setAttribute("id", o.getId().toString());
        e.setAttribute("presenca", o.getPresenca().getValue());
        Diario d = o.getId().getDiario();
        e.setAttribute("diarioId", d.getId().toString());
        e.setAttribute("planoDeEnsinoId", d.getId().getPlanoDeEnsino().getId().toString());
        e.setAttribute("unidadeCurricularId", d.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().toString());
        e.setAttribute("estudanteId", o.getId().getEstudante().getId().toString());
        e.setAttribute("turmaId", o.getId().getEstudante().getId().getTurma().getId().getId().toString());
        e.setAttribute("cursoId", o.getId().getEstudante().getId().getTurma().getId().getCurso().getId().getId().toString());
        e.setAttribute("campusId", o.getId().getEstudante().getId().getTurma().getId().getCurso().getId().getCampus().getId().toString());

        return e;
    }

}
