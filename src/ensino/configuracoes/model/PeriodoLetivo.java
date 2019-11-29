package ensino.configuracoes.model;

import ensino.util.types.Periodo;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class PeriodoLetivo {

    private Integer numero;
    private String descricao;
    private Periodo periodo;
    // parent
    private Calendario calendario;
    // composition
    private List<SemanaLetiva> semanasLetivas;

    public PeriodoLetivo() {
        semanasLetivas = new ArrayList<>();
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Calendario getCalendario() {
        return calendario;
    }

    public void setCalendario(Calendario calendario) {
        this.calendario = calendario;
    }

    public void addSemanaLetiva(SemanaLetiva semanaLetiva) {
        semanaLetiva.setPeriodoLetivo(this);
        semanasLetivas.add(semanaLetiva);
    }

    public void removeSemanaLetiva(SemanaLetiva semanaLetiva) {
        semanasLetivas.remove(semanaLetiva);
    }

    public List<SemanaLetiva> getSemanasLetivas() {
        return semanasLetivas;
    }

    public void setSemanasLetivas(List<SemanaLetiva> semanasLetivas) {
        this.semanasLetivas = semanasLetivas;
    }
    
    /**
     * Recupera a lista de atividades realizadas na semana
     * @param semana    Semana do ano a ser verificada
     * @return 
     */
    private List<Atividade> getAtividadesPorSemana(int semana) {
        List<Atividade> lista = new ArrayList();
        Calendar cal = Calendar.getInstance();
        
        Iterator<Atividade> it = calendario.getAtividades().iterator();
        while(it.hasNext()) {
            Atividade at = it.next();
            cal.setTime(at.getPeriodo().getDe());
            if (cal.get(Calendar.WEEK_OF_YEAR) == semana) {
                lista.add(at);
            } else if (Calendar.WEEK_OF_YEAR > semana) {
                break;
            }
        }
        return lista;
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final PeriodoLetivo other = (PeriodoLetivo) obj;
        if (!Objects.equals(this.descricao, other.descricao)) {
            return false;
        }
        if (!Objects.equals(this.numero, other.numero)) {
            return false;
        }
        if (!Objects.equals(this.periodo, other.periodo)) {
            return false;
        }
        if (!Objects.equals(this.calendario, other.calendario)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
