/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.configuracoes.dao.xml.DocenteDaoXML;
import ensino.configuracoes.dao.xml.EstudanteDaoXML;
import ensino.configuracoes.dao.xml.PeriodoLetivoDaoXML;
import ensino.configuracoes.dao.xml.SemanaLetivaDaoXML;
import ensino.configuracoes.dao.xml.TurmaDaoXML;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.Docente;
import ensino.configuracoes.model.Estudante;
import ensino.configuracoes.model.PeriodoLetivo;
import ensino.configuracoes.model.SemanaLetiva;
import ensino.configuracoes.model.Turma;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.patterns.DaoPattern;
import ensino.patterns.factory.BeanFactory;
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
    public PlanoDeEnsino getObject(Object... args) {
        int i = 0;
        PlanoDeEnsino o = new PlanoDeEnsino();
        o.setId((Integer) args[i++]);
        o.setObjetivoGeral((String) args[i++]);
        o.setRecuperacao((String) args[i++]);
        
        return o;
    }

    @Override
    public PlanoDeEnsino getObject(Element e) {
        try {
            Integer campusId = new Integer(e.getAttribute("campusId")),
                    cursoId = new Integer(e.getAttribute("cursoId")),
                    undId = new Integer(e.getAttribute("unidadeCurricularId")),
                    calenarioId = new Integer(e.getAttribute("ano")),
                    periodoLetivoNumero = new Integer(e.getAttribute("nPeriodoLetivo")),
                    turmaId = new Integer(e.getAttribute("turmaId"));
            
            PlanoDeEnsino o = getObject(
                    new Integer(e.getAttribute("id")),
                    e.getAttribute("objetivoGeral"),
                    e.getAttribute("recuperacao"));
            // recupera as associações
            DaoPattern<Docente> docenteDao = DocenteDaoXML.getInstance();
            o.setDocente(docenteDao.findById(new Integer(e.getAttribute("docenteId"))));
            
            DaoPattern<PeriodoLetivo> periodoLetivoDao = PeriodoLetivoDaoXML.getInstance();
            PeriodoLetivo periodoLetivo = periodoLetivoDao.findById(periodoLetivoNumero, calenarioId, campusId);
            o.setPeriodoLetivo(periodoLetivo);
            /**
             * Recupera as semanas letivas do período letivo vinculado ao
             * plano de ensino
             */
            DaoPattern<SemanaLetiva> semanaLetivaDao = SemanaLetivaDaoXML.getInstance();
            String filter = String.format("//SemanaLetiva/semanaLetiva[@pNumero=%d "
                + "and @ano=%d and @campusId=%d]", 
                periodoLetivoNumero, calenarioId, campusId);
            periodoLetivo.setSemanasLetivas(semanaLetivaDao.list(filter, o.getPeriodoLetivo()));
            
            DaoPattern<Turma> turmaDao = TurmaDaoXML.getInstance();
            Turma turma = turmaDao.findById(turmaId, cursoId, campusId);
            o.setTurma(turma);
            /**
             * Recupera os estudantes vinculados a turma
             */
            DaoPattern<Estudante> estudanteDao = EstudanteDaoXML.getInstance();
            filter = String.format("//Estudante/estudante[@turmaId=%d and @cursoId=%d and @campusId=%d]", 
                turma.getId(), turma.getCurso().getId(),
                turma.getCurso().getCampus().getId());
            turma.setEstudantes(estudanteDao.list(filter, turma));
            
            return o;
        } catch (Exception ex) {
            Logger.getLogger(PlanoDeEnsinoFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public PlanoDeEnsino getObject(HashMap<String, Object> p) {
        PlanoDeEnsino o = getObject(p.get("id"),
                p.get("objetivoGeral"),
                p.get("recuperacao"));
        o.setDocente((Docente) p.get("docente"));
        o.setUnidadeCurricular((UnidadeCurricular) p.get("unidadeCurricular"));
        o.setPeriodoLetivo((PeriodoLetivo) p.get("periodoLetivo"));
        o.setTurma((Turma) p.get("turma"));
        
        ((List<Objetivo>) p.get("objetivos")).forEach((obj) -> {
            o.addObjetivo(obj);
        });
        ((List<Detalhamento>) p.get("detalhamentos")).forEach((det) -> {
            o.addDetalhamento(det);
        });
        ((List<PlanoAvaliacao>) p.get("planoAvaliacoes")).forEach((pl) -> {
            o.addPlanoAvaliacao(pl);
        });
        ((List<HorarioAula>) p.get("horarios")).forEach((hor) -> {
            o.addHorario(hor);
        });
        ((List<Diario>) p.get("diarios")).forEach((di) -> {
            o.addDiario(di);
        });
        
        return o;
    }

    @Override
    public Node toXml(Document doc, PlanoDeEnsino o) {
        Element e = doc.createElement("planoDeEnsino");
        e.setAttribute("id", o.getId().toString());
        e.setAttribute("objetivoGeral", o.getObjetivoGeral());
        e.setAttribute("recuperacao", o.getRecuperacao());
        
        e.setAttribute("docenteId", o.getDocente().getId().toString());
        Calendario calendario = o.getPeriodoLetivo().getCalendario();
        e.setAttribute("nPeriodoLetivo", o.getPeriodoLetivo().getNumero().toString());
        e.setAttribute("ano", o.getPeriodoLetivo().getCalendario().getAno().toString());
        e.setAttribute("turmaId", o.getTurma().getId().toString());
        e.setAttribute("cursoId", o.getTurma().getCurso().getId().toString());
        e.setAttribute("unidadeCurricularId", o.getUnidadeCurricular().getId().toString());
        e.setAttribute("campusId", calendario.getCampus().getId().toString());
        
        return e;
    }
}
