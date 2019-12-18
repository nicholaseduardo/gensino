package ensino.planejamento.model;

import java.util.Objects;

public class Objetivo {

    private Integer sequencia;
    private String descricao;
    // parent
    private PlanoDeEnsino planoDeEnsino;
    private Boolean deleted;
    
    public Objetivo() {
        deleted = false;
    }
    
    public Boolean isDeleted() {
        return deleted;
    }
    
    public void delete() {
        deleted = true;
    }

    public Integer getSequencia() {
        return sequencia;
    }

    public void setSequencia(Integer sequencia) {
        this.sequencia = sequencia;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public PlanoDeEnsino getPlanoDeEnsino() {
        return planoDeEnsino;
    }

    public void setPlanoDeEnsino(PlanoDeEnsino plano) {
        this.planoDeEnsino = plano;
    }
    
    @Override
    public String toString() {
        int length = this.descricao.length();
        if (length > 50)
            length = 50;
        return String.format("[%d] %s", this.sequencia, this.descricao.substring(0, length));
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final Objetivo other = (Objetivo) obj;
        if (!Objects.equals(this.descricao, other.descricao)) {
            return false;
        }
        if (!Objects.equals(this.sequencia, other.sequencia)) {
            return false;
        }
        return true;
    }

}
