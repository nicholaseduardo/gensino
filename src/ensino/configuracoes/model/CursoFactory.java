/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import ensino.configuracoes.dao.xml.CampusDaoXML;
import ensino.patterns.factory.BeanFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author nicho
 */
public class CursoFactory implements BeanFactory<Curso> {

    private static CursoFactory instance = null;

    private CursoFactory() {
    }

    public static CursoFactory getInstance() {
        if (instance == null) {
            instance = new CursoFactory();
        }
        return instance;
    }

    @Override
    public Curso getObject(Object... args) {
        Curso c = new Curso();
        int index = 0;
        c.setId((Integer) args[index++]);
        c.setNome((String) args[index++]);
        return c;
    }

    @Override
    public Curso getObject(Element e) {
        Integer id = Integer.parseInt(e.getAttribute("id"));
        Curso c = getObject(id, e.getAttribute("nome"));
        try {
            // Identifica o objeto Pai (Campus)
            String sParentId = e.getAttribute("campusId");
            Integer parentId = sParentId.matches("\\d+")
                    ? Integer.parseInt(sParentId) : null;
            // Aciona o Dao do Campus
            CampusDaoXML campusDaoXML = new CampusDaoXML();
            c.setCampus(campusDaoXML.findById(parentId));

            if (e.hasChildNodes()) {
                NodeList nodeList = e.getChildNodes();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node child = nodeList.item(i);
                    if ("unidadeCurricular".equals(child.getNodeName())) {
                        c.addUnidadeCurricular(new UnidadeCurricular((Element) child));
                    } else if ("turma".equals(child.getNodeName())) {
                        c.addTurma(new Turma((Element) child));
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(CursoFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return c;
    }

    @Override
    public Curso getObject(HashMap<String, Object> p) {
        Curso c = getObject(p.get("id"), p.get("nome"));
        c.setImagem((ImageIcon) p.get("imagem"));
        c.setCampus((Campus) p.get("campus"));
        c.setUnidadesCurriculares((List<UnidadeCurricular>) p.get("unidadesCurriculares"));
        c.setTurmas((List<Turma>) p.get("turmas"));

        return c;
    }

    @Override
    public Node toXml(Document doc, Curso o) {
        Element e = doc.createElement("curso");
        e.setAttribute("id", o.getId().toString());
        e.setAttribute("campusId", o.getCampus().getId().toString());
        e.setAttribute("nome", o.getNome());

        if (!o.getUnidadesCurriculares().isEmpty()) {
            o.getUnidadesCurriculares().forEach((und) -> {
                e.appendChild(und.toXml(doc));
            });
        }

        if (!o.getTurmas().isEmpty()) {
            o.getTurmas().forEach((turma) -> {
                e.appendChild(turma.toXml(doc));
            });
        }

        return e;
    }

}
