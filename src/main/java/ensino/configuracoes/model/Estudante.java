package ensino.configuracoes.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "estudante")
public class Estudante implements Serializable {
    
    @EmbeddedId
    private EstudanteId id;
    
    @Column(name = "nome")
    private String nome;
    
    @Column(name = "registro")
    private String registro;
    
    @Transient
    private Boolean deleted;
    
    public Estudante() {
        id = new EstudanteId();
        deleted = false;
    }
    
    public void delete() {
        deleted = true;
    }
    
    public Boolean isDeleted() {
        return this.deleted;
    }
    
    public boolean issetId() {
        return id.getId() != null;
    }

    public EstudanteId getId() {
        return id;
    }

    public void setId(EstudanteId id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + Objects.hashCode(this.id);
        hash = 31 * hash + Objects.hashCode(this.nome);
        hash = 31 * hash + Objects.hashCode(this.registro);
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
        final Estudante other = (Estudante) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.registro, other.registro)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return nome;
    }

}
