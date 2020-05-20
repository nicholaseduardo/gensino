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
public class AtendimentoEstudanteId implements Serializable {
    /**
     * Atributo utilizado para identificar a sequência de inclusão
     * de atendimento aos estudantes
     */
    @Column(name = "sequencia")
    private Integer sequencia;
    /**
     * Atributo utilizado para identificar de qual permanência
     * é o atendimento
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = {
        @JoinColumn(name = "planoDeEnsino_id"),
        @JoinColumn(name = "permanenciaEstudantil_id")
    })
    private PermanenciaEstudantil permanenciaEstudantil;
    /**
     * Atributo utilizado para identificar o estudante cujo atendimento está
     * sendo registrado
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = {
        @JoinColumn(name = "campus_id"),
        @JoinColumn(name = "curso_id"),
        @JoinColumn(name = "turma_id"),
        @JoinColumn(name = "estudante_id")
    })
    private Estudante estudante;

    public AtendimentoEstudanteId() {
    }

    public AtendimentoEstudanteId(Integer id, PermanenciaEstudantil permanenciaEstudantil, Estudante estudante) {
        this.sequencia = id;
        this.permanenciaEstudantil = permanenciaEstudantil;
        this.estudante = estudante;
    }

    public Integer getSequencia() {
        return sequencia;
    }

    public void setSequencia(Integer sequencia) {
        this.sequencia = sequencia;
    }

    public PermanenciaEstudantil getPermanenciaEstudantil() {
        return permanenciaEstudantil;
    }

    public void setPermanenciaEstudantil(PermanenciaEstudantil permanenciaEstudantil) {
        this.permanenciaEstudantil = permanenciaEstudantil;
    }

    public Estudante getEstudante() {
        return estudante;
    }

    public void setEstudante(Estudante estudante) {
        this.estudante = estudante;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.sequencia);
        hash = 23 * hash + Objects.hashCode(this.permanenciaEstudantil);
        hash = 23 * hash + Objects.hashCode(this.estudante);
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
        final AtendimentoEstudanteId other = (AtendimentoEstudanteId) obj;
        if (!Objects.equals(this.sequencia, other.sequencia)) {
            return false;
        }
        if (!Objects.equals(this.permanenciaEstudantil, other.permanenciaEstudantil)) {
            return false;
        }
        if (!Objects.equals(this.estudante, other.estudante)) {
            return false;
        }
        return true;
    }
    
}
