package ensino.configuracoes.model;

import ensino.defaults.XMLInterface;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Turma implements XMLInterface {

    private Integer id;
    private String nome;
    private Integer ano;
    // parent
    private Curso curso;
    // aggregation
    private List<Estudante> estudantes;

    public Turma(Integer id, String nome, Integer ano) {
        this.id = id;
        this.nome = nome;
        this.ano = ano;
        this.estudantes = new ArrayList<>();
    }

    public Turma() {
        this(null, null, null);
    }

    public Turma(Element e) {
        this(
                Integer.parseInt(e.getAttribute("id")),
                e.getAttribute("nome"),
                Integer.parseInt(e.getAttribute("ano"))
        );
        if (e.hasChildNodes()) {
            NodeList nodes = e.getChildNodes();
            for(int i = 0; i < nodes.getLength(); i++) {
                Node child = nodes.item(i);
                if ("estudante".equals(child.getNodeName())) {
                    addEstudante(new Estudante((Element) child));
                }
            }
        }
    }

    public Turma(HashMap<String, Object> params) {
        this(
                (Integer) params.get("id"),
                (String) params.get("nome"),
                (Integer) params.get("ano")
        );
        this.curso = (Curso) params.get("curso");
        this.estudantes = (List<Estudante>) params.get("estudantes");
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

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
    
    public boolean hasEstudantes() {
        return !estudantes.isEmpty();
    }

    public void addEstudante(Estudante estudante) {
        estudante.setTurma(this);
        estudantes.add(estudante);
    }

    public void removeEstudante(Estudante estudante) {
        estudantes.remove(estudante);
    }

    public void updateEstudante(Estudante estudante) {
        for(Estudante student : estudantes) {
            if (student.getId().equals(estudante.getId())) {
                student = estudante;
                break;
            }
        }
    }

    public List<Estudante> getEstudantes() {
        return estudantes;
    }

    public void setEstudantes(List<Estudante> estudantes) {
        this.estudantes = estudantes;
    }

    @Override
    public Node toXml(Document doc) {
        Element e = (Element) doc.createElement("turma");
        e.setAttribute("id", id.toString());
        e.setAttribute("nome", nome);
        e.setAttribute("ano", ano.toString());
        
        for(Estudante estudante : estudantes) {
            e.appendChild(estudante.toXml(doc));
        }
        return e;
    }

    @Override
    public HashMap<String, Object> getKey() {
        HashMap<String, Object> map = new HashMap();
        map.put("id", id);
        map.put("curso", curso);

        return map;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.id);
        hash = 83 * hash + Objects.hashCode(this.nome);
        hash = 83 * hash + Objects.hashCode(this.ano);
        hash = 83 * hash + Objects.hashCode(this.curso);
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
        final Turma other = (Turma) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.ano, other.ano)) {
            return false;
        }
        if (!Objects.equals(this.curso, other.curso)) {
            return false;
        }
        return true;
    }

}
