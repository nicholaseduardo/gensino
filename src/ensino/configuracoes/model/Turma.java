package ensino.configuracoes.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Turma {

    private Integer id;
    private String nome;
    private Integer ano;
    // parent
    private Curso curso;
    // aggregation
    private List<Estudante> estudantes;

    public Turma() {
        this.estudantes = new ArrayList<>();
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

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }
    
    public boolean hasEstudantes() {
        return !estudantes.isEmpty();
    }

    public void addEstudante(Estudante estudante) {
        estudante.setTurma(this);
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
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.id);
        hash = 83 * hash + Objects.hashCode(this.nome);
        hash = 83 * hash + Objects.hashCode(this.ano);
        hash = 83 * hash + Objects.hashCode(this.curso);
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
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.ano, other.ano)) {
            return false;
        }
        if (!Objects.equals(this.curso, other.curso)) {
            return false;
        }
        return true;
    }

}
