/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author nicho
 */
@Entity
@Table(name = "curso")
public class Curso implements Serializable {

    @EmbeddedId
    private CursoId id;
    
    @Column(name = "nome", length = 255)
    private String nome;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nivelEnsino_id")
    private NivelEnsino nivelEnsino;
    
    @OneToMany(mappedBy = "id.curso", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Turma> turmas;
    
    @OneToMany(mappedBy = "id.curso", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UnidadeCurricular> unidadesCurriculares;
    
    public Curso() {
        id = new CursoId();
        turmas = new ArrayList<>();
        unidadesCurriculares = new ArrayList<>();
    }

    public CursoId getId() {
        return id;
    }

    public void setId(CursoId id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public Campus getCampus() {
        return id.getCampus();
    }

    public NivelEnsino getNivelEnsino() {
        return nivelEnsino;
    }

    public void setNivelEnsino(NivelEnsino nivelEnsino) {
        this.nivelEnsino = nivelEnsino;
    }
    
    public void addUnidadeCurricular(UnidadeCurricular unidadeCurricular) {
        unidadeCurricular.getId().setCurso(this);
        unidadesCurriculares.add(unidadeCurricular);
    }
    
    public void updateUnidadeCurricular(UnidadeCurricular unidadeCurricular) {
        for(UnidadeCurricular und : unidadesCurriculares) {
            if (und.getId().equals(unidadeCurricular.getId())) {
                und = unidadeCurricular;
                break;
            }
        }
    }
    
    public void removeUnidadeCurricular(UnidadeCurricular unidadeCurricular) {
        unidadesCurriculares.remove(unidadeCurricular);
    }

    public List<UnidadeCurricular> getUnidadesCurriculares() {
        if (unidadesCurriculares == null)
            unidadesCurriculares = new ArrayList<>();
        return unidadesCurriculares;
    }

    public void setUnidadesCurriculares(List<UnidadeCurricular> unidadesCurriculares) {
        this.unidadesCurriculares = unidadesCurriculares;
    }
    
    public void addTurma(Turma turma) {
        turma.getId().setCurso(this);
        turmas.add(turma);
    }
    
    public void updateTurma(Turma turma) {
        for(Turma t : turmas) {
            if (t.getId().equals(t.getId())) {
                t = turma;
                break;
            }
        }
    }
    
    public void removeTurma(Turma turma) {
        turmas.remove(turma);
    }

    public List<Turma> getTurmas() {
        if (turmas == null)
            turmas = new ArrayList<>();
        return turmas;
    }

    public void setTurmas(List<Turma> turmas) {
        this.turmas = turmas;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
        hash = 59 * hash + Objects.hashCode(this.nome);
        hash = 59 * hash + Objects.hashCode(this.nivelEnsino);
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
        final Curso other = (Curso) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.nivelEnsino, other.nivelEnsino)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return nome;
    }

}
