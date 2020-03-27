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
public class HorarioAulaId implements Serializable {
    
    /**
     * Atributo utilizado para identificar unicamente o
     * horário da aula.
     */
    @Column(name = "id")
    private Integer id;
    
    /**
     * Atributo utilizado para identificar em qual plano
     * de ensino serão registrados os dados do diário.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "planoDeEnsino_id")
    private PlanoDeEnsino planoDeEnsino;

    public HorarioAulaId() {
    }

    public HorarioAulaId(Integer id, PlanoDeEnsino planoDeEnsino) {
        this.id = id;
        this.planoDeEnsino = planoDeEnsino;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PlanoDeEnsino getPlanoDeEnsino() {
        return planoDeEnsino;
    }

    public void setPlanoDeEnsino(PlanoDeEnsino planoDeEnsino) {
        this.planoDeEnsino = planoDeEnsino;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.id);
        hash = 29 * hash + Objects.hashCode(this.planoDeEnsino);
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
        final HorarioAulaId other = (HorarioAulaId) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.planoDeEnsino, other.planoDeEnsino)) {
            return false;
        }
        return true;
    }
    
}
