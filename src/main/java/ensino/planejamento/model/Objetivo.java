package ensino.planejamento.model;

import ensino.configuracoes.model.ObjetivoUC;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "objetivo")
public class Objetivo implements Serializable {

    @EmbeddedId
    private ObjetivoId id;

    @Lob
    @Column(name = "descricao", columnDefinition = "CLOB")
    private String descricao;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = {
        @JoinColumn(name = "objetivouc_unidadeCurricular_id"),
        @JoinColumn(name = "objetivouc_curso_id"),
        @JoinColumn(name = "objetivouc_campus_id"),
        @JoinColumn(name = "objetivouc_id")
    })
    private ObjetivoUC objetivoUC;

    /**
     * Atributo utilizado para armazenas as notas das avaliações por estudante
     */
    @OneToMany(mappedBy = "objetivo", fetch = FetchType.LAZY)
    private List<PlanoAvaliacao> planosAvaliacao;

    @Transient
    private Boolean deleted;

    public Objetivo() {
        id = new ObjetivoId();
        deleted = false;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void delete() {
        deleted = true;
    }

    public ObjetivoId getId() {
        return id;
    }

    public void setId(ObjetivoId id) {
        this.id = id;
    }
    
    public Boolean hasId() {
        return id.hasSequencia();
    }

    public String getShortName() {
        return String.format("Obj. [id: %d]", id.getSequencia());
    }

    public PlanoDeEnsino getPlanoDeEnsino() {
        return id.getPlanoDeEnsino();
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public ObjetivoUC getObjetivoUC() {
        return objetivoUC;
    }

    public void setObjetivoUC(ObjetivoUC objetivo) {
        this.objetivoUC = objetivo;
    }

    public List<PlanoAvaliacao> getPlanosAvaliacao() {
        return planosAvaliacao;
    }

    public void setPlanosAvaliacao(List<PlanoAvaliacao> planosAvaliacao) {
        this.planosAvaliacao = planosAvaliacao;
    }

    public void addPlanoAvaliacao(PlanoAvaliacao o) {
        o.setObjetivo(this);
        planosAvaliacao.add(o);
    }

    public void removePlanoAvaliacao(PlanoAvaliacao o) {
        planosAvaliacao.remove(o);
    }

    @Override
    public String toString() {
        int length = this.descricao.length();
        if (length > 70) {
            length = 70;
        }
        return String.format("[%d] %s", this.id.getSequencia(), this.descricao.substring(0, length));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.id);
        hash = 37 * hash + Objects.hashCode(this.descricao);
        hash = 37 * hash + Objects.hashCode(this.objetivoUC);
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
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
