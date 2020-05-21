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
    public UnidadeCurricular createObject(Object... args) {
        int i = 0;
        UnidadeCurricular o = new UnidadeCurricular();
        if (args[i] instanceof UnidadeCurricularId) {
            o.setId((UnidadeCurricularId) args[i++]);
        } else {
            o.getId().setId((Integer) args[i++]);
        }
        o.setNome((String) args[i++]);
        o.setnAulasTeoricas((Integer) args[i++]);
        o.setnAulasPraticas((Integer) args[i++]);
        o.setCargaHoraria((Integer) args[i++]);
        o.setEmenta((String) args[i++]);
        return o;
    }

    @Override
    public UnidadeCurricular getObject(Element e) {
        return createObject(
                Integer.parseInt(e.getAttribute("id")),
                e.getAttribute("nome"),
                Integer.parseInt(e.getAttribute("nAulasTeoricas")),
                Integer.parseInt(e.getAttribute("nAulasPraticas")),
                Integer.parseInt(e.getAttribute("cargaHoraria")),
                e.getAttribute("ementa")
        );
    }

    public UnidadeCurricular updateObject(UnidadeCurricular o, HashMap<String, Object> p) {
        o.setNome((String) p.get("nome"));
        o.setnAulasTeoricas((Integer) p.get("nAulasTeoricas"));
        o.setnAulasPraticas((Integer) p.get("nAulasPraticas"));
        o.setCargaHoraria((Integer) p.get("cargaHoraria"));
        o.setEmenta((String) p.get("ementa"));

        if (p.get("referenciasBibliograficas") != null) {
            ((List<ReferenciaBibliografica>) p.get("referenciasBibliograficas")).forEach((rb) -> {
                o.addReferenciaBibliografica(rb);
            });
        }

        if (p.get("planosDeEnsino") != null) {
            ((List<PlanoDeEnsino>) p.get("planosDeEnsino")).forEach((pde) -> {
                o.addPlanoDeEnsino(pde);
            });
        }
        return o;
    }

    @Override
    public UnidadeCurricular getObject(HashMap<String, Object> p) {
        UnidadeCurricular o = createObject(
                new UnidadeCurricularId((Integer) p.get("id"), (Curso) p.get("curso")),
                p.get("nome"),
                p.get("nAulasTeoricas"),
                p.get("nAulasPraticas"),
                p.get("cargaHoraria"),
                p.get("ementa")
        );
        if (p.get("referenciasBibliograficas") != null) {
            ((List<ReferenciaBibliografica>) p.get("referenciasBibliograficas")).forEach((rb) -> {
                o.addReferenciaBibliografica(rb);
            });
        }

        if (p.get("planosDeEnsino") != null) {
            ((List<PlanoDeEnsino>) p.get("planosDeEnsino")).forEach((pde) -> {
                o.addPlanoDeEnsino(pde);
            });
        }
        return o;
    }

    @Override
    public Node toXml(Document doc, UnidadeCurricular o) {
        Element e = (Element) doc.createElement("unidadeCurricular");
        e.setAttribute("id", o.getId().toString());
        e.setAttribute("cursoId", o.getId().getCurso().getId().getId().toString());
        e.setAttribute("campusId", o.getId().getCurso().getId().getCampus().getId().toString());
        e.setAttribute("nome", o.getNome());
        e.setAttribute("nAulasTeoricas", o.getnAulasTeoricas().toString());
        e.setAttribute("nAulasPraticas", o.getnAulasPraticas().toString());
        e.setAttribute("cargaHoraria", o.getCargaHoraria().toString());
        e.setAttribute("ementa", o.getEmenta());
        return e;
    }

}
