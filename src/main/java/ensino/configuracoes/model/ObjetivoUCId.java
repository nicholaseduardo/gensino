/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

/**
 *
 * @author santos
 */
@Embeddable
public class ObjetivoUCId implements Serializable {
    
    @Column(name = "sequencia")
    private Long sequencia;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = {
        @JoinColumn(name = "unidadeCurricular_id"),
        @JoinColumn(name = "curso_id"),
        @JoinColumn(name = "campus_id")
    })
    private UnidadeCurricular unidadeCurricular;

    public ObjetivoUCId() {
    }

    public ObjetivoUCId(Long sequencia, UnidadeCurricular unidadeCurricular) {
        this.sequencia = sequencia;
        this.unidadeCurricular = unidadeCurricular;
    }

    public Long getSequencia() {
        return sequencia;
    }

    public void setSequencia(Long sequencia) {
        this.sequencia = sequencia;
    }
    
    public Boolean hasSequencia() {
        return sequencia != null && sequencia > 0;
    }

    public UnidadeCurricular getUnidadeCurricular() {
        return unidadeCurricular;
    }

    public void setUnidadeCurricular(UnidadeCurricular unidadeCurricular) {
        this.unidadeCurricular = unidadeCurricular;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + Objects.hashCode(this.sequencia);
        hash = 73 * hash + Objects.hashCode(this.unidadeCurricular);
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
        final ObjetivoUCId other = (ObjetivoUCId) obj;
        if (!Objects.equals(this.sequencia, other.sequencia)) {
            return false;
        }
        if (!Objects.equals(this.unidadeCurricular, other.unidadeCurricular)) {
            return false;
        }
        return true;
    }
    
}
