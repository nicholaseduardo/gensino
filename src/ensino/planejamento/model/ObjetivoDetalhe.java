/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import java.util.Objects;

/**
 *
 * @author nicho
 */
public class ObjetivoDetalhe {
    private Objetivo objetivo;
    private Detalhamento detalhamento;

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
        hash = 73 * hash + Objects.hashCode(this.objetivo);
        hash = 73 * hash + Objects.hashCode(this.detalhamento);
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
        if (!Objects.equals(this.objetivo, other.objetivo)) {
            return false;
        }
        if (!Objects.equals(this.detalhamento, other.detalhamento)) {
            return false;
        }
        return true;
    }
}
