/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import ensino.patterns.BaseObject;
import java.util.HashMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author nicho
 */
public class Recurso extends BaseObject {
    
    public Recurso(Element element) {
        super(element);
    }
    
    public Recurso(HashMap<String, Object> params) {
        super(params);
    }
    
    @Override
    public String toString() {
        return nome;
    }
    
    @Override
    public Node toXml(Document doc) {
        return super.toXml(doc, "recurso");
    }
    
}
