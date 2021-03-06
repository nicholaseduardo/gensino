/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.util.PresencaConverter;
import ensino.util.types.Presenca;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author santos
 */
@Entity
@Table(name = "atendimentoEstudante")
public class AtendimentoEstudante implements Serializable {
    
    @EmbeddedId
    private AtendimentoEstudanteId id;
    
    /**
     * Atributo utilizado para registrar a presença ou a 
     * falta do estudante no atendimento.
     *
     * A presença pode ser:<br/>
     * <ul>
     * <li>PRESENTE</li>
     * <li>FALTA</li>
     * </ul>
     */
    @Column(name = "presenca", nullable = false)
    @Convert(converter = PresencaConverter.class)
    private Presenca presenca;
    

    public AtendimentoEstudante() {
        id = new AtendimentoEstudanteId();
        presenca = Presenca.FALTA;
    }

    public AtendimentoEstudanteId getId() {
        return id;
    }

    public void setId(AtendimentoEstudanteId id) {
        this.id = id;
    }
    
    public Boolean hasId() {
        return id.hasSequencia();
    }

    public Presenca getPresenca() {
        return presenca;
    }

    public void setPresenca(Presenca presenca) {
        this.presenca = presenca;
    }
    
    public Boolean isPresente() {
        return Presenca.PRESENTE.equals(presenca);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.id);
        hash = 67 * hash + Objects.hashCode(this.presenca);
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
        final AtendimentoEstudante other = (AtendimentoEstudante) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (this.presenca != other.presenca) {
            return false;
        }
        return true;
    }
    
}
