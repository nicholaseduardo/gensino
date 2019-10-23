package ensino.configuracoes.model;

import ensino.defaults.XMLInterface;
import ensino.planejamento.model.PlanoDeEnsino;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UnidadeCurricular implements XMLInterface {
    private Integer id;
    private String nome;
    private Integer nAulasTeoricas;
    private Integer nAulasPraticas;
    private Integer cargaHoraria;
    private String ementa;
    private Curso curso;
    
    private List<ReferenciaBibliografica> referencias;
    private List<PlanoDeEnsino> planosDeEnsino;
    
    public UnidadeCurricular(Integer id, String nome, Integer nAulasTeoricas,
            Integer nAulasPraticas, Integer cargaHoraria, String ementa) {
        this.id = id;
        this.nome = nome;
        this.nAulasTeoricas = nAulasTeoricas;
        this.nAulasPraticas = nAulasPraticas;
        this.cargaHoraria = cargaHoraria;
        this.ementa = ementa;
        
        this.referencias = new ArrayList();
        this.planosDeEnsino = new ArrayList();
    }
    
    public UnidadeCurricular() {
        this(null, null, null, null, null, null);
    }
    
    public UnidadeCurricular(Element e) {
        this(
                Integer.parseInt(e.getAttribute("id")),
                e.getAttribute("nome"),
                Integer.parseInt(e.getAttribute("nAulasTeoricas")),
                Integer.parseInt(e.getAttribute("nAulasPraticas")),
                Integer.parseInt(e.getAttribute("cargaHoraria")),
                e.getAttribute("ementa")
        );
        // verifica se tem elementos como referencias bibliograficas
        if (e.hasChildNodes()) {
            NodeList nodeList = e.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node child = nodeList.item(i);
                String nodeName = child.getNodeName();
                if ("referenciaBibliografica".equals(nodeName)) {
                    ReferenciaBibliografica referencia = new ReferenciaBibliografica((Element) child);
                    referencia.setUnidadeCurricular(this);
                    addReferenciaBibliografica(referencia);
                } else if ("planoDeEnsino".equals(nodeName)) {
                    PlanoDeEnsino plano = new PlanoDeEnsino((Element) child);
                    plano.setUnidadeCurricular(this);
                    addPlanoDeEnsino(plano);
                }
            }
        }
    }
    
    public UnidadeCurricular(HashMap<String, Object> params) {
        this(
                (Integer)params.get("id"),
                (String)params.get("nome"),
                (Integer)params.get("nAulasTeoricas"),
                (Integer)params.get("nAulasPraticas"),
                (Integer)params.get("cargaHoraria"),
                (String)params.get("ementa")
        );
        this.curso = (Curso) params.get("curso");
        this.referencias = (List<ReferenciaBibliografica>) params.get("referenciasBibliograficas");
        this.planosDeEnsino = (List<PlanoDeEnsino>) params.get("planosDeEnsino");
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

    public Integer getnAulasTeoricas() {
        return nAulasTeoricas;
    }

    public void setnAulasTeoricas(Integer nAulasTeoricas) {
        this.nAulasTeoricas = nAulasTeoricas;
    }

    public Integer getnAulasPraticas() {
        return nAulasPraticas;
    }

    public void setnAulasPraticas(Integer nAulasPraticas) {
        this.nAulasPraticas = nAulasPraticas;
    }

    public Integer getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(Integer cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public String getEmenta() {
        return ementa;
    }

    public void setEmenta(String ementa) {
        this.ementa = ementa;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
    
    public void addReferenciaBibliografica(ReferenciaBibliografica referencia) {
        referencia.setUnidadeCurricular(this);
        this.referencias.add(referencia);
    }
    
    public void removeReferenciaBibliografica(ReferenciaBibliografica referencia) {
        this.referencias.remove(referencia);
    }

    public List<ReferenciaBibliografica> getReferenciasBibliograficas() {
        return referencias;
    }

    public void setReferenciasBibliograficas(List<ReferenciaBibliografica> referencias) {
        this.referencias = referencias;
    }
    
    public void addPlanoDeEnsino(PlanoDeEnsino planoDeEnsino) {
        planoDeEnsino.setUnidadeCurricular(this);
        this.planosDeEnsino.add(planoDeEnsino);
    }
    
    public void removePlanoDeEnsino(PlanoDeEnsino planoDeEnsino) {
        this.planosDeEnsino.remove(planoDeEnsino);
    }

    public List<PlanoDeEnsino> getPlanosDeEnsino() {
        return planosDeEnsino;
    }

    public void setPlanosDeEnsino(List<PlanoDeEnsino> planosDeEnsino) {
        this.planosDeEnsino = planosDeEnsino;
    }
    
    /**
     * Converte a lista de referências bibliográficas no formato TEXTO
     * 
     * @return 
     */
    public String referenciaBibliograficaToString() {
        StringBuilder sbBasica = new StringBuilder();
            sbBasica.append("--- Bibliografia básica --- \n");
            StringBuilder sbComplementar = new StringBuilder();
            sbComplementar.append("--- Bibliografia complementar --- \n");
            
            for (int i = 0; i < referencias.size(); i++) {
                ReferenciaBibliografica ref = referencias.get(i);
                if (ref.isBasica()) {
                    sbBasica.append(ref.getBibliografia().getReferencia());
                } else {
                    sbComplementar.append(ref.getBibliografia().getReferencia());
                }
            }
            return (sbBasica.toString() + "\n\n" + sbComplementar.toString());
    }

    @Override
    public Node toXml(Document doc) {
        Element e = (Element) doc.createElement("unidadeCurricular");
        e.setAttribute("id", id.toString());
        e.setAttribute("nome", nome);
        e.setAttribute("nAulasTeoricas", nAulasTeoricas.toString());
        e.setAttribute("nAulasPraticas", nAulasPraticas.toString());
        e.setAttribute("cargaHoraria", cargaHoraria.toString());
        e.setAttribute("ementa", ementa);
        
        if (!referencias.isEmpty()) {
            referencias.forEach((ref) -> {
                e.appendChild(ref.toXml(doc));
            });
        }
        if (!planosDeEnsino.isEmpty()) {
            planosDeEnsino.forEach((plano) -> {
                e.appendChild(plano.toXml(doc));
            });
        }
        return e;
    }

    @Override
    public HashMap<String, Object> getKey() {
        HashMap<String, Object> map = new HashMap();
        map.put("id", id);
        map.put("nome", nome);
        map.put("nAulasTeoricas", nAulasTeoricas);
        map.put("nAulasPraticas", nAulasPraticas);
        map.put("cargaHoraria", cargaHoraria);
        map.put("ementa", ementa);
        map.put("curso", curso);
        
        return map;
    }

}
