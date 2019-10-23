/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.defaults.XMLInterface;
import ensino.util.Turno;
import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author nicho
 */
public class HorarioAula implements XMLInterface {
    /**
     * Atributo utilizado para identificar unicamente o
     * horário da aula.
     */
    private Integer id;
    /**
     * Atributo utilizado para indicar o dia da semana ao qual
     * terá aula com a disciplina do plano de ensino.<br/>
     * Para tanto, deve-se utilizar uma das constantes da enum<code>DayOfWeek</code>
     * <ul>
     *      <li>DayOfWeek.MONDAY para segunda-feira</li>
     *      <li>DayOfWeek.TUESDAY para terça-feira</li>
     *      <li>DayOfWeek.WEDNESDAY para quarta-feira</li>
     *      <li>DayOfWeek.THURSDAY para quinta-feira</li>
     *      <li>DayOfWeek.FRIDAY para sexta-feira</li>
     *      <li>DayOfWeek.SATURDAY para sábado</li>
     *      <li>DayOfWeek.MONDAY para domingo</li>
     * </ul>
     */
    private DayOfWeek diaDaSemana;
    /**
     * Indica qual o horário da aula.
     * Informa a hora, no formato (HH:MM), de início da aula da disciplina do 
     * plano de ensino.
     */
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
    private Turno turno;
    /**
     * Atributo utilizado para identificar em qual plano
     * de ensino serão registrados os dados do diário.
     */
    private PlanoDeEnsino planoDeEnsino;
    
    public HorarioAula() {
        this(null, null, null, null);
    }
    
    public HorarioAula(Integer id, DayOfWeek diaDaSemana, String horario,
            Turno turno) {
        this.id = id;
        this.diaDaSemana = diaDaSemana;
        this.horario = horario;
        this.turno = turno;
    }
    
    public HorarioAula(Element e, PlanoDeEnsino planoDeEnsino) {
        this(
                Integer.parseInt(e.getAttribute("id")),
                null,
                e.getAttribute("horario"), null
        );
        this.planoDeEnsino = planoDeEnsino;
        String sSemana = e.getAttribute("diaDaSemana");
        if (sSemana.matches("\\d+")) {
            diaDaSemana = DayOfWeek.of(Integer.parseInt(sSemana));
        }
        String sTurno = e.getAttribute("turno");
        if (sTurno.matches("\\d+")) {
            turno = Turno.of(Integer.parseInt(sTurno));
        }
    }
    
    public HorarioAula(HashMap<String, Object> params) {
        this((Integer) params.get("id"),
                (DayOfWeek) params.get("diaDaSemana"),
                (String) params.get("horario"),
                (Turno) params.get("turno"));
        this.planoDeEnsino = (PlanoDeEnsino) params.get("planoDeEnsino");
    }

    @Override
    public Node toXml(Document doc) {
        Element e = doc.createElement("horarioAula");
        e.setAttribute("id", id.toString());
        e.setAttribute("diaDaSemana", String.valueOf(diaDaSemana.getValue()));
        e.setAttribute("horario", horario);
        e.setAttribute("turno", String.valueOf(turno.getValue()));

        return e;
    }

    @Override
    public HashMap<String, Object> getKey() {
        HashMap<String, Object> map = new HashMap();
        map.put("id", id);

        return map;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.diaDaSemana);
        hash = 97 * hash + Objects.hashCode(this.horario);
        hash = 97 * hash + Objects.hashCode(this.turno);
        hash = 97 * hash + Objects.hashCode(this.planoDeEnsino);
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
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.planoDeEnsino, other.planoDeEnsino)) {
            return false;
        }
        return true;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DayOfWeek getDiaDaSemana() {
        return diaDaSemana;
    }

    public void setDiaDaSemana(DayOfWeek diaDaSemana) {
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

    public PlanoDeEnsino getPlanoDeEnsino() {
        return planoDeEnsino;
    }

    public void setPlanoDeEnsino(PlanoDeEnsino planoDeEnsino) {
        this.planoDeEnsino = planoDeEnsino;
    }
    
}
