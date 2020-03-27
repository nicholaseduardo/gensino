/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.configuracoes.dao.xml.InstrumentoAvaliacaoDaoXML;
import ensino.configuracoes.dao.xml.RecursoDaoXML;
import ensino.configuracoes.dao.xml.TecnicaDaoXML;
import ensino.patterns.BaseObject;
import ensino.patterns.DaoPattern;
import ensino.patterns.factory.BeanFactory;
import ensino.util.types.TipoMetodo;
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
public class MetodologiaFactory implements BeanFactory<Metodologia> {

    private static MetodologiaFactory instance = null;

    private MetodologiaFactory() {
    }

    public static MetodologiaFactory getInstance() {
        if (instance == null) {
            instance = new MetodologiaFactory();
        }
        return instance;
    }

    @Override
    public Metodologia createObject(Object... args) {
        int i = 0;
        Metodologia o = new Metodologia();
        if (args[i] instanceof MetodologiaId) {
            o.setId((MetodologiaId) args[i++]);
        } else {
            o.getId().setSequencia((Integer) args[i++]);
        }
        o.setTipo((TipoMetodo) args[i++]);
        o.setMetodo((BaseObject) args[i++]);

        return o;
    }

    @Override
    public Metodologia getObject(Element e) {
        try {
            Metodologia o = createObject(
                    Integer.parseInt(e.getAttribute("sequencia")),
                    TipoMetodo.of(Integer.parseInt(e.getAttribute("tipoMetodo"))),
                    null);

            DaoPattern dao;
            switch (o.getTipo()) {
                default:
                case TECNICA:
                    dao = TecnicaDaoXML.getInstance();
                    break;
                case RECURSO:
                    dao = RecursoDaoXML.getInstance();
                    break;
                case INSTRUMENTO:
                    dao = InstrumentoAvaliacaoDaoXML.getInstance();
                    break;
            }
            o.setMetodo((BaseObject) dao.findById(new Integer(e.getAttribute("baseObjectId"))));

            return o;
        } catch (Exception ex) {
            Logger.getLogger(MetodologiaFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Metodologia getObject(HashMap<String, Object> p) {
        Metodologia o = createObject(
                p.get("sequencia"),
                p.get("tipo"),
                p.get("metodo")
        );
        o.getId().setDetalhamento((Detalhamento) p.get("detalhamento"));

        return o;
    }

    @Override
    public Node toXml(Document doc, Metodologia o) {
        Element e = (Element) doc.createElement("metodologia");
        e.setAttribute("sequencia", o.getId().getSequencia().toString());
        e.setAttribute("tipoMetodo", String.valueOf(o.getTipo().getValue()));
        e.setAttribute("baseObjectId", o.getMetodo().getId().toString());
        Detalhamento detalhe = o.getId().getDetalhamento();
        e.setAttribute("detalhamentoSequencia", detalhe.getId().getSequencia().toString());
        e.setAttribute("planoDeEnsinoId", detalhe.getId().getPlanoDeEnsino().getId().toString());
        e.setAttribute("unidadeCurricularId", detalhe.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getId().toString());
        e.setAttribute("cursoId", detalhe.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getCurso().getId().getId().toString());
        e.setAttribute("campusId", detalhe.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getCurso().getId().getCampus().getId().toString());

        return e;
    }

}
