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
public class DiarioFrequenciaId implements Serializable {
    
    /**
     * Atributo utilizado para identificar uma frequência do diário
     */
    @Column
    private Long id;
    
    /**
     * Atributo utilizado para identificar de qual diário é o registro
     * desta frequência.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns(value = {
        @JoinColumn(name = "planoDeEnsino_id"),
        @JoinColumn(name = "diario_id")
    })
    private Diario diario;
    /**
     * Atributo utilizado para identificar o estudante cuja frequência está
     * sendo registrada.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns(value = {
        @JoinColumn(name = "campus_id"),
        @JoinColumn(name = "curso_id"),
        @JoinColumn(name = "turma_id"),
        @JoinColumn(name = "estudante_id")
    })
    private Estudante estudante;

    public DiarioFrequenciaId() {
    }

    public DiarioFrequenciaId(Long id, Diario diario, Estudante estudante) {
        this.id = id;
        this.diario = diario;
        this.estudante = estudante;
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

    public Diario getDiario() {
        return diario;
    }

    public void setDiario(Diario diario) {
        this.diario = diario;
    }

    public Estudante getEstudante() {
        return estudante;
    }

    public void setEstudante(Estudante estudante) {
        this.estudante = estudante;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + Objects.hashCode(this.diario);
        hash = 53 * hash + Objects.hashCode(this.estudante);
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
        final DiarioFrequenciaId other = (DiarioFrequenciaId) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.diario, other.diario)) {
            return false;
        }
        if (!Objects.equals(this.estudante, other.estudante)) {
            return false;
        }
        return true;
    }
    
}
