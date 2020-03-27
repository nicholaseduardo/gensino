/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.configuracoes.model.Estudante;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author nicho
 */
@Entity
@Table(name = "avaliacao")
public class Avaliacao implements Serializable {
    
    @EmbeddedId
    private AvaliacaoId id;
    
    /**
     * Atributo utilizado para registrar a nota obtida pelo estudante após
     * realizar a avaliação
     */
    @Column(name = "nota", columnDefinition = "REAL", precision = 3, scale = 2)
    private Double nota;
    
    public Avaliacao() {
        id = new AvaliacaoId();
        this.nota = 0.0;
    }

    public AvaliacaoId getId() {
        return id;
    }

    public void setId(AvaliacaoId id) {
        this.id = id;
    }
    
    public Estudante getEstudante() {
        return id.getEstudante();
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }
    
    @Override
    public String toString() {
        return nota.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id);
        hash = 29 * hash + Objects.hashCode(this.nota);
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
        final Avaliacao other = (Avaliacao) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.nota, other.nota)) {
            return false;
        }
        return true;
    }
    
}
