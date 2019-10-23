package ensino.configuracoes.model;

import ensino.defaults.XMLInterface;
import java.util.HashMap;
import java.util.Objects;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Estudante implements XMLInterface {
    private Integer id;
    private String nome;
    private String registro;
    // parent
    private Turma turma;
    
    public Estudante(Integer id, String nome, String registro) {
        this.id = id;
        this.nome = nome;
        this.registro = registro;
    }
    
    public Estudante() {
        this(null, null, null);
    }
    
    public Estudante(Element e) {
        this(
                Integer.parseInt(e.getAttribute("id")),
                e.getAttribute("nome"),
                e.getAttribute("registro")
        );
    }
    
    public Estudante(HashMap<String, Object> params) {
        this(
                (Integer)params.get("id"),
                (String)params.get("nome"),
                (String)params.get("registro")
        );
        this.turma = (Turma) params.get("turma");
    }

    public Integer getId() {
        return id;
    }
    
    public boolean issetId() {
        return id != null;
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

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    @Override
    public Node toXml(Document doc) {
        Element e = (Element) doc.createElement("estudante");
        e.setAttribute("id", id.toString());
        e.setAttribute("nome", nome);
        e.setAttribute("registro", registro);
        return e;
    }

    @Override
    public HashMap<String, Object> getKey() {
        HashMap<String, Object> map = new HashMap();
        map.put("id", id);
        map.put("turma", turma);
        
        return map;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.id);
        hash = 23 * hash + Objects.hashCode(this.nome);
        hash = 23 * hash + Objects.hashCode(this.registro);
        hash = 23 * hash + Objects.hashCode(this.turma);
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
        final Estudante other = (Estudante) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.turma, other.turma)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return nome;
    }

}
