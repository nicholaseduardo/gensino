/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import ensino.patterns.factory.BeanFactory;
import ensino.planejamento.model.PlanoDeEnsino;
import java.util.HashMap;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author nicho
 */
public class UnidadeCurricularFactory implements BeanFactory<UnidadeCurricular> {

    private static UnidadeCurricularFactory instance = null;

    private UnidadeCurricularFactory() {
    }

    public static UnidadeCurricularFactory getInstance() {
        if (instance == null) {
            instance = new UnidadeCurricularFactory();
        }
        return instance;
    }

    @Override
    public UnidadeCurricular getObject(Object... args) {
        int i = 0;
        UnidadeCurricular o = new UnidadeCurricular();
        o.setId((Integer) args[i++]);
        o.setNome((String) args[i++]);
        o.setnAulasTeoricas((Integer) args[i++]);
        o.setnAulasPraticas((Integer) args[i++]);
        o.setCargaHoraria((Integer) args[i++]);
        o.setEmenta((String) args[i++]);
        return o;
    }

    @Override
    public UnidadeCurricular getObject(Element e) {
        return getObject(
                new Integer(e.getAttribute("id")),
                e.getAttribute("nome"),
                new Integer(e.getAttribute("nAulasTeoricas")),
                new Integer(e.getAttribute("nAulasPraticas")),
                new Integer(e.getAttribute("cargaHoraria")),
                e.getAttribute("ementa")
        );
    }

    @Override
    public UnidadeCurricular getObject(HashMap<String, Object> p) {
        UnidadeCurricular o = getObject(
                p.get("id"),
                p.get("nome"),
                p.get("nAulasTeoricas"),
                p.get("nAulasPraticas"),
                p.get("cargaHoraria"),
                p.get("ementa")
        );
        o.setCurso((Curso) p.get("curso"));
        o.setReferenciasBibliograficas(
                (List<ReferenciaBibliografica>) p.get("referenciasBibliograficas"));
        o.setPlanosDeEnsino(
                (List<PlanoDeEnsino>) p.get("planosDeEnsino"));
        return o;
    }

    @Override
    public Node toXml(Document doc, UnidadeCurricular o) {
        Element e = (Element) doc.createElement("unidadeCurricular");
        e.setAttribute("id", o.getId().toString());
        e.setAttribute("cursoId", o.getCurso().getId().toString());
        e.setAttribute("campusId", o.getCurso().getCampus().getId().toString());
        e.setAttribute("nome", o.getNome());
        e.setAttribute("nAulasTeoricas", o.getnAulasTeoricas().toString());
        e.setAttribute("nAulasPraticas", o.getnAulasPraticas().toString());
        e.setAttribute("cargaHoraria", o.getCargaHoraria().toString());
        e.setAttribute("ementa", o.getEmenta());
        return e;
    }
    
}
