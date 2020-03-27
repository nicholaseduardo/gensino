/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author nicho
 */
@Entity
@Table(name = "objetivoDetalhe")
public class ObjetivoDetalhe implements Serializable {
    
    @EmbeddedId
    private ObjetivoDetalheId id;
    
    @Transient
    private Boolean deleted;
    
    public ObjetivoDetalhe() {
        id = new ObjetivoDetalheId();
        deleted = false;
    }

    public ObjetivoDetalheId getId() {
        return id;
    }

    public void setId(ObjetivoDetalheId id) {
        this.id = id;
    }
    
    public Detalhamento getDetalhamento() {
        return id.getDetalhamento();
    }
    
    public Objetivo getObjetivo() {
        return id.getObjetivo();
    }
    
    public Boolean isDeleted() {
        return deleted;
    }
    
    public void delete() {
        deleted = true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.id);
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
        final ObjetivoDetalhe other = (ObjetivoDetalhe) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
}
