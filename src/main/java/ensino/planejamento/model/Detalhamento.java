package ensino.planejamento.model;

import ensino.configuracoes.model.SemanaLetiva;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "detalhamento")
public class Detalhamento implements Serializable {

    @EmbeddedId
    private DetalhamentoId id;
    
    @Column(name = "nAulasPraticas")
    private Integer NAulasPraticas;
    
    @Column(name = "nAulasTeoricas")
    private Integer NAulasTeoricas;
    
    @Column(name = "conteudo", length = 500)
    private String conteudo;
    
    @Column(name = "observacao", length = 500)
    private String observacao;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = {
        @JoinColumn(name = "campus_id"),
        @JoinColumn(name = "calendario_ano"),
        @JoinColumn(name = "periodoLetivo_numero"),
        @JoinColumn(name = "semanaLetiva_id")
    })
    private SemanaLetiva semanaLetiva;
    
    @OneToMany(mappedBy = "id.detalhamento", 
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, 
            fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Metodologia> metodologias;
    
    @OneToMany(mappedBy = "id.detalhamento", 
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, 
            fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ObjetivoDetalhe> objetivoDetalhes;

    public Detalhamento() {
        id = new DetalhamentoId();
        this.metodologias = new ArrayList();
        this.objetivoDetalhes = new ArrayList();
    }

    public DetalhamentoId getId() {
        return id;
    }

    public void setId(DetalhamentoId id) {
        this.id = id;
    }
    
    public PlanoDeEnsino getPlanoDeEnsino() {
        return id.getPlanoDeEnsino();
    }

    public Integer getNAulasPraticas() {
        return NAulasPraticas;
    }

    public void setNAulasPraticas(Integer nAulasPraticas) {
        this.NAulasPraticas = nAulasPraticas;
    }

    public Integer getNAulasTeoricas() {
        return NAulasTeoricas;
    }

    public void setNAulasTeoricas(Integer nAulasTeoricas) {
        this.NAulasTeoricas = nAulasTeoricas;
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

    public SemanaLetiva getSemanaLetiva() {
        return semanaLetiva;
    }

    public void setSemanaLetiva(SemanaLetiva semanaLetiva) {
        this.semanaLetiva = semanaLetiva;
    }
    
    public void addMetodologia(Metodologia metodologia) {
        metodologia.getId().setDetalhamento(this);
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
    
    public void addObjetivoDetalhe(ObjetivoDetalhe objetivoDetalhe) {
        objetivoDetalhe.getId().setDetalhamento(this);
        this.objetivoDetalhes.add(objetivoDetalhe);
    }
    
    public void removeObjetivoDetalhe(ObjetivoDetalhe objetivoDetalhe) {
        this.objetivoDetalhes.remove(objetivoDetalhe);
    }

    public List<ObjetivoDetalhe> getObjetivoDetalhes() {
        return objetivoDetalhes;
    }

    public void setObjetivoDetalhes(List<ObjetivoDetalhe> objetivoDetalhes) {
        this.objetivoDetalhes = objetivoDetalhes;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
        hash = 79 * hash + Objects.hashCode(this.NAulasPraticas);
        hash = 79 * hash + Objects.hashCode(this.NAulasTeoricas);
        hash = 79 * hash + Objects.hashCode(this.conteudo);
        hash = 79 * hash + Objects.hashCode(this.observacao);
        hash = 79 * hash + Objects.hashCode(this.semanaLetiva);
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
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.NAulasPraticas, other.NAulasPraticas)) {
            return false;
        }
        if (!Objects.equals(this.NAulasTeoricas, other.NAulasTeoricas)) {
            return false;
        }
        if (!Objects.equals(this.semanaLetiva, other.semanaLetiva)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.semanaLetiva.toString();
    }
    
}
