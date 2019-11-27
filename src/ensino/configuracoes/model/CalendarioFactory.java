/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import ensino.configuracoes.dao.xml.CampusDaoXML;
import ensino.patterns.factory.BeanFactory;
import ensino.planejamento.model.PlanoDeEnsino;
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
public class CalendarioFactory implements BeanFactory<Calendario> {

    private static CalendarioFactory instance = null;

    private CalendarioFactory() {
    }

    public static CalendarioFactory getInstance() {
        if (instance == null) {
            instance = new CalendarioFactory();
        }
        return instance;
    }

    @Override
    public Calendario getObject(Object... args) {
        Calendario c = new Calendario();
        int index = 0;
        c.setAno((Integer) args[index++]);
        c.setDescricao((String) args[index++]);
        return c;
    }

    @Override
    public Calendario getObject(Element e) {
        Calendario c = getObject(
                Integer.parseInt(e.getAttribute("ano")),
                e.getAttribute("descricao"));
        try {
            // Identifica o objeto Pai (Campus)
            String sParentId = e.getAttribute("campusId");
            Integer parentId = sParentId.matches("\\d+")
                    ? Integer.parseInt(sParentId) : null;
            // Aciona o Dao do Campus
            CampusDaoXML campusDaoXML = new CampusDaoXML();
            c.setCampus(campusDaoXML.findById(parentId));
        } catch (Exception ex) {
            Logger.getLogger(CursoFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return c;
    }

    @Override
    public Calendario getObject(HashMap<String, Object> p) {
        Calendario c = getObject(p.get("ano"), p.get("descricao"));
        c.setCampus((Campus) p.get("campus"));
        c.setAtividade((List<Atividade>) p.get("atividades"));
        c.setPeriodosLetivos((List<PeriodoLetivo>) p.get("periodosLetivos"));
        c.setPlanosDeEnsino((List<PlanoDeEnsino>) p.get("planoDeEnsino"));
        
        return c;
    }

    @Override
    public Node toXml(Document doc, Calendario o) {
        Element e = doc.createElement("calendario");
        e.setAttribute("ano", o.getAno().toString());
        e.setAttribute("campusId", o.getCampus().getId().toString());
        e.setAttribute("descricao", o.getDescricao());

        return e;
    }

}
