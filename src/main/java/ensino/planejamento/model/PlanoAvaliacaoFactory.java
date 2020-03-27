/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.configuracoes.dao.xml.InstrumentoAvaliacaoDaoXML;
import ensino.configuracoes.model.InstrumentoAvaliacao;
import ensino.helpers.DateHelper;
import ensino.patterns.DaoPattern;
import ensino.patterns.factory.BeanFactory;
import ensino.planejamento.dao.ObjetivoDaoXML;
import ensino.util.types.Bimestre;
import java.util.Date;
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
public class PlanoAvaliacaoFactory implements BeanFactory<PlanoAvaliacao> {

    private static PlanoAvaliacaoFactory instance = null;

    private PlanoAvaliacaoFactory() {

    }

    public static PlanoAvaliacaoFactory getInstance() {
        if (instance == null) {
            instance = new PlanoAvaliacaoFactory();
        }
        return instance;
    }

    @Override
    public PlanoAvaliacao createObject(Object... args) {
        int i = 0;
        PlanoAvaliacao o = new PlanoAvaliacao();
        if (args[i] instanceof PlanoAvaliacaoId) {
            o.setId((PlanoAvaliacaoId) args[i++]);
        } else {
            o.getId().setSequencia((Integer) args[i++]);
        }
        o.setNome((String) args[i++]);
        o.setBimestre((Bimestre) args[i++]);
        o.setPeso((Double) args[i++]);
        o.setValor((Double) args[i++]);
        o.setData((Date) args[i++]);
        if (args.length > 6) {
            o.setInstrumentoAvaliacao((InstrumentoAvaliacao)args[i++]);
            o.setObjetivo((Objetivo)args[i++]);
        }

        return o;
    }

    @Override
    public PlanoAvaliacao getObject(Element e) {
        try {
            PlanoAvaliacao o = createObject(
                    new Integer(e.getAttribute("sequencia")),
                    e.getAttribute("nome"),
                    Bimestre.of(new Integer(e.getAttribute("bimestre"))),
                    new Double(e.getAttribute("peso")),
                    new Double(e.getAttribute("valor")),
                    DateHelper.stringToDate(e.getAttribute("data"), "dd/MM/yyyy"));
            DaoPattern<InstrumentoAvaliacao> instrumentoDao = InstrumentoAvaliacaoDaoXML.getInstance();
            o.setInstrumentoAvaliacao(instrumentoDao.findById(new Integer(e.getAttribute("instrumentoAvaliacaoId"))));

            DaoPattern<Objetivo> objetivoDao = ObjetivoDaoXML.getInstance();
            o.setObjetivo(objetivoDao.findById(
                    new Integer(e.getAttribute("objetivoSequencia")),
                    new Integer(e.getAttribute("planoDeEnsinoId")),
                    new Integer(e.getAttribute("unidadeCurricularId")),
                    new Integer(e.getAttribute("cursoId")),
                    new Integer(e.getAttribute("campusId"))
            ));

            return o;
        } catch (Exception ex) {
            Logger.getLogger(PlanoAvaliacaoFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public PlanoAvaliacao getObject(HashMap<String, Object> p) {
        PlanoAvaliacao o = createObject(
                p.get("sequencia"),
                p.get("nome"),
                p.get("bimestre"),
                p.get("peso"),
                p.get("valor"),
                p.get("data")
        );
        o.setInstrumentoAvaliacao((InstrumentoAvaliacao) p.get("instrumentoAvaliacao"));
        o.setObjetivo((Objetivo) p.get("objetivo"));
        o.getId().setPlanoDeEnsino((PlanoDeEnsino) p.get("planoDeEnsino"));
        o.setAvaliacoes((List<Avaliacao>) p.get("avaliacoes"));

        return o;
    }

    @Override
    public Node toXml(Document doc, PlanoAvaliacao o) {
        Element e = doc.createElement("planoAvaliacao");
        e.setAttribute("sequencia", o.getId().getSequencia().toString());
        e.setAttribute("nome", o.getNome());
        e.setAttribute("bimestre", String.valueOf(o.getBimestre().getValue()));
        e.setAttribute("peso", o.getPeso().toString());
        e.setAttribute("valor", o.getValor().toString());
        e.setAttribute("data", DateHelper.dateToString(o.getData(), "dd/MM/yyyy"));

        e.setAttribute("instrumentoAvaliacaoId", o.getInstrumentoAvaliacao().getId().toString());
        e.setAttribute("objetivoSequencia", o.getObjetivo() != null ? o.getObjetivo().getId().getSequencia().toString() : "0");
        e.setAttribute("planoDeEnsinoId", o.getId().getPlanoDeEnsino().getId().toString());
        e.setAttribute("unidadeCurricularId", o.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getId().toString());
        e.setAttribute("cursoId", o.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getCurso().getId().getId().toString());
        e.setAttribute("campusId", o.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getCurso().getId().getCampus().getId().toString());

        return e;
    }

}
