/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

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
public class MetodologiaId implements Serializable {
    
    @Column(name = "sequencia")
    private Long sequencia;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns(value = {
        @JoinColumn(name = "planoDeEnsino_id"),
        @JoinColumn(name = "detalhamento_id")
    })
    private Detalhamento detalhamento;

    public MetodologiaId() {
    }

    public MetodologiaId(Long sequencia, Detalhamento detalhamento) {
        this.sequencia = sequencia;
        this.detalhamento = detalhamento;
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

    public Detalhamento getDetalhamento() {
        return detalhamento;
    }

    public void setDetalhamento(Detalhamento detalhamento) {
        this.detalhamento = detalhamento;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + Objects.hashCode(this.sequencia);
        hash = 31 * hash + Objects.hashCode(this.detalhamento);
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
        final MetodologiaId other = (MetodologiaId) obj;
        if (!Objects.equals(this.sequencia, other.sequencia)) {
            return false;
        }
        if (!Objects.equals(this.detalhamento, other.detalhamento)) {
            return false;
        }
        return true;
    }
    
}
