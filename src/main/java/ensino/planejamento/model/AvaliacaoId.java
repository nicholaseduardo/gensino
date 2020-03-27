/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.configuracoes.model.Estudante;
import java.io.Serializable;
import java.util.Objects;
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
public class AvaliacaoId implements Serializable {
    
    /**
     * Atributo utilizado para identificar em qual plano de avaliacao será
     * utilizado para registro de nota do estudante
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = {
        @JoinColumn(name = "planoDeEnsino_id"),
        @JoinColumn(name = "planoAvaliacao_id")
    })
    private PlanoAvaliacao planoAvaliacao;
    
    /**
     * Atributo utilizado para identificar o estudante que será avaliado de
     * acordo com o plano de avaliação
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = {
        @JoinColumn(name = "campus_id"),
        @JoinColumn(name = "curso_id"),
        @JoinColumn(name = "turma_id"),
        @JoinColumn(name = "estudante_id")
    })
    private Estudante estudante;

    public AvaliacaoId() {
    }

    public AvaliacaoId(PlanoAvaliacao planoAvaliacao, Estudante estudante) {
        this.planoAvaliacao = planoAvaliacao;
        this.estudante = estudante;
    }

    public PlanoAvaliacao getPlanoAvaliacao() {
        return planoAvaliacao;
    }

    public void setPlanoAvaliacao(PlanoAvaliacao planoAvaliacao) {
        this.planoAvaliacao = planoAvaliacao;
    }

    public Estudante getEstudante() {
        return estudante;
    }

    public void setEstudante(Estudante estudante) {
        this.estudante = estudante;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.planoAvaliacao);
        hash = 97 * hash + Objects.hashCode(this.estudante);
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
        final AvaliacaoId other = (AvaliacaoId) obj;
        if (!Objects.equals(this.planoAvaliacao, other.planoAvaliacao)) {
            return false;
        }
        if (!Objects.equals(this.estudante, other.estudante)) {
            return false;
        }
        return true;
    }
    
}
