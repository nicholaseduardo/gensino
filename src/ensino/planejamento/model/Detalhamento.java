package ensino.planejamento.model;

import ensino.configuracoes.model.SemanaLetiva;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Detalhamento {

    private Integer sequencia;
    private Integer nAulasPraticas;
    private Integer nAulasTeoricas;
    private String conteudo;
    private String observacao;
    // parent
    private PlanoDeEnsino planoDeEnsino;
    // composition
    private SemanaLetiva semanaLetiva;
    private List<Metodologia> metodologias;
    private List<ObjetivoDetalhe> objetivoDetalhes;

    public Detalhamento() {
        this.metodologias = new ArrayList();
        this.objetivoDetalhes = new ArrayList();
    }

    public Integer getSequencia() {
        return sequencia;
    }

    public void setSequencia(Integer sequencia) {
        this.sequencia = sequencia;
    }

    public Integer getNAulasPraticas() {
        return nAulasPraticas;
    }

    public void setNAulasPraticas(Integer nAulasPraticas) {
        this.nAulasPraticas = nAulasPraticas;
    }

    public Integer getNAulasTeoricas() {
        return nAulasTeoricas;
    }

    public void setNAulasTeoricas(Integer nAulasTeoricas) {
        this.nAulasTeoricas = nAulasTeoricas;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public PlanoDeEnsino getPlanoDeEnsino() {
        return planoDeEnsino;
    }

    public void setPlanoDeEnsino(PlanoDeEnsino plano) {
        this.planoDeEnsino = plano;
    }

    public SemanaLetiva getSemanaLetiva() {
        return semanaLetiva;
    }

    public void setSemanaLetiva(SemanaLetiva semanaLetiva) {
        this.semanaLetiva = semanaLetiva;
    }
    
    public void addMetodologia(Metodologia metodologia) {
        metodologia.setDetalhamento(this);
        this.metodologias.add(metodologia);
    }
    
    public void removeMetodologia(Metodologia metodologia) {
        this.metodologias.remove(metodologia);
    }

    public List<Metodologia> getMetodologias() {
        return metodologias;
    }

    public void setMetodologias(List<Metodologia> metodologias) {
        this.metodologias = metodologias;
    }
    
    public void addObjetivoDetalhe(ObjetivoDetalhe objetivoDetalhe) {
        this.objetivoDetalhes.add(objetivoDetalhe);
    }
    
    public void removeObjetivoDetalhe(ObjetivoDetalhe objetivoDetalhe) {
        this.objetivoDetalhes.remove(objetivoDetalhe);
    }

    public List<ObjetivoDetalhe> getObjetivoDetalhes() {
        return objetivoDetalhes;
    }

    public void setObjetivoDetalhes(List<ObjetivoDetalhe> objetivoDetalhes) {
        this.objetivoDetalhes = objetivoDetalhes;
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final Detalhamento other = (Detalhamento) obj;
        if (!Objects.equals(this.conteudo, other.conteudo)) {
            return false;
        }
        if (!Objects.equals(this.observacao, other.observacao)) {
            return false;
        }
        if (!Objects.equals(this.sequencia, other.sequencia)) {
            return false;
        }
        if (!Objects.equals(this.nAulasPraticas, other.nAulasPraticas)) {
            return false;
        }
        if (!Objects.equals(this.nAulasTeoricas, other.nAulasTeoricas)) {
            return false;
        }
        if (!Objects.equals(this.planoDeEnsino, other.planoDeEnsino)) {
            return false;
        }
        if (!Objects.equals(this.semanaLetiva, other.semanaLetiva)) {
            return false;
        }
        return true;
    }

}
