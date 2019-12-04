/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.configuracoes.dao.xml.DocenteDaoXML;
import ensino.configuracoes.dao.xml.PeriodoLetivoDaoXML;
import ensino.configuracoes.dao.xml.TurmaDaoXML;
import ensino.configuracoes.dao.xml.UnidadeCurricularDaoXML;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.Docente;
import ensino.configuracoes.model.PeriodoLetivo;
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
            DaoPattern<Docente> docenteDao = new DocenteDaoXML();
            o.setDocente(docenteDao.findById(new Integer(e.getAttribute("docenteId"))));
            
            DaoPattern<UnidadeCurricular> undDao = new UnidadeCurricularDaoXML();
            o.setUnidadeCurricular(undDao.findById(undId, cursoId, campusId));
            
            DaoPattern<PeriodoLetivo> periodoLetivoDao = new PeriodoLetivoDaoXML();
            o.setPeriodoLetivo(periodoLetivoDao.findById(periodoLetivoNumero, calenarioId, campusId));
            
            DaoPattern<Turma> turmaDao = new TurmaDaoXML();
            o.setTurma(turmaDao.findById(turmaId, cursoId, campusId));
            
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
        
        o.setObjetivos((List<Objetivo>) p.get("objetivos"));
        o.setDetalhamentos((List<Detalhamento>) p.get("detalhamentos"));
        o.setPlanosAvaliacoes((List<PlanoAvaliacao>) p.get("planoAvaliacoes"));
        o.setHorarios((List<HorarioAula>) p.get("horarios"));
        o.setDiarios((List<Diario>) p.get("diarios"));
        
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
        e.setAttribute("turmaId", o.getTurma().getId().toString());
        e.setAttribute("cursoId", o.getTurma().getCurso().getId().toString());
        e.setAttribute("unidadeCurricularId", o.getUnidadeCurricular().getId().toString());
        e.setAttribute("campusId", calendario.getCampus().getId().toString());
        
        return e;
    }
}
