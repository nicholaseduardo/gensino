package ensino.configuracoes.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "turma")
public class Turma implements Serializable {

    @EmbeddedId
    private TurmaId id;
    
    @Column(name = "nome")
    private String nome;
    
    @Column(name = "ano")
    private Integer ano;
    
    @OneToMany(mappedBy = "id.turma", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Estudante> estudantes;

    public Turma() {
        id = new TurmaId();
        this.estudantes = new ArrayList<>();
    }

    public TurmaId getId() {
        return id;
    }

    public void setId(TurmaId id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }
    
    public boolean hasEstudantes() {
        return !estudantes.isEmpty();
    }

    public void addEstudante(Estudante estudante) {
        estudante.getId().setTurma(this);
        estudantes.add(estudante);
    }

    public void removeEstudante(Estudante estudante) {
        estudantes.remove(estudante);
    }

    public void updateEstudante(Estudante estudante) {
        for(Estudante student : estudantes) {
            if (student.getId().equals(estudante.getId())) {
                student = estudante;
                break;
            }
        }
    }

    public List<Estudante> getEstudantes() {
        return estudantes;
    }

    public void setEstudantes(List<Estudante> estudantes) {
        this.estudantes = estudantes;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
        hash = 89 * hash + Objects.hashCode(this.nome);
        hash = 89 * hash + Objects.hashCode(this.ano);
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
        final Turma other = (Turma) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.ano, other.ano)) {
            return false;
        }
        return true;
    }

    

}
