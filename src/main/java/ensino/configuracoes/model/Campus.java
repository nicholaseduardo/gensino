/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import ensino.patterns.BaseObject;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author nicho
 */
@Entity
@Table(name = "campus")
public class Campus extends BaseObject {

    @OneToMany(mappedBy = "id.campus", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Curso> cursos;
    
    @OneToMany(mappedBy = "id.campus", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Calendario> calendarios;
    
    public Campus() {
        super();
        cursos = new ArrayList();
        calendarios = new ArrayList();
    }
    
    public List<Curso> getCursos() {
        return this.cursos;
    }

    public void setCursos(List<Curso> cursos) {
        this.cursos = cursos;
    }

    public void addCurso(Curso c) {
        c.getId().setCampus(this);
        cursos.add(c);
    }

    public void removeCurso(Curso c) {
        c.getId().setCampus(null);
        cursos.remove(c);
    }
    
    public List<Calendario> getCalendarios() {
        return this.calendarios;
    }

    public void setCalendarios(List<Calendario> calendarios) {
        this.calendarios = calendarios;
    }

    public void addCalendario(Calendario c) {
        c.getId().setCampus(this);
        calendarios.add(c);
    }

    public void removeCalendario(Calendario c) {
        c.getId().setCampus(null);
        calendarios.remove(c);
    }

    public boolean hasCalendario() {
        return calendarios.size() > 0;
    }
    
    @Override
    public String toString() {
        return nome;
    }

}
