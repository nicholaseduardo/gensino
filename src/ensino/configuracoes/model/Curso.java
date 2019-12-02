/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import ensino.patterns.BaseObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.ImageIcon;

/**
 *
 * @author nicho
 */
public class Curso extends BaseObject {

    private ImageIcon imagem;
    private Campus campus;
    
    private List<UnidadeCurricular> unidadesCurriculares;
    private List<Turma> turmas;

    public Curso() {
        unidadesCurriculares = new ArrayList<>();
        turmas = new ArrayList<>();
    }

    public ImageIcon getImagem() {
        return imagem;
    }

    public void setImagem(ImageIcon imagem) {
        this.imagem = imagem;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }
    
    public void addUnidadeCurricular(UnidadeCurricular unidadeCurricular) {
        unidadeCurricular.setCurso(this);
        unidadesCurriculares.add(unidadeCurricular);
    }
    
    public void upddateUnidadeCurricular(UnidadeCurricular unidadeCurricular) {
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
        turma.setCurso(this);
        turmas.add(turma);
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
        if (!super.equals(obj)) {
            return false;
        }
        final Curso other = (Curso) obj;
        return Objects.equals(this.campus, other.campus);
    }
    
    @Override
    public String toString() {
        return nome;
    }

}
