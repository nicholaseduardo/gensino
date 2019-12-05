package ensino.configuracoes.model;

import java.util.Objects;

public class ReferenciaBibliografica {

    public static Integer TIPO_BASICA = 0;
    public static Integer TIPO_COMPLEMENTAR = 1;
    
    private Integer sequencia;
    private Integer tipo;
    private UnidadeCurricular unidadeCurricular;
    private Bibliografia bibliografia;
    private Boolean deleted;

    public ReferenciaBibliografica() {
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

    /**
     * Recupera o tipo de referência da bibliografia.<br/> Pode ser:
     * <code>ReferenciaBibliografica.TIPO_BASICA</code> ou
     * <code>ReferenciaBibliografica.TIPO_COMPLEMENTAR</code>
     * @return 
     */
    public Integer getTipo() {
        return tipo;
    }
    
    public String getTipoDescricao() {
        switch (tipo) {
            case 0: return "Básica";
            case 1: return "Complementar";
        }
        return "";
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }
    
    public boolean isBasica() {
        return Objects.equals(tipo, TIPO_BASICA);
    }

    public UnidadeCurricular getUnidadeCurricular() {
        return unidadeCurricular;
    }

    public void setUnidadeCurricular(UnidadeCurricular unidadeCurricular) {
        this.unidadeCurricular = unidadeCurricular;
    }

    public Bibliografia getBibliografia() {
        return bibliografia;
    }

    public void setBibliografia(Bibliografia bibliografia) {
        this.bibliografia = bibliografia;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", tipo == TIPO_BASICA ? "Básica" : "Complementar", bibliografia.toString());
    }
}
