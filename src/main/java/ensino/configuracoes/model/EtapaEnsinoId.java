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
import javax.persistence.ManyToOne;

/**
 *
 * @author santos
 */
@Embeddable 
public class EtapaEnsinoId implements Serializable {
    @Column(name = "id", nullable = false)
    private Integer id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nivelEnsino_id")
    private NivelEnsino nivelEnsino;

    public EtapaEnsinoId() {
    }

    public EtapaEnsinoId(Integer id, NivelEnsino nivelEnsino) {
        this.id = id;
        this.nivelEnsino = nivelEnsino;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public NivelEnsino getNivelEnsino() {
        return nivelEnsino;
    }

    public void setNivelEnsino(NivelEnsino nivelEnsino) {
        this.nivelEnsino = nivelEnsino;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.id);
        hash = 37 * hash + Objects.hashCode(this.nivelEnsino);
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
        final EtapaEnsinoId other = (EtapaEnsinoId) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.nivelEnsino, other.nivelEnsino)) {
            return false;
        }
        return true;
    }
    
}
