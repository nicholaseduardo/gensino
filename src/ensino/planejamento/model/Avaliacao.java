/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.configuracoes.model.Estudante;
import java.util.Objects;
import org.w3c.dom.Element;

/**
 *
 * @author nicho
 */
public class Avaliacao {    
    /**
     * Atributo utilizado para identificar em qual plano de avaliacao será
     * utilizado para registro de nota do estudante
     */
    private PlanoAvaliacao planoAvaliacao;
    /**
     * Atributo utilizado para identificar o estudante que será avaliado de
     * acordo com o plano de avaliação
     */
    private Estudante estudante;
    /**
     * Atributo utilizado para registrar a nota obtida pelo estudante após
     * realizar a avaliação
     */
    private Double nota;
    
    public Avaliacao() {
        
    }
    
    public Avaliacao(Element e, PlanoAvaliacao planoAvaliacao) {
        
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.planoAvaliacao);
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
        if (!Objects.equals(this.planoAvaliacao, other.planoAvaliacao)) {
            return false;
        }
        if (!Objects.equals(this.estudante, other.estudante)) {
            return false;
        }
        return true;
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
    
}
