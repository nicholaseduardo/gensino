/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.configuracoes.model.Estudante;
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
 * @author nicho
 */
@Entity
@Table(name = "diarioFrequencia")
public class DiarioFrequencia implements Serializable {
    
    @EmbeddedId
    private DiarioFrequenciaId id;
    
    /**
     * Atributo utilizado para registrar a presença ou a falta do estudante.
     *
     * A presença pode ser:<br/>
     * <ul>
     * <li>PRESENTE</li>
     * <li>FALTA</li>
     * </ul>
     */
    @Column(name = "tipoAula", nullable = false)
    @Convert(converter = PresencaConverter.class)
    private Presenca presenca;
    
    public DiarioFrequencia() {
        id = new DiarioFrequenciaId();
        presenca = Presenca.PONTO;
    }

    public DiarioFrequenciaId getId() {
        return id;
    }

    public void setId(DiarioFrequenciaId id) {
        this.id = id;
    }
    
    public Boolean hasId() {
        return id.hasId();
    }
    
    public Diario getDiario() {
        return id.getDiario();
    }
    
    public Estudante getEstudante() {
        return id.getEstudante();
    }

    public Presenca getPresenca() {
        return presenca;
    }

    public void setPresenca(Presenca presenca) {
        this.presenca = presenca;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.id);
        hash = 47 * hash + Objects.hashCode(this.presenca);
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
        final DiarioFrequencia other = (DiarioFrequencia) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (this.presenca != other.presenca) {
            return false;
        }
        return true;
    }
    
    public String toString() {
        return presenca.getValue();
    }
    
}
