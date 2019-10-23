package ensino.planejamento.model;

import ensino.defaults.XMLInterface;
import java.util.HashMap;
import java.util.Objects;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Objetivo implements XMLInterface {

    private Integer sequencia;
    private String descricao;
    // parent
    private PlanoDeEnsino planoDeEnsino;
    
    public Objetivo() {
        this(null, null, null);
    }
    
    public Objetivo(PlanoDeEnsino planoDeEnsino) {
        this(null, null, planoDeEnsino);
    }
    
    public Objetivo(Integer sequencia, String descricao, PlanoDeEnsino plano) {
        this.sequencia = sequencia;
        this.descricao = descricao;
        this.planoDeEnsino = plano;
    }
    
    public Objetivo(Element e) {
        this(
            Integer.parseInt(e.getAttribute("sequencia")),
            e.getAttribute("descricao"), null);
        
    }
    
    public Objetivo(HashMap<String, Object> params) {
        this(
                (Integer)params.get("sequencia"),
                (String)params.get("descricao"), null
        );
        this.planoDeEnsino = (PlanoDeEnsino) params.get("planoDeEnsino");
    }

    public Integer getSequencia() {
        return sequencia;
    }

    public void setSequencia(Integer sequencia) {
        this.sequencia = sequencia;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public PlanoDeEnsino getPlanoDeEnsino() {
        return planoDeEnsino;
    }

    public void setPlanoDeEnsino(PlanoDeEnsino plano) {
        this.planoDeEnsino = plano;
    }
    
    @Override
    public String toString() {
        return String.format("[%d] %s", this.sequencia, this.descricao.substring(0, 50));
    }
    
    @Override
    public Node toXml(Document doc) {
        Element e = doc.createElement("objetivo");
        e.setAttribute("sequencia", sequencia.toString());
        e.setAttribute("descricao", descricao);

        return e;
    }

    @Override
    public HashMap<String, Object> getKey() {
        HashMap<String, Object> map = new HashMap();
        map.put("sequencia", sequencia);

        return map;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final Objetivo other = (Objetivo) obj;
        if (!Objects.equals(this.descricao, other.descricao)) {
            return false;
        }
        if (!Objects.equals(this.sequencia, other.sequencia)) {
            return false;
        }
        return true;
    }

}
