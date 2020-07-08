/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

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
public class ObjetivoUCConteudoId implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = {
        @JoinColumn(name = "objetivouc_unidadeCurricular_id"),
        @JoinColumn(name = "objetivouc_curso_id"),
        @JoinColumn(name = "objetivouc_campus_id"),
        @JoinColumn(name = "objetivouc_id")
    })
    private ObjetivoUC objetivo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = {
        @JoinColumn(name = "conteudo_unidadeCurricular_id"),
        @JoinColumn(name = "conteudo_curso_id"),
        @JoinColumn(name = "conteudo_campus_id"),
        @JoinColumn(name = "conteudo_id")
    })
    private Conteudo conteudo;

    public ObjetivoUCConteudoId() {
    }

    public ObjetivoUCConteudoId(ObjetivoUC objetivo, Conteudo conteudo) {
        this.objetivo = objetivo;
        this.conteudo = conteudo;
    }

    public ObjetivoUC getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(ObjetivoUC objetivo) {
        this.objetivo = objetivo;
    }

    public Conteudo getConteudo() {
        return conteudo;
    }

    public void setConteudo(Conteudo conteudo) {
        this.conteudo = conteudo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.objetivo);
        hash = 17 * hash + Objects.hashCode(this.conteudo);
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
        final ObjetivoUCConteudoId other = (ObjetivoUCConteudoId) obj;
        if (!Objects.equals(this.objetivo, other.objetivo)) {
            return false;
        }
        if (!Objects.equals(this.conteudo, other.conteudo)) {
            return false;
        }
        return true;
    }
    
}
