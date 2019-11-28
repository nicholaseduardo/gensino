package ensino.configuracoes.model;

import ensino.util.types.Periodo;
import java.util.Calendar;
import java.util.Objects;

public class Atividade {

    private Integer id;
    private Periodo periodo;
    private String descricao;
    private Calendario calendario;
    private Legenda legenda;
    
    public Atividade() {
        
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Calendario getCalendario() {
        return calendario;
    }

    public void setCalendario(Calendario calendario) {
        this.calendario = calendario;
    }

    public Legenda getLegenda() {
        return legenda;
    }

    public void setLegenda(Legenda legenda) {
        this.legenda = legenda;
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
        final Atividade other = (Atividade) obj;
        if (!Objects.equals(this.descricao, other.descricao)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.periodo, other.periodo)) {
            return false;
        }
        if (!this.calendario.equals(other.calendario)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(periodo.getDe());

        Integer diaDe = calendar.get(Calendar.DAY_OF_MONTH);
        sb.append(String.format("%02d", diaDe));
        if (!periodo.isMesmaData()) {
            calendar.setTime(periodo.getAte());
            Integer diaAte = calendar.get(Calendar.DAY_OF_MONTH);
            sb.append(String.format(" a %02d", diaAte));
        }
        sb.append(" - " + descricao);

        return sb.toString();
    }

}
