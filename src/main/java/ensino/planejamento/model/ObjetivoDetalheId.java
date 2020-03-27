/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

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
public class ObjetivoDetalheId implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = {
        @JoinColumn(name = "objetivo_planoDeEnsino_id"),
        @JoinColumn(name = "objetivo_id")
    })
    private Objetivo objetivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(value = {
        @JoinColumn(name = "detalhamehto_planoDeEnsino_id"),
        @JoinColumn(name = "detalhamento_id")
    })
    private Detalhamento detalhamento;

    public ObjetivoDetalheId() {
    }

    public ObjetivoDetalheId(Objetivo objetivo, Detalhamento detalhamento) {
        this.objetivo = objetivo;
        this.detalhamento = detalhamento;
    }

    public Objetivo getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(Objetivo objetivo) {
        this.objetivo = objetivo;
    }

    public Detalhamento getDetalhamento() {
        return detalhamento;
    }

    public void setDetalhamento(Detalhamento detalhamento) {
        this.detalhamento = detalhamento;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.objetivo);
        hash = 79 * hash + Objects.hashCode(this.detalhamento);
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
        final ObjetivoDetalheId other = (ObjetivoDetalheId) obj;
        if (!Objects.equals(this.objetivo, other.objetivo)) {
            return false;
        }
        if (!Objects.equals(this.detalhamento, other.detalhamento)) {
            return false;
        }
        return true;
    }

}
