package ensino.configuracoes.model;

import ensino.planejamento.model.PlanoDeEnsino;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "unidadeCurricular")
public class UnidadeCurricular implements Serializable {
    
    @EmbeddedId
    private UnidadeCurricularId id;
    
    @Column(name = "nome")
    private String nome;
    
    @Column(name = "nAulasTeoricas")
    private Integer nAulasTeoricas;
    
    @Column(name = "nAulasPraticas")
    private Integer nAulasPraticas;
    
    @Column(name = "cargaHoraria")
    private Integer cargaHoraria;
    
    @Lob
    @Column(name = "ementa", columnDefinition = "CLOB")
    private String ementa;
    
    @OneToMany(mappedBy = "id.unidadeCurricular", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ReferenciaBibliografica> referencias;
    
    @OneToMany(mappedBy = "unidadeCurricular", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PlanoDeEnsino> planosDeEnsino;
    
    public UnidadeCurricular() {
        id = new UnidadeCurricularId();
        this.referencias = new ArrayList();
        this.planosDeEnsino = new ArrayList();
    }

    public UnidadeCurricularId getId() {
        return id;
    }

    public void setId(UnidadeCurricularId id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    
    public Curso getCurso() {
        return id.getCurso();
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
    
    public void addReferenciaBibliografica(ReferenciaBibliografica referencia) {
        referencia.getId().setUnidadeCurricular(this);
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
                    sbBasica.append(ref.getId().getBibliografia().getReferencia());
                } else {
                    sbComplementar.append(ref.getId().getBibliografia().getReferencia());
                }
            }
            return (sbBasica.toString() + "\n\n" + sbComplementar.toString());
    }
    
    @Override
    public String toString() {
        return this.nome;
    }

}
