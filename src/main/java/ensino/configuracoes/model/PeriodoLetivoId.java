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
public class PeriodoLetivoId implements Serializable {
    
    @Column(name = "numero")
    private Integer numero;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = {
        @JoinColumn(name = "calendario_ano"),
        @JoinColumn(name = "campus_id")
    })
    private Calendario calendario;

    public PeriodoLetivoId() {
    }

    public PeriodoLetivoId(Integer numero, Calendario calendario) {
        this.numero = numero;
        this.calendario = calendario;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Calendario getCalendario() {
        return calendario;
    }

    public void setCalendario(Calendario calendario) {
        this.calendario = calendario;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.numero);
        hash = 29 * hash + Objects.hashCode(this.calendario);
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
        final PeriodoLetivoId other = (PeriodoLetivoId) obj;
        if (!Objects.equals(this.numero, other.numero)) {
            return false;
        }
        if (!Objects.equals(this.calendario, other.calendario)) {
            return false;
        }
        return true;
    }
    
}
