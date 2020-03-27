/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import ensino.util.types.Periodo;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author nicho
 */
@Entity(name = "SemanaLetiva")
@Table(name = "semanaLetiva")
public class SemanaLetiva implements Serializable {

    @EmbeddedId
    private SemanaLetivaId id;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Embedded
    private Periodo periodo;

    /**
     * Atributo utilizado para marcar o objeto para remoção futura. Valor
     * padrão: false
     */
    @Transient
    private Boolean deleted;

    public SemanaLetiva() {
        id = new SemanaLetivaId();
        deleted = false;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void delete() {
        deleted = true;
    }

    public SemanaLetivaId getId() {
        return id;
    }

    public void setId(SemanaLetivaId id) {
        this.id = id;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.id);
        hash = 89 * hash + Objects.hashCode(this.descricao);
        hash = 89 * hash + Objects.hashCode(this.periodo);
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
        final SemanaLetiva other = (SemanaLetiva) obj;
        if (!Objects.equals(this.descricao, other.descricao)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.periodo, other.periodo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s", descricao);
    }

}
