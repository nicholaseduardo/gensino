package ensino.planejamento.model;

import ensino.patterns.BaseObject;
import ensino.util.TipoMetodoConverter;
import ensino.util.types.TipoMetodo;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "metodologia")
public class Metodologia implements Serializable {

    @EmbeddedId
    private MetodologiaId id;
    
    @Column(name = "tipoMetodo", nullable = false, columnDefinition = "INTEGER")
    @Convert(converter = TipoMetodoConverter.class)
    private TipoMetodo tipoMetodo;
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metodo_id")
    private BaseObject metodo;
    /**
     * Atributo utilizado para marcar a instância do objeto
     * para exclusão;
     */
    @Transient
    private Boolean deleted;

    public Metodologia() {
        id = new MetodologiaId();
        deleted = false;
    }

    public MetodologiaId getId() {
        return id;
    }

    public void setId(MetodologiaId id) {
        this.id = id;
    }
    
    public Detalhamento getDetalhamento() {
        return id.getDetalhamento();
    }
    
    public void delete() {
        deleted = true;
    }
    
    public Boolean isDeleted() {
        return deleted;
    }

    public TipoMetodo getTipo() {
        return tipoMetodo;
    }

    public void setTipo(TipoMetodo tipo) {
        this.tipoMetodo = tipo;
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
    public String toString() {
        return String.format("[%s] %s", tipoMetodo.toString(), metodo.getNome());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.id);
        hash = 23 * hash + Objects.hashCode(this.tipoMetodo);
        hash = 23 * hash + Objects.hashCode(this.metodo);
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
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (this.tipoMetodo != other.tipoMetodo) {
            return false;
        }
        if (!Objects.equals(this.metodo, other.metodo)) {
            return false;
        }
        return true;
    }

}
