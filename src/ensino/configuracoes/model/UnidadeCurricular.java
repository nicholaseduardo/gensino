package ensino.configuracoes.model;

import ensino.planejamento.model.PlanoDeEnsino;
import java.util.ArrayList;
import java.util.List;

public class UnidadeCurricular {
    private Integer id;
    private String nome;
    private Integer nAulasTeoricas;
    private Integer nAulasPraticas;
    private Integer cargaHoraria;
    private String ementa;
    private Curso curso;
    
    private List<ReferenciaBibliografica> referencias;
    private List<PlanoDeEnsino> planosDeEnsino;
    
    public UnidadeCurricular() {
        this.referencias = new ArrayList();
        this.planosDeEnsino = new ArrayList();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getnAulasTeoricas() {
        return nAulasTeoricas;
    }

    public void setnAulasTeoricas(Integer nAulasTeoricas) {
        this.nAulasTeoricas = nAulasTeoricas;
    }

    public Integer getnAulasPraticas() {
        return nAulasPraticas;
    }

    public void setnAulasPraticas(Integer nAulasPraticas) {
        this.nAulasPraticas = nAulasPraticas;
    }

    public Integer getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(Integer cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public String getEmenta() {
        return ementa;
    }

    public void setEmenta(String ementa) {
        this.ementa = ementa;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
    
    public void addReferenciaBibliografica(ReferenciaBibliografica referencia) {
        referencia.setUnidadeCurricular(this);
        this.referencias.add(referencia);
    }
    
    public void removeReferenciaBibliografica(ReferenciaBibliografica referencia) {
        this.referencias.remove(referencia);
    }

    public List<ReferenciaBibliografica> getReferenciasBibliograficas() {
        return referencias;
    }

    public void setReferenciasBibliograficas(List<ReferenciaBibliografica> referencias) {
        this.referencias = referencias;
    }
    
    public void addPlanoDeEnsino(PlanoDeEnsino planoDeEnsino) {
        planoDeEnsino.setUnidadeCurricular(this);
        this.planosDeEnsino.add(planoDeEnsino);
    }
    
    public void removePlanoDeEnsino(PlanoDeEnsino planoDeEnsino) {
        this.planosDeEnsino.remove(planoDeEnsino);
    }

    public List<PlanoDeEnsino> getPlanosDeEnsino() {
        return planosDeEnsino;
    }

    public void setPlanosDeEnsino(List<PlanoDeEnsino> planosDeEnsino) {
        this.planosDeEnsino = planosDeEnsino;
    }
    
    /**
     * Converte a lista de referências bibliográficas no formato TEXTO
     * 
     * @return 
     */
    public String referenciaBibliograficaToString() {
        StringBuilder sbBasica = new StringBuilder();
            sbBasica.append("--- Bibliografia básica --- \n");
            StringBuilder sbComplementar = new StringBuilder();
            sbComplementar.append("--- Bibliografia complementar --- \n");
            
            for (int i = 0; i < referencias.size(); i++) {
                ReferenciaBibliografica ref = referencias.get(i);
                if (ref.isBasica()) {
                    sbBasica.append(ref.getBibliografia().getReferencia());
                } else {
                    sbComplementar.append(ref.getBibliografia().getReferencia());
                }
            }
            return (sbBasica.toString() + "\n\n" + sbComplementar.toString());
    }

}
