package ensino.planejamento.model;

import ensino.configuracoes.model.Estudante;
import ensino.configuracoes.model.InstrumentoAvaliacao;
import ensino.defaults.XMLInterface;
import ensino.helpers.DateHelper;
import ensino.util.Bimestre;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PlanoAvaliacao implements XMLInterface {
    /**
     * Atributo utilizado para identificar a sequência de inclusão
     * dos instrumentos avaliativos.
     */
    private Integer sequencia;
    /**
     * Atributo utilizado para identificar nominalmente a avaliação
     * a ser aplicada ao estudante.
     */
    private String nome;
    /**
     * Atributo utilizado para identificar o bimestre no qual o
     * estudante será avaliado
     */
    private Bimestre bimestre;
    /**
     * Atributo utilizado para atribuir um peso a nota do 
     * estudante
     */
    private Double peso;
    /**
     * Atributo utilizado para registra a nota máxima a ser atribuída
     * a avaliação
     */
    private Double valor;
    /**
     * Atributo utilizado para registrar a data na qual a avaliação
     * será aplicada.
     */
    private Date data;
    /**
     * Atributo utilizado para identifica a qual plano de ensino
     * pertence esse plano de avaliações
     */
    private PlanoDeEnsino planoDeEnsino;
    /**
     * Atributo utilizado para identificar qual o instrumento 
     * avaliativo será utilizado no processo de avaliação do estudante
     */
    private InstrumentoAvaliacao instrumentoAvaliacao;
    /**
     * Atributo utilizado para identificar qual objetivo está sendo
     * medido ao aplicar a avaliação
     */
    private Objetivo objetivo;
    /**
     * Atributo utilizado para armazenas as notas das avaliações
     * por estudante
     */
    private List<Avaliacao> avaliacoes;

    public PlanoAvaliacao() {
        this(null, null, null, null, null, null);
    }

    public PlanoAvaliacao(Integer sequencia, String nome,
            Bimestre bimestre, Double peso, Double valor,
            Date data) {
        this.sequencia = sequencia;
        this.nome = nome;
        this.bimestre = bimestre;
        this.peso = peso;
        this.valor = valor;
        this.data = data;
        this.avaliacoes = new ArrayList<>();
    }

    public PlanoAvaliacao(Element e, PlanoDeEnsino planoDeEnsino) throws ParseException {
        this(
                Integer.parseInt(e.getAttribute("sequencia")),
                e.getAttribute("nome"),
                null,
                Double.parseDouble(e.getAttribute("peso")),
                Double.parseDouble(e.getAttribute("valor")),
                DateHelper.stringToDate(e.getAttribute("data"), "dd/MM/yyyy"));
        this.planoDeEnsino = planoDeEnsino;
        String sBim = e.getAttribute("bimestre");
        if (sBim.matches("\\d")) {
            Bimestre b = Bimestre.values()[Integer.parseInt(sBim)];
            bimestre = b;
        }
        if (e.hasChildNodes()) {
            NodeList nodeList = e.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node child = nodeList.item(i);
                if ("instrumento".equals(child.getNodeName())) {
                    this.instrumentoAvaliacao = new InstrumentoAvaliacao((Element) child);
                } else if ("objetivo".equals(child.getNodeName())) {
                    this.objetivo = new Objetivo((Element) child);
                } else if ("avaliacao".equals(child.getNodeName())) {
                    addAvaliacao(new Avaliacao((Element) child, this));
                }
            }
        }
    }

    public PlanoAvaliacao(HashMap<String, Object> params) {
        this(
                (Integer) params.get("sequencia"),
                (String) params.get("nome"),
                (Bimestre) params.get("bimestre"),
                (Double) params.get("peso"),
                (Double) params.get("valor"),
                (Date) params.get("data")
        );
        this.instrumentoAvaliacao = (InstrumentoAvaliacao) params.get("instrumentoAvaliacao");
        this.objetivo = (Objetivo) params.get("objetivo");
        this.planoDeEnsino = (PlanoDeEnsino) params.get("planoDeEnsino");
        this.avaliacoes = (List<Avaliacao>) params.get("avaliacoes");
    }

    @Override
    public Node toXml(Document doc) {
        Element e = doc.createElement("planoAvaliacao");
        e.setAttribute("sequencia", sequencia.toString());
        e.setAttribute("nome", nome);
        e.setAttribute("bimestre", String.valueOf(bimestre.getValor()));
        e.setAttribute("peso", peso.toString());
        e.setAttribute("valor", valor.toString());
        e.setAttribute("data", DateHelper.dateToString(data, "dd/MM/yyyy"));

        e.appendChild(instrumentoAvaliacao.toXml(doc));
        if (objetivo != null) {
            e.appendChild(objetivo.toXml(doc));
        }
        if (!avaliacoes.isEmpty()) {
            avaliacoes.forEach((avaliacao) -> {
                e.appendChild(avaliacao.toXml(doc));
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
        final PlanoAvaliacao other = (PlanoAvaliacao) obj;
        if (!Objects.equals(this.data, other.data)) {
            return false;
        }
        return true;
    }

    public Integer getSequencia() {
        return sequencia;
    }

    public void setSequencia(Integer sequencia) {
        this.sequencia = sequencia;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Bimestre getBimestre() {
        return bimestre;
    }

    public void setBimestre(Bimestre bimestre) {
        this.bimestre = bimestre;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getNota() {
        return valor * peso;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public PlanoDeEnsino getPlanoDeEnsino() {
        return planoDeEnsino;
    }

    public void setPlanoDeEnsino(PlanoDeEnsino plano) {
        this.planoDeEnsino = plano;
    }

    public InstrumentoAvaliacao getInstrumentoAvaliacao() {
        return instrumentoAvaliacao;
    }

    public void setInstrumentoAvaliacao(InstrumentoAvaliacao instrumentoAvaliacao) {
        this.instrumentoAvaliacao = instrumentoAvaliacao;
    }

    public Objetivo getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(Objetivo objetivo) {
        this.objetivo = objetivo;
    }
    
    public void addAvaliacao(Avaliacao avaliacao) {
        avaliacao.setPlanoAvaliacao(this);
        avaliacoes.add(avaliacao);
    }
    
    public void removeAvaliacao(Avaliacao avaliacao) {
        avaliacoes.remove(avaliacao);
    }

    public List<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(List<Avaliacao> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }
    
    public boolean hasAvaliacoes() {
        return !avaliacoes.isEmpty();
    }
    
    public void criarAvaliacoes(List<Estudante> estudantes) {
        estudantes.forEach((estudante) -> {
            Avaliacao avaliacao = new Avaliacao();
            avaliacao.setPlanoAvaliacao(this);
            avaliacao.setEstudante(estudante);
            addAvaliacao(avaliacao);
        });
    }

}
