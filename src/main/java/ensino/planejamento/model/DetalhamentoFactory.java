/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.configuracoes.dao.xml.SemanaLetivaDaoXML;
import ensino.configuracoes.model.PeriodoLetivo;
import ensino.configuracoes.model.SemanaLetiva;
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
public class DetalhamentoFactory implements BeanFactory<Detalhamento> {

    private static DetalhamentoFactory instance = null;

    private DetalhamentoFactory() {

    }

    public static DetalhamentoFactory getInstance() {
        if (instance == null) {
            instance = new DetalhamentoFactory();
        }
        return instance;
    }

    @Override
    public Detalhamento createObject(Object... args) {
        int i = 0;
        Detalhamento o = new Detalhamento();
        if (args[i] instanceof DetalhamentoId) {
            o.setId((DetalhamentoId) args[i++]);
        } else {
            o.getId().setSequencia((Integer) args[i++]);
        }
        o.setNAulasPraticas((Integer) args[i++]);
        o.setNAulasTeoricas((Integer) args[i++]);
        o.setConteudo((String) args[i++]);
        o.setObservacao((String) args[i++]);

        return o;
    }

    @Override
    public Detalhamento getObject(Element e) {
        try {
            Detalhamento o = createObject(
                    Integer.parseInt(e.getAttribute("sequencia")),
                    Integer.parseInt(e.getAttribute("nAulasPraticas")),
                    Integer.parseInt(e.getAttribute("nAulasTeoricas")),
                    e.getAttribute("conteudo"),
                    e.getAttribute("observacao"));

            DaoPattern<SemanaLetiva> dao = SemanaLetivaDaoXML.getInstance();
            o.setSemanaLetiva(dao.findById(
                    Integer.parseInt(e.getAttribute("semanaLetivaId")),
                    Integer.parseInt(e.getAttribute("nPeriodoLetivo")),
                    Integer.parseInt(e.getAttribute("ano")),
                    Integer.parseInt(e.getAttribute("campusId"))));

            return o;
        } catch (Exception ex) {
            Logger.getLogger(DetalhamentoFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Detalhamento getObject(HashMap<String, Object> p) {
        Detalhamento o = createObject(
                p.get("sequencia"),
                p.get("nAulasPraticas"),
                p.get("nAulasTeoricas"),
                p.get("conteudo"),
                p.get("observacao"));
        o.getId().setPlanoDeEnsino((PlanoDeEnsino) p.get("planoDeEnsino"));
        o.setSemanaLetiva((SemanaLetiva) p.get("semanaLetiva"));
        if (p.get("metodologias") != null) {
            ((List<Metodologia>) p.get("metodologias")).forEach((metodo) -> {
                o.addMetodologia(metodo);
            });
        }
        if (p.get("objetivoDetalhes") != null) {
            ((List<ObjetivoDetalhe>) p.get("objetivoDetalhes")).forEach((obj) -> {
                o.addObjetivoDetalhe(obj);
            });
        }
        return o;
    }

    @Override
    public Node toXml(Document doc, Detalhamento o) {
        Element e = doc.createElement("detalhamento");
        e.setAttribute("sequencia", o.getId().getSequencia().toString());
        e.setAttribute("nAulasPraticas", o.getNAulasPraticas().toString());
        e.setAttribute("nAulasTeoricas", o.getNAulasTeoricas().toString());
        e.setAttribute("conteudo", o.getConteudo());
        e.setAttribute("observacao", o.getObservacao());

        e.setAttribute("semanaLetivaId", o.getSemanaLetiva().getId().toString());
        PeriodoLetivo periodoLetivo = o.getSemanaLetiva().getId().getPeriodoLetivo();
        e.setAttribute("nPeriodoLetivo", periodoLetivo.getId().getNumero().toString());
        e.setAttribute("ano", periodoLetivo.getId().getCalendario().getId().getAno().toString());
        e.setAttribute("planoDeEnsinoId", o.getId().getPlanoDeEnsino().getId().toString());
        e.setAttribute("unidadeCurricularId", o.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getId().toString());
        e.setAttribute("cursoId", o.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getCurso().getId().getId().toString());
        e.setAttribute("campusId", periodoLetivo.getId().getCalendario().getId().getCampus().getId().toString());

        return e;
    }
}
