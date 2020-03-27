package ensino.configuracoes.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "referenciaBibliografica")
public class ReferenciaBibliografica implements Serializable {

    public static Integer TIPO_BASICA = 0;
    public static Integer TIPO_COMPLEMENTAR = 1;
    
    @EmbeddedId
    private ReferenciaBibliograficaId id;
    
    @Column(name = "tipo")
    private Integer tipo;
    
    @Transient
    private Boolean deleted;

    public ReferenciaBibliografica() {
        id = new ReferenciaBibliograficaId();
        deleted = false;
    }

    public ReferenciaBibliograficaId getId() {
        return id;
    }

    public void setId(ReferenciaBibliograficaId id) {
        this.id = id;
    }
    
    public Bibliografia getBibliografia() {
        return id.getBibliografia();
    }
    
    public Boolean isDeleted() {
        return deleted;
    }
    
    public void delete() {
        deleted = true;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
        hash = 59 * hash + Objects.hashCode(this.tipo);
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
        final ReferenciaBibliografica other = (ReferenciaBibliografica) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.tipo, other.tipo)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s", tipo == TIPO_BASICA ? "Básica" : "Complementar", id.getBibliografia().toString());
    }
}
