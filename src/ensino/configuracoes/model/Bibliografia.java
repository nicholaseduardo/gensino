package ensino.configuracoes.model;

import ensino.defaults.XMLInterface;
import java.util.HashMap;
import java.util.Objects;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Bibliografia implements XMLInterface {

    private Integer id;
    private String titulo;
    private String autor;
    private String referencia;

    public Bibliografia(Integer id) {
        this(id, null, null, null);
    }

    public Bibliografia(Integer id, String titulo, String autor, String referencia) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.referencia = referencia;
    }

    public Bibliografia(Element e) {
        this(
                Integer.parseInt(e.getAttribute("id")),
                e.getAttribute("titulo"),
                e.getAttribute("autor"),
                e.getAttribute("referencia")
        );
    }

    public Bibliografia(HashMap<String, Object> params) {
        this(
                (Integer) params.get("id"),
                (String) params.get("titulo"),
                (String) params.get("autor"),
                (String) params.get("referencia")
        );
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    @Override
    public Node toXml(Document doc) {
        Element e = doc.createElement("bibliografia");
        e.setAttribute("id", id.toString());
        e.setAttribute("titulo", titulo);
        e.setAttribute("autor", autor);
        e.setAttribute("referencia", referencia);
        return e;
    }
    
    @Override
    public HashMap<String, Object> getKey() {
        HashMap<String, Object> map = new HashMap();
        map.put("id", id);
        return map;
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final Bibliografia other = (Bibliografia) obj;
        if (!Objects.equals(this.titulo, other.titulo)) {
            return false;
        }
        if (!Objects.equals(this.autor, other.autor)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.referencia, other.referencia)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", titulo, autor);
    }
}
