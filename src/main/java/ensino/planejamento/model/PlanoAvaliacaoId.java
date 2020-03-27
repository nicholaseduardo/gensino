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
public class PlanoAvaliacaoId implements Serializable {
    
    /**
     * Atributo utilizado para identificar a sequência de inclusão
     * dos instrumentos avaliativos.
     */
    @Column(name = "sequencia")
    private Integer sequencia;
    /**
     * Atributo utilizado para identifica a qual plano de ensino
     * pertence esse plano de avaliações
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "planoAvaliacao_planoDeEnsino_id")
    private PlanoDeEnsino planoDeEnsino;

    public PlanoAvaliacaoId() {
    }

    public PlanoAvaliacaoId(Integer sequencia, PlanoDeEnsino planoDeEnsino) {
        this.sequencia = sequencia;
        this.planoDeEnsino = planoDeEnsino;
    }

    public Integer getSequencia() {
        return sequencia;
    }

    public void setSequencia(Integer sequencia) {
        this.sequencia = sequencia;
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
        hash = 41 * hash + Objects.hashCode(this.sequencia);
        hash = 41 * hash + Objects.hashCode(this.planoDeEnsino);
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
        final PlanoAvaliacaoId other = (PlanoAvaliacaoId) obj;
        if (!Objects.equals(this.sequencia, other.sequencia)) {
            return false;
        }
        if (!Objects.equals(this.planoDeEnsino, other.planoDeEnsino)) {
            return false;
        }
        return true;
    }
    
}
