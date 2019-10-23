package ensino.planejamento.model;

import ensino.configuracoes.model.SemanaLetiva;
import ensino.defaults.XMLInterface;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Detalhamento implements XMLInterface {

    private Integer sequencia;
    private Integer nAulasPraticas;
    private Integer nAulasTeoricas;
    private String conteudo;
    private String observacao;
    // parent
    private PlanoDeEnsino planoDeEnsino;
    // composition
    private SemanaLetiva semanaLetiva;
    private List<Metodologia> metodologias;
    private List<Objetivo> objetivos;

    public Detalhamento() {
        this(null, 0, 0, " ", " ", null, null);
    }

    public Detalhamento(Integer sequencia, Integer nAulasPraticas, Integer nAulasTeoricas,
            String conteudo, String observacao, PlanoDeEnsino planoDeEnsino,
            SemanaLetiva semanaLetiva) {
        this.sequencia = sequencia;
        this.nAulasPraticas = nAulasPraticas;
        this.nAulasTeoricas = nAulasTeoricas;
        this.conteudo = conteudo;
        this.observacao = observacao;
        this.planoDeEnsino = planoDeEnsino;
        this.semanaLetiva = semanaLetiva;
        
        this.metodologias = new ArrayList();
        this.objetivos = new ArrayList();
    }

    public Detalhamento(Element e) throws ParseException {
        this(
                Integer.parseInt(e.getAttribute("sequencia")),
                Integer.parseInt(e.getAttribute("nAulasPraticas")),
                Integer.parseInt(e.getAttribute("nAulasTeoricas")),
                e.getAttribute("conteudo"),
                e.getAttribute("observacao"), null, null);
        if (e.hasChildNodes()) {
            NodeList nodeList = e.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element child = (Element) nodeList.item(i);
                if ("metodologia".equals(child.getNodeName())) {
                    this.addMetodologia(new Metodologia((Element)child));
                } else if ("objetivo".equals(child.getNodeName())) {
                    this.addObjetivo(new Objetivo((Element)child));
                } else if ("semanaLetiva".equals(child.getNodeName())) {
                    semanaLetiva = new SemanaLetiva((Element)child);
                }
            }
        }
    }

    public Detalhamento(HashMap<String, Object> params) {
        this(
                (Integer) params.get("sequencia"),
                (Integer) params.get("nAulasPraticas"),
                (Integer) params.get("nAulasTeoricas"),
                (String) params.get("conteudo"),
                (String) params.get("observacao"),
                (PlanoDeEnsino) params.get("planoDeEnsino"),
                (SemanaLetiva) params.get("semanaLetiva")
        );
        this.metodologias = (List<Metodologia>) params.get("metodologias");
        this.objetivos = (List<Objetivo>) params.get("objetivos");
    }

    public Integer getSequencia() {
        return sequencia;
    }

    public void setSequencia(Integer sequencia) {
        this.sequencia = sequencia;
    }

    public Integer getNAulasPraticas() {
        return nAulasPraticas;
    }

    public void setNAulasPraticas(Integer nAulasPraticas) {
        this.nAulasPraticas = nAulasPraticas;
    }

    public Integer getNAulasTeoricas() {
        return nAulasTeoricas;
    }

    public void setNAulasTeoricas(Integer nAulasTeoricas) {
        this.nAulasTeoricas = nAulasTeoricas;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public PlanoDeEnsino getPlanoDeEnsino() {
        return planoDeEnsino;
    }

    public void setPlanoDeEnsino(PlanoDeEnsino plano) {
        this.planoDeEnsino = plano;
    }

    public SemanaLetiva getSemanaLetiva() {
        return semanaLetiva;
    }

    public void setSemanaLetiva(SemanaLetiva semanaLetiva) {
        this.semanaLetiva = semanaLetiva;
    }
    
    public void addMetodologia(Metodologia metodologia) {
        metodologia.setDetalhamento(this);
        this.metodologias.add(metodologia);
    }
    
    public void removeMetodologia(Metodologia metodologia) {
        this.metodologias.remove(metodologia);
    }

    public List<Metodologia> getMetodologias() {
        return metodologias;
    }

    public void setMetodologias(List<Metodologia> metodologias) {
        this.metodologias = metodologias;
    }
    
    public void addObjetivo(Objetivo objetivo) {
        this.objetivos.add(objetivo);
    }
    
    public void removeObjetivo(Objetivo objetivo) {
        this.objetivos.remove(objetivo);
    }

    public List<Objetivo> getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(List<Objetivo> objetivos) {
        this.objetivos = objetivos;
    }

    @Override
    public Node toXml(Document doc) {
        Element e = doc.createElement("detalhamento");
        e.setAttribute("sequencia", sequencia.toString());
        e.setAttribute("nAulasPraticas", nAulasPraticas.toString());
        e.setAttribute("nAulasTeoricas", nAulasTeoricas.toString());
        e.setAttribute("conteudo", conteudo);
        e.setAttribute("observacao", observacao);
        
        e.appendChild(semanaLetiva.toXml(doc));

        // composition
        if (!metodologias.isEmpty()) {
            metodologias.forEach((metodo) -> {
                e.appendChild(metodo.toXml(doc));
            });
        }
        
        if (!objetivos.isEmpty()) {
            objetivos.forEach((objetivo) -> {
                e.appendChild(objetivo.toXml(doc));
            });
        }

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
        int hash = 3;
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
        final Detalhamento other = (Detalhamento) obj;
        if (!Objects.equals(this.conteudo, other.conteudo)) {
            return false;
        }
        if (!Objects.equals(this.observacao, other.observacao)) {
            return false;
        }
        if (!Objects.equals(this.sequencia, other.sequencia)) {
            return false;
        }
        if (!Objects.equals(this.nAulasPraticas, other.nAulasPraticas)) {
            return false;
        }
        if (!Objects.equals(this.nAulasTeoricas, other.nAulasTeoricas)) {
            return false;
        }
        if (!Objects.equals(this.planoDeEnsino, other.planoDeEnsino)) {
            return false;
        }
        if (!Objects.equals(this.semanaLetiva, other.semanaLetiva)) {
            return false;
        }
        return true;
    }

}
