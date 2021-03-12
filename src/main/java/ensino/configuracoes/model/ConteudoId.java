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
public class ConteudoId implements Serializable {
    
    @Column(name = "id")
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns(value = {
        @JoinColumn(name = "unidadeCurricular_id"),
        @JoinColumn(name = "curso_id"),
        @JoinColumn(name = "campus_id")
    })
    private UnidadeCurricular unidadeCurricular;
    
    public ConteudoId(Long sequencia, UnidadeCurricular uc) {
        this.id = sequencia;
        this.unidadeCurricular = uc;
    }

    public ConteudoId() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long sequencia) {
        this.id = sequencia;
    }
    
    public Boolean hasId() {
        return id != null && id > 0;
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
        hash = 79 * hash + Objects.hashCode(this.id);
        hash = 79 * hash + Objects.hashCode(this.unidadeCurricular);
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
        final ConteudoId other = (ConteudoId) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.unidadeCurricular, other.unidadeCurricular)) {
            return false;
        }
        return true;
    }
    
}
