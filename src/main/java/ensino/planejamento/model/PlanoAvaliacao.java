package ensino.planejamento.model;

import ensino.configuracoes.model.Estudante;
import ensino.configuracoes.model.EtapaEnsino;
import ensino.configuracoes.model.InstrumentoAvaliacao;
import ensino.util.BimestreConverter;
import ensino.util.types.Bimestre;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "planoAvaliacao")
public class PlanoAvaliacao implements Serializable {

    @EmbeddedId
    private PlanoAvaliacaoId id;

    /**
     * Atributo utilizado para identificar nominalmente a avaliação a ser
     * aplicada ao estudante.
     */
    @Column(name = "nome")
    private String nome;

    /**
     * Atributo utilizado para identificar qual a etapa de ensino será utilizada
     * no processo de avaliação do estudante
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns(value = {
        @JoinColumn(name = "nivelEnsino_id"),
        @JoinColumn(name = "etapaEnsino_id")
    })
    private EtapaEnsino etapaEnsino;

    @Deprecated
    @Column(name = "bimestre", nullable = true)
    @Convert(converter = BimestreConverter.class)
    private Bimestre bimestre;

    /**
     * Atributo utilizado para atribuir um peso a nota do estudante
     */
    @Column(name = "peso", columnDefinition = "NUMERIC", precision = 2)
    private Double peso;
    /**
     * Atributo utilizado para registra a nota máxima a ser atribuída a
     * avaliação
     */
    @Column(name = "valor", columnDefinition = "NUMERIC", precision = 2)
    private Double valor;
    /**
     * Atributo utilizado para registrar a data na qual a avaliação será
     * aplicada.
     */
    @Column(name = "data_plano", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date data;
    /**
     * Atributo utilizado para identificar qual o instrumento avaliativo será
     * utilizado no processo de avaliação do estudante
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "instrumentoAvaliacao_id")
    private InstrumentoAvaliacao instrumentoAvaliacao;

    /**
     * Atributo utilizado para identificar qual objetivo está sendo medido ao
     * aplicar a avaliação
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns(value = {
        @JoinColumn(name = "planoDeEnsino_id"),
        @JoinColumn(name = "objetivo_id")
    })
    private Objetivo objetivo;

    /**
     * Atributo utilizado para armazenas as notas das avaliações por estudante
     */
    @OneToMany(mappedBy = "id.planoAvaliacao",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH},
            fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Avaliacao> avaliacoes;

    /**
     * Atributo utilizado para marcar o objeto para exclusão
     */
    @Transient
    private Boolean deleted;

    public PlanoAvaliacao() {
        id = new PlanoAvaliacaoId();
        this.avaliacoes = new ArrayList<>();
        this.peso = 0.0;
        this.valor = 0.0;
        this.deleted = false;
        this.bimestre = Bimestre.PRIMEIRO;
    }

    public Boolean isDeleted() {
        return this.deleted;
    }

    public void delete() {
        this.deleted = true;
    }

    public PlanoAvaliacaoId getId() {
        return id;
    }

    public void setId(PlanoAvaliacaoId id) {
        this.id = id;
    }
    
    public PlanoDeEnsino getPlanoDeEnsino() {
        if (id != null)
            return id.getPlanoDeEnsino();
        return null;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public EtapaEnsino getEtapaEnsino() {
        return etapaEnsino;
    }

    public void setEtapaEnsino(EtapaEnsino etapaEnsino) {
        this.etapaEnsino = etapaEnsino;
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
        avaliacao.getId().setPlanoAvaliacao(this);
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
        if (!hasAvaliacoes()) {
            estudantes.forEach((estudante) -> {
                Avaliacao avaliacao = AvaliacaoFactory.getInstance()
                        .createObject(new AvaliacaoId(this, estudante), 0.0);
                addAvaliacao(avaliacao);
            });
        }
    }

    public Avaliacao getAvaliacaoDo(Estudante estudante) {
        if (hasAvaliacoes()) {
            Iterator<Avaliacao> it = avaliacoes.iterator();
            while (it.hasNext()) {
                Avaliacao o = it.next();
                if (estudante.equals(o.getEstudante())) {
                    return o;
                }
            }
        }
        /**
         * Não existe avaliação para o estudante, portanto, deve ser criada
         */
        Avaliacao o = AvaliacaoFactory.getInstance()
                .createObject(new AvaliacaoId(this, estudante), 0.0);
        addAvaliacao(o);
        return o;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
        hash = 37 * hash + Objects.hashCode(this.nome);
        hash = 37 * hash + Objects.hashCode(this.etapaEnsino);
        hash = 37 * hash + Objects.hashCode(this.peso);
        hash = 37 * hash + Objects.hashCode(this.valor);
        hash = 37 * hash + Objects.hashCode(this.data);
        hash = 37 * hash + Objects.hashCode(this.instrumentoAvaliacao);
        hash = 37 * hash + Objects.hashCode(this.objetivo);
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
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.etapaEnsino, other.etapaEnsino)) {
            return false;
        }
        if (!Objects.equals(this.peso, other.peso)) {
            return false;
        }
        if (!Objects.equals(this.valor, other.valor)) {
            return false;
        }
        if (!Objects.equals(this.data, other.data)) {
            return false;
        }
        if (!Objects.equals(this.instrumentoAvaliacao, other.instrumentoAvaliacao)) {
            return false;
        }
        if (!Objects.equals(this.objetivo, other.objetivo)) {
            return false;
        }
        return true;
    }

}
