package ensino.configuracoes.model;

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
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "objetivoUC")
public class ObjetivoUC implements Serializable {

    @EmbeddedId
    private ObjetivoUCId id;

    @Lob
    @Column(name = "descricao", columnDefinition = "CLOB")
    private String descricao;

    @Column(name = "ordem")
    private Integer ordem;

    @OneToMany(mappedBy = "id.objetivo", fetch = FetchType.LAZY,
            orphanRemoval = true)
    @OrderBy(value = "ordem")
    private List<ObjetivoUCConteudo> conteudos;

    public ObjetivoUC() {
        id = new ObjetivoUCId();
        conteudos = new ArrayList();
    }

    public ObjetivoUCId getId() {
        return id;
    }

    public void setId(ObjetivoUCId id) {
        this.id = id;
    }

    public String getShortName() {
        return String.format("Obj. [id: %d]", id.getSequencia());
    }

    public UnidadeCurricular getUnidadeCurricular() {
        return id.getUnidadeCurricular();
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public void addConteudo(ObjetivoUCConteudo o) {
        o.getId().setObjetivo(this);
        if (!conteudos.contains(o)) {
            conteudos.add(o);
        } else {
            /**
             * Força a atualização do objeto considerando
             * que a ordem pode ter sido modificada.
             */
            int index = conteudos.indexOf(o);
            conteudos.set(index, o);
        }
    }

    public void removeConteudo(ObjetivoUCConteudo o) {
        conteudos.remove(o);
    }

    public List<ObjetivoUCConteudo> getConteudos() {
        return conteudos;
    }

    public void setConteudos(List<ObjetivoUCConteudo> conteudos) {
        this.conteudos = conteudos;
    }

    @Override
    public String toString() {
        int length = this.descricao.length();
        if (length > 70) {
            length = 70;
        }
        return String.format("[%d] %s", this.id.getSequencia(), this.descricao.substring(0, length));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.id);
        hash = 47 * hash + Objects.hashCode(this.descricao);
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
        final ObjetivoUC other = (ObjetivoUC) obj;
        if (!Objects.equals(this.descricao, other.descricao)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
