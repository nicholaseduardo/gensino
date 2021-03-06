/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.configuracoes.model.Docente;
import ensino.configuracoes.model.PeriodoLetivo;
import ensino.configuracoes.model.Turma;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.patterns.factory.BeanFactory;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author nicho
 */
public class PlanoDeEnsinoFactory implements BeanFactory<PlanoDeEnsino> {

    private static PlanoDeEnsinoFactory instance = null;

    private PlanoDeEnsinoFactory() {

    }

    public static PlanoDeEnsinoFactory getInstance() {
        if (instance == null) {
            instance = new PlanoDeEnsinoFactory();
        }
        return instance;
    }

    @Override
    public PlanoDeEnsino createObject(Object... args) {
        int i = 0;
        PlanoDeEnsino o = new PlanoDeEnsino();
        o.setId((Integer) args[i++]);
        o.setObjetivoGeral((String) args[i++]);
        o.setRecuperacao((String) args[i++]);

        return o;
    }

    @Override
    public PlanoDeEnsino getObject(HashMap<String, Object> p) {
        PlanoDeEnsino o = createObject(p.get("id"),
                p.get("objetivoGeral"),
                p.get("recuperacao"));
        o.setDocente((Docente) p.get("docente"));
        o.setUnidadeCurricular((UnidadeCurricular) p.get("unidadeCurricular"));
        o.setPeriodoLetivo((PeriodoLetivo) p.get("periodoLetivo"));
        o.setTurma((Turma) p.get("turma"));

        if (p.containsKey("objetivos")) {
            ((List<Objetivo>) p.get("objetivos")).forEach((obj) -> {
                o.addObjetivo(obj);
            });
        }
        if (p.containsKey("detalhamentos")) {
            ((List<Detalhamento>) p.get("detalhamentos")).forEach((det) -> {
                o.addDetalhamento(det);
            });
        }
        if (p.containsKey("planoAvaliacoes")) {
            ((List<PlanoAvaliacao>) p.get("planoAvaliacoes")).forEach((pl) -> {
                o.addPlanoAvaliacao(pl);
            });
        }
        if (p.containsKey("horarios")) {
            ((List<HorarioAula>) p.get("horarios")).forEach((hor) -> {
                o.addHorario(hor);
            });
        }
        if (p.containsKey("diarios")) {
            ((List<Diario>) p.get("diarios")).forEach((di) -> {
                o.addDiario(di);
            });
        }

        return o;
    }

    public PlanoDeEnsino updateObject(PlanoDeEnsino o, HashMap<String, Object> p) {
        o.setObjetivoGeral((String) p.get("objetivoGeral"));
        o.setRecuperacao((String) p.get("recuperacao"));
        o.setDocente((Docente) p.get("docente"));
        o.setUnidadeCurricular((UnidadeCurricular) p.get("unidadeCurricular"));
        o.setPeriodoLetivo((PeriodoLetivo) p.get("periodoLetivo"));
        o.setTurma((Turma) p.get("turma"));

        if (p.containsKey("objetivos")) {
            ((List<Objetivo>) p.get("objetivos")).forEach((obj) -> {
                o.addObjetivo(obj);
            });
        }
        if (p.containsKey("detalhamentos")) {
            ((List<Detalhamento>) p.get("detalhamentos")).forEach((det) -> {
                o.addDetalhamento(det);
            });
        }
        if (p.containsKey("planoAvaliacoes")) {
            ((List<PlanoAvaliacao>) p.get("planoAvaliacoes")).forEach((pl) -> {
                o.addPlanoAvaliacao(pl);
            });
        }
        if (p.containsKey("horarios")) {
            ((List<HorarioAula>) p.get("horarios")).forEach((hor) -> {
                o.addHorario(hor);
            });
        }
        if (p.containsKey("diarios")) {
            ((List<Diario>) p.get("diarios")).forEach((di) -> {
                o.addDiario(di);
            });
        }

        return o;
    }
}
