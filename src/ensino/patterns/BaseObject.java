/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.patterns;

import ensino.defaults.XMLInterface;
import java.util.HashMap;
import java.util.Objects;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author nicho
 */
public abstract class BaseObject implements XMLInterface {
    protected Integer id;
    protected String nome;
    
    public BaseObject() {
        id = null;
        nome = "";
    }
    
    public BaseObject(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }
    
    public BaseObject(Element element) {
        this(
                Integer.parseInt(element.getAttribute("id")),
                element.getAttribute("nome")
        );
    }
    
    public BaseObject(HashMap<String, Object> params) {
        this(
                (Integer)params.get("id"),
                (String)params.get("nome")
        );
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    /**
     * Converte o objeto da classe <code>Object</code> em um 
     * objeto da classe <code>Node</code>.
     * Deve ser reescrita para as classe que tenham atributos distintos do
     * proposto nesta classe.
     * 
     * @param doc   Objeto da classe <code>Document</code> que representa o arquivo XML
     * @param elementName Nome do nó a ser adicionado no arquivo
     * @return 
     */
    protected Node toXml(Document doc, String elementName) {
        Element element = doc.createElement(elementName);
        element.setAttribute("id", id.toString());
        element.setAttribute("nome", this.nome);
        return element;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.nome);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BaseObject other = (BaseObject) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        return Objects.equals(this.id, other.id);
    }
    
    @Override
    public HashMap<String, Object> getKey() {
        HashMap<String, Object> map = new HashMap();
        map.put("id", id);
        return map;
    }
    
}
