package ensino.configuracoes.model;

import ensino.defaults.XMLInterface;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Objects;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Legenda implements XMLInterface {

    private Integer id;
    private String nome;
    private Boolean letivo;
    private Boolean informativo;
    private Color cor;
    private List<Atividade> atividades;

    public Legenda(Integer id) {
        this(id, null, false, true, null);
    }

    public Legenda(Integer id, String nome, boolean letivo, boolean informativo, Color cor) {
        this.id = id;
        this.nome = nome;
        this.letivo = letivo;
        this.informativo = informativo;
        this.cor = cor;
        this.atividades = new ArrayList<>();
    }

    public Legenda(Element e) {
        this(
                Integer.parseInt(e.getAttribute("id")),
                e.getAttribute("nome"),
                Boolean.parseBoolean(e.getAttribute("letivo")),
                Boolean.parseBoolean(e.getAttribute("informativo")),
                new Color(Integer.parseInt(e.getAttribute("cor")))
        );
    }

    public Legenda(HashMap<String, Object> params) {
        this(
                (Integer) params.get("id"),
                (String) params.get("nome"),
                (Boolean) params.get("letivo"),
                (Boolean) params.get("informativo"),
                (Color) params.get("cor")
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

    public Boolean isLetivo() {
        return letivo;
    }

    public void setLetivo(Boolean letivo) {
        this.letivo = letivo;
    }

    public Boolean isInformativo() {
        return informativo;
    }

    public void setInformativo(Boolean informativo) {
        this.informativo = informativo;
    }

    public Color getCor() {
        return cor;
    }

    public void setCor(Color cor) {
        this.cor = cor;
    }

    public List<Atividade> getAtividades() {
        return atividades;
    }

    public void setAtividades(List<Atividade> atividades) {
        this.atividades = atividades;
    }

    public void addAtividade(Atividade at) {
        atividades.add(at);
    }

    public void removeAtividade(Atividade at) {
        atividades.remove(at);
    }

    @Override
    public Node toXml(Document doc) {
        Element e = doc.createElement("legenda");
        e.setAttribute("id", id.toString());
        e.setAttribute("nome", nome);
        e.setAttribute("letivo", letivo.toString());
        e.setAttribute("informativo", informativo.toString());
        e.setAttribute("cor", String.valueOf(cor.getRGB()));
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
        final Legenda other = (Legenda) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.letivo, other.letivo)) {
            return false;
        }
        if (!Objects.equals(this.informativo, other.informativo)) {
            return false;
        }
        if (!Objects.equals(this.cor, other.cor)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nome;
    }
}
