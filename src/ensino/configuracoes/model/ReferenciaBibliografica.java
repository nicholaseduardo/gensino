package ensino.configuracoes.model;

import ensino.defaults.XMLInterface;
import java.util.HashMap;
import java.util.Objects;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ReferenciaBibliografica implements XMLInterface {

    public static Integer TIPO_BASICA = 0;
    public static Integer TIPO_COMPLEMENTAR = 1;
    
    private Integer sequencia;
    private Integer tipo;
    private UnidadeCurricular unidadeCurricular;
    private Bibliografia bibliografia;

    public ReferenciaBibliografica() {
        this(null, null, null, null);
    }

    public ReferenciaBibliografica(Integer sequencia, Integer tipo, UnidadeCurricular unidadeCurricular, Bibliografia bibliografia) {
        this.sequencia = sequencia;
        this.tipo = tipo;
        this.unidadeCurricular = unidadeCurricular;
        this.bibliografia = bibliografia;
    }

    public ReferenciaBibliografica(Element e) {
        this(Integer.parseInt(e.getAttribute("sequencia")),
                Integer.parseInt(e.getAttribute("tipo")), null, null);
        
        if (e.hasChildNodes()) {
            Element nodeBibliografia = (Element) e.getFirstChild();
            this.bibliografia = new Bibliografia(nodeBibliografia);
        }
    }

    public ReferenciaBibliografica(HashMap<String, Object> params) {
        this(
                (Integer) params.get("sequencia"),
                (Integer) params.get("tipo"),
                (UnidadeCurricular) params.get("unidadeCurricular"),
                (Bibliografia) params.get("bibliografia")
        );
    }

    public Integer getSequencia() {
        return sequencia;
    }

    public void setSequencia(Integer sequencia) {
        this.sequencia = sequencia;
    }

    /**
     * Recupera o tipo de referência da bibliografia.<br/> Pode ser:
     * <code>ReferenciaBibliografica.TIPO_BASICA</code> ou
     * <code>ReferenciaBibliografica.TIPO_COMPLEMENTAR</code>
     * @return 
     */
    public Integer getTipo() {
        return tipo;
    }
    
    public String getTipoDescricao() {
        switch (tipo) {
            case 0: return "Básica";
            case 1: return "Complementar";
        }
        return "";
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }
    
    public boolean isBasica() {
        return Objects.equals(tipo, TIPO_BASICA);
    }

    public UnidadeCurricular getUnidadeCurricular() {
        return unidadeCurricular;
    }

    public void setUnidadeCurricular(UnidadeCurricular unidadeCurricular) {
        this.unidadeCurricular = unidadeCurricular;
    }

    public Bibliografia getBibliografia() {
        return bibliografia;
    }

    public void setBibliografia(Bibliografia bibliografia) {
        this.bibliografia = bibliografia;
    }

    @Override
    public Node toXml(Document doc) {
        Element e = doc.createElement("referenciaBibliografica");
        e.setAttribute("sequencia", sequencia.toString());
        e.setAttribute("tipo", tipo.toString());
        e.appendChild(bibliografia.toXml(doc));
        return e;
    }
    
    @Override
    public HashMap<String, Object> getKey() {
        HashMap<String, Object> map = new HashMap();
        map.put("sequencia", sequencia);
        map.put("tipo", tipo);
        map.put("unidadeCurricular", unidadeCurricular);
        map.put("bibliografia", bibliografia);
        return map;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", tipo == TIPO_BASICA ? "Básica" : "Complementar", bibliografia.toString());
    }
}
