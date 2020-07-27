package ensino.configuracoes.model;

import ensino.planejamento.model.PlanoDeEnsino;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.OrderBy;

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
    
    @OneToMany(mappedBy = "id.unidadeCurricular", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ReferenciaBibliografica> referencias;
    
    @OneToMany(mappedBy = "id.unidadeCurricular", fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy(clause = "nivel ASC, sequencia ASC")
    private List<Conteudo> conteudos;
    
    @OneToMany(mappedBy = "id.unidadeCurricular", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ObjetivoUC> objetivos;
    
    @OneToMany(mappedBy = "unidadeCurricular", fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy(clause = "id ASC")
    private List<PlanoDeEnsino> planosDeEnsino;
    
    public UnidadeCurricular() {
        id = new UnidadeCurricularId();
        this.referencias = new ArrayList();
        this.conteudos = new ArrayList();
        this.objetivos = new ArrayList();
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

    public List<Conteudo> getConteudos() {
        return conteudos;
    }

    public void setConteudos(List<Conteudo> conteudos) {
        this.conteudos = conteudos;
    }
    
    public void addConteudo(Conteudo o) {
        o.getId().setUnidadeCurricular(this);
        conteudos.add(o);
    }

    public void removeConteudo(Conteudo o) {
        conteudos.remove(o);
    }
    
    public void updateConteudo(Conteudo o) {
        for(Conteudo co : conteudos) {
            if (o.equals(co)) {
                co = o;
                break;
            }
        }
    }

    public List<ObjetivoUC> getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(List<ObjetivoUC> objetivos) {
        this.objetivos = objetivos;
    }
    
    public void addObjetivo(ObjetivoUC o) {
        o.getId().setUnidadeCurricular(this);
        objetivos.add(o);
    }
    
    public void removeObjetivo(ObjetivoUC o) {
        objetivos.remove(o);
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
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
        hash = 59 * hash + Objects.hashCode(this.nome);
        hash = 59 * hash + Objects.hashCode(this.nAulasTeoricas);
        hash = 59 * hash + Objects.hashCode(this.nAulasPraticas);
        hash = 59 * hash + Objects.hashCode(this.cargaHoraria);
        hash = 59 * hash + Objects.hashCode(this.ementa);
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
        final UnidadeCurricular other = (UnidadeCurricular) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.ementa, other.ementa)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.nAulasTeoricas, other.nAulasTeoricas)) {
            return false;
        }
        if (!Objects.equals(this.nAulasPraticas, other.nAulasPraticas)) {
            return false;
        }
        if (!Objects.equals(this.cargaHoraria, other.cargaHoraria)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return this.nome;
    }

}
