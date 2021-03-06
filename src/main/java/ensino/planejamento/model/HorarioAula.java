/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.util.DiaDaSemanaConverter;
import ensino.util.TurnoConverter;
import ensino.util.types.DiaDaSemana;
import ensino.util.types.Turno;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author nicho
 */
@Entity
@Table(name = "horarioAula")
public class HorarioAula implements Serializable {
    
    @EmbeddedId
    private HorarioAulaId id;
    
    /**
     * Atributo utilizado para indicar o dia da semana ao qual
     * terá aula com a disciplina do plano de ensino.<br/>
     * Para tanto, deve-se utilizar uma das constantes da enum<code>DiaDaSemana</code>
     * <ul>
     *      <li>DiaDaSemana.MONDAY para segunda-feira</li>
     *      <li>DiaDaSemana.TUESDAY para terça-feira</li>
     *      <li>DiaDaSemana.WEDNESDAY para quarta-feira</li>
     *      <li>DiaDaSemana.THURSDAY para quinta-feira</li>
     *      <li>DiaDaSemana.FRIDAY para sexta-feira</li>
     *      <li>DiaDaSemana.SATURDAY para sábado</li>
     *      <li>DiaDaSemana.MONDAY para domingo</li>
     * </ul>
     */
    @Column(name = "diaDaSemana", nullable = false, columnDefinition = "INTEGER")
    @Convert(converter = DiaDaSemanaConverter.class)
    private DiaDaSemana diaDaSemana;
    /**
     * Indica qual o horário da aula.
     * Informa a hora, no formato (HH:MM), de início da aula da disciplina do 
     * plano de ensino.
     */
    @Column(name = "horario")
    private String horario;
    /**
     * Atributo utilizado para indicar o turno do horário.
     * Para tanto, deve-se utilizar um objeto enum <code>Turno</code>:
     * <ul>
     *      <li>Turno.MATUTINO para Manhã</li>
     *      <li>Turno.VESPERTINO para Tarde</li>
     *      <li>Turno.NOTURNO para Noite</li>
     * </ul>
     */
    @Column(name = "turno", nullable = false, columnDefinition = "INTEGER")
    @Convert(converter = TurnoConverter.class)
    private Turno turno;
    
    @Transient
    private Boolean deleted;
    
    public HorarioAula() {
        id = new HorarioAulaId();
        deleted = false;
    }
    
    public void delete() {
        this.deleted = true;
    }
    
    public Boolean isDeleted() {
        return this.deleted;
    }

    public HorarioAulaId getId() {
        return id;
    }

    public void setId(HorarioAulaId id) {
        this.id = id;
    }
    
    public Boolean hasId() {
        return id.hasId();
    }
    
    public PlanoDeEnsino getPlanoDeEnsino() {
        return id.getPlanoDeEnsino();
    }

    public DiaDaSemana getDiaDaSemana() {
        return diaDaSemana;
    }

    public void setDiaDaSemana(DiaDaSemana diaDaSemana) {
        this.diaDaSemana = diaDaSemana;
    }

    public String getHorario() {
        return horario;
    }
    
    public Date getHorarioAsDate() {
        Calendar cal = Calendar.getInstance();
        String aHorario[] = horario.split(":");
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(aHorario[0]));
        cal.set(Calendar.MINUTE, Integer.parseInt(aHorario[1]));
        return cal.getTime();
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.id);
        hash = 23 * hash + Objects.hashCode(this.diaDaSemana);
        hash = 23 * hash + Objects.hashCode(this.horario);
        hash = 23 * hash + Objects.hashCode(this.turno);
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
        final HorarioAula other = (HorarioAula) obj;
        if (!Objects.equals(this.horario, other.horario)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (this.diaDaSemana != other.diaDaSemana) {
            return false;
        }
        if (this.turno != other.turno) {
            return false;
        }
        return true;
    }
    
}
