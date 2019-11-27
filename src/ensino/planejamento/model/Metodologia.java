package ensino.planejamento.model;

import ensino.configuracoes.model.InstrumentoAvaliacao;
import ensino.configuracoes.model.Recurso;
import ensino.configuracoes.model.Tecnica;
import ensino.defaults.XMLInterface;
import ensino.patterns.BaseObject;
import ensino.util.types.TipoMetodo;
import java.util.HashMap;
import java.util.Objects;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Metodologia implements XMLInterface {

    private Integer sequencia;
    private TipoMetodo tipoMetodo;
    // parent
    private Detalhamento detalhamento;
    // child
    private BaseObject metodo;

    public Metodologia(Integer sequencia, TipoMetodo tipo, Detalhamento detalhamento, BaseObject metodo) {
        this.sequencia = sequencia;
        this.tipoMetodo = tipo;
        this.detalhamento = detalhamento;
        this.metodo = metodo;
    }

    public Metodologia() {
        this(null, null, null, null);
    }

    public Metodologia(Element e) {
        this(Integer.parseInt(e.getAttribute("sequencia")),
                null, null, null
        );
        String sTipo = e.getAttribute("tipo");
        if (sTipo.matches("\\d")) {
            tipoMetodo = TipoMetodo.values()[Integer.parseInt(sTipo)];
        }

        if (e.hasChildNodes()) {
            Element eChild = (Element) e.getFirstChild();
            switch (tipoMetodo) {
                case TECNICA:
                    this.metodo = new Tecnica(eChild);
                    break;
                case RECURSO:
                    this.metodo = new Recurso(eChild);
                    break;
                default:
                    this.metodo = new InstrumentoAvaliacao(eChild);
                    break;
            }
        }
    }

    public Metodologia(HashMap<String, Object> params) {
        this(
                (Integer) params.get("sequencia"),
                (TipoMetodo) params.get("tipo"),
                (Detalhamento) params.get("detalhamento"),
                (BaseObject) params.get("metodo")
        );
    }

    public Integer getSequencia() {
        return sequencia;
    }

    public void setSequencia(Integer sequencia) {
        this.sequencia = sequencia;
    }

    public TipoMetodo getTipo() {
        return tipoMetodo;
    }

    public void setTipo(TipoMetodo tipo) {
        this.tipoMetodo = tipo;
    }

    public Detalhamento getDetalhamento() {
        return detalhamento;
    }

    public void setDetalhamento(Detalhamento curso) {
        this.detalhamento = curso;
    }

    public BaseObject getMetodo() {
        return metodo;
    }

    public void setMetodo(BaseObject metodo) {
        this.metodo = metodo;
    }

    public boolean isTecnica() {
        return TipoMetodo.TECNICA.equals(tipoMetodo);
    }

    @Override
    public Node toXml(Document doc) {
        Element e = (Element) doc.createElement("metodologia");
        e.setAttribute("sequencia", sequencia.toString());
        e.setAttribute("tipo", String.valueOf(tipoMetodo.getTipo()));
        e.appendChild(metodo.toXml(doc));
        return e;
    }

    @Override
    public HashMap<String, Object> getKey() {
        HashMap<String, Object> map = new HashMap();
        map.put("id", sequencia);

        return map;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", tipoMetodo.toString(), metodo.getNome());
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
        final Metodologia other = (Metodologia) obj;
        if (this.tipoMetodo != other.tipoMetodo) {
            return false;
        }
        if (!Objects.equals(this.detalhamento, other.detalhamento)) {
            return false;
        }
        if (!Objects.equals(this.metodo, other.metodo)) {
            return false;
        }
        return true;
    }

}
