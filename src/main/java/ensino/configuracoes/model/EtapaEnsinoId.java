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
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nivelEnsino_id")
    private NivelEnsino nivelEnsino;

    public EtapaEnsinoId() {
    }

    public EtapaEnsinoId(Long id, NivelEnsino nivelEnsino) {
        this.id = id;
        this.nivelEnsino = nivelEnsino;
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
        final EtapaEnsinoId other = (EtapaEnsinoId) obj;
        if (!Objects.equals(this.id, other.getId())) {
            return false;
        }
        if (!Objects.equals(this.nivelEnsino, other.getNivelEnsino())) {
            return false;
        }
        return true;
    }
    
}
