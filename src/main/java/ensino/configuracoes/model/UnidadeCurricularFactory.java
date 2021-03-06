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
            o.getId().setId((Long) args[i++]);
        }
        o.setNome((String) args[i++]);
        o.setnAulasTeoricas((Integer) args[i++]);
        o.setnAulasPraticas((Integer) args[i++]);
        o.setCargaHoraria((Integer) args[i++]);
        o.setEmenta((String) args[i++]);
        return o;
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

        if (p.get("conteudos") != null) {
            ((List<Conteudo>) p.get("conteudos")).forEach((c) -> {
                o.addConteudo(c);
            });
        }

        if (p.get("objetivos") != null) {
            ((List<ObjetivoUC>) p.get("objetivos")).forEach((c) -> {
                o.addObjetivo(c);
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
                new UnidadeCurricularId((Long) p.get("id"), (Curso) p.get("curso")),
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

        if (p.get("conteudos") != null) {
            ((List<Conteudo>) p.get("conteudos")).forEach((c) -> {
                o.addConteudo(c);
            });
        }

        if (p.get("objetivos") != null) {
            ((List<ObjetivoUC>) p.get("objetivos")).forEach((c) -> {
                o.addObjetivo(c);
            });
        }

        if (p.get("planosDeEnsino") != null) {
            ((List<PlanoDeEnsino>) p.get("planosDeEnsino")).forEach((pde) -> {
                o.addPlanoDeEnsino(pde);
            });
        }
        return o;
    }

}
