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
import javax.persistence.ManyToOne;

/**
 *
 * @author santos
 */
@Embeddable
public class DetalhamentoId implements Serializable {
    
    @Column(name = "sequencia")
    private Long sequencia;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "planoDeEnsino_id")
    private PlanoDeEnsino planoDeEnsino;

    public DetalhamentoId() {
    }

    public DetalhamentoId(Long sequencia, PlanoDeEnsino planoDeEnsino) {
        this.sequencia = sequencia;
        this.planoDeEnsino = planoDeEnsino;
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

    public PlanoDeEnsino getPlanoDeEnsino() {
        return planoDeEnsino;
    }

    public void setPlanoDeEnsino(PlanoDeEnsino planoDeEnsino) {
        this.planoDeEnsino = planoDeEnsino;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.sequencia);
        hash = 59 * hash + Objects.hashCode(this.planoDeEnsino);
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
        final DetalhamentoId other = (DetalhamentoId) obj;
        if (!Objects.equals(this.sequencia, other.sequencia)) {
            return false;
        }
        if (!Objects.equals(this.planoDeEnsino, other.planoDeEnsino)) {
            return false;
        }
        return true;
    }
    
}
