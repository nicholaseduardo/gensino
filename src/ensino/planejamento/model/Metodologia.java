package ensino.planejamento.model;

import ensino.patterns.BaseObject;
import ensino.util.types.TipoMetodo;
import java.util.Objects;

public class Metodologia {

    private Integer sequencia;
    private TipoMetodo tipoMetodo;
    // parent
    private Detalhamento detalhamento;
    // child
    private BaseObject metodo;
    /**
     * Atributo utilizado para marcar a instância do objeto
     * para exclusão;
     */
    private Boolean deleted;

    public Metodologia() {
        deleted = false;
    }
    
    public void delete() {
        deleted = true;
    }
    
    public Boolean isDeleted() {
        return deleted;
    }

    public Integer getSequencia() {
        return sequencia;
    }

    public void setSequencia(Integer sequencia) {
        this.sequencia = sequencia;
    }

    public TipoMetodo getTipo() {
        return tipoMetodo;
    }

    public void setTipo(TipoMetodo tipo) {
        this.tipoMetodo = tipo;
    }

    public Detalhamento getDetalhamento() {
        return detalhamento;
    }

    public void setDetalhamento(Detalhamento curso) {
        this.detalhamento = curso;
    }

    public BaseObject getMetodo() {
        return metodo;
    }

    public void setMetodo(BaseObject metodo) {
        this.metodo = metodo;
    }

    public boolean isTecnica() {
        return TipoMetodo.TECNICA.equals(tipoMetodo);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", tipoMetodo.toString(), metodo.getNome());
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final Metodologia other = (Metodologia) obj;
        if (this.tipoMetodo != other.tipoMetodo) {
            return false;
        }
        if (!Objects.equals(this.detalhamento, other.detalhamento)) {
            return false;
        }
        if (!Objects.equals(this.metodo, other.metodo)) {
            return false;
        }
        return true;
    }

}
