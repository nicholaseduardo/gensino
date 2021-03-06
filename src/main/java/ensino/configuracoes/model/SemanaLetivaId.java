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
public class SemanaLetivaId implements Serializable {

    @Column(name = "id")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = {
        @JoinColumn(name = "periodoLetivo_numero"),
        @JoinColumn(name = "calendario_ano"),
        @JoinColumn(name = "campus_id")
    })
    private PeriodoLetivo periodoLetivo;

    public SemanaLetivaId() {
    }

    public SemanaLetivaId(Long id, PeriodoLetivo periodoLetivo) {
        this.id = id;
        this.periodoLetivo = periodoLetivo;
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Boolean hasId() {
        return id != null && id > 0;
    }

    public PeriodoLetivo getPeriodoLetivo() {
        return periodoLetivo;
    }

    public void setPeriodoLetivo(PeriodoLetivo periodoLetivo) {
        this.periodoLetivo = periodoLetivo;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.id);
        hash = 47 * hash + Objects.hashCode(this.periodoLetivo);
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
        final SemanaLetivaId other = (SemanaLetivaId) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.periodoLetivo, other.periodoLetivo)) {
            return false;
        }
        return true;
    }

}
