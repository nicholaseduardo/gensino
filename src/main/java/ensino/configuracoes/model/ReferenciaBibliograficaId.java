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
public class ReferenciaBibliograficaId implements Serializable {
    
    @Column(name = "sequencia")
    private Long sequencia;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = {
        @JoinColumn(name = "unidadeCurricular_id"),
        @JoinColumn(name = "curso_id"),
        @JoinColumn(name = "campus_id")
    })
    private UnidadeCurricular unidadeCurricular;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bibliografia_id")
    private Bibliografia bibliografia;

    public ReferenciaBibliograficaId() {
    }

    public ReferenciaBibliograficaId(Long sequencia, UnidadeCurricular unidadeCurricular, Bibliografia bibliografia) {
        this.sequencia = sequencia;
        this.unidadeCurricular = unidadeCurricular;
        this.bibliografia = bibliografia;
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

    public Bibliografia getBibliografia() {
        return bibliografia;
    }

    public void setBibliografia(Bibliografia bibliografia) {
        this.bibliografia = bibliografia;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.sequencia);
        hash = 53 * hash + Objects.hashCode(this.unidadeCurricular);
        hash = 53 * hash + Objects.hashCode(this.bibliografia);
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
        final ReferenciaBibliograficaId other = (ReferenciaBibliograficaId) obj;
        if (!Objects.equals(this.sequencia, other.sequencia)) {
            return false;
        }
        if (!Objects.equals(this.unidadeCurricular, other.unidadeCurricular)) {
            return false;
        }
        if (!Objects.equals(this.bibliografia, other.bibliografia)) {
            return false;
        }
        return true;
    }
    
}
