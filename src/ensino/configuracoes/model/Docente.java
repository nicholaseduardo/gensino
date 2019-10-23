/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import ensino.patterns.BaseObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author nicho
 */
public class Docente extends BaseObject {

    public Docente() {
        super();
    }

    public Docente(Integer id, String nome) {
        super(id, nome);
    }

    public Docente(Element element) {
        super(element);
    }

    public Docente(HashMap<String, Object> params) {
        super(params);
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public Node toXml(Document doc) {
        return super.toXml(doc, "docente");
    }

}
