package ensino.configuracoes.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "calendario")
public class Calendario implements Serializable {

    @EmbeddedId
    private CalendarioId id;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @OneToMany(mappedBy = "id.calendario", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy(value = "periodo.de")
    private List<Atividade> atividades;

    @OneToMany(mappedBy = "id.calendario", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<PeriodoLetivo> periodosLetivos;

    public Calendario() {
        id = new CalendarioId();
        atividades = new ArrayList();
        periodosLetivos = new ArrayList();
    }

    public CalendarioId getId() {
        return id;
    }

    public void setId(CalendarioId id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void addAtividade(Atividade atividade) {
        atividade.getId().setCalendario(this);
        this.atividades.add(atividade);
    }

    public void updAtividade(Atividade atividade) {
        for (Atividade at : atividades) {
            if (at.getId().equals(atividade.getId())) {
                at = atividade;
                break;
            }
        }
    }

    public void removeAtividade(Atividade atividade) {
        this.atividades.remove(atividade);
    }

    public List<Atividade> getAtividades() {
        return atividades;
    }

    public void setAtividades(List<Atividade> atividades) {
        this.atividades = atividades;
    }

    private Atividade getAtividadePorDia(Date dia) {
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < atividades.size(); i++) {
            Atividade o = atividades.get(i);
            
            if (o.getPeriodo().contemData(dia))
                return o;
                
        }
        return null;
    }

    public Boolean isDiaLetivo(Date dia) {
        Atividade o = this.getAtividadePorDia(dia);
        
        return (o == null || o.getLegenda().isLetivo());
    }

    public void addPeriodoLetivo(PeriodoLetivo periodoLetivo) {
        periodoLetivo.getId().setCalendario(this);
        this.periodosLetivos.add(periodoLetivo);
    }

    public List<PeriodoLetivo> getPeriodosLetivos() {
        return periodosLetivos;
    }

    public void setPeriodosLetivos(List<PeriodoLetivo> periodosLetivos) {
        this.periodosLetivos = periodosLetivos;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.id);
        hash = 79 * hash + Objects.hashCode(this.descricao);
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
        final Calendario other = (Calendario) obj;
        if (!Objects.equals(this.descricao, other.descricao)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(descricao);
        if (!atividades.isEmpty()) {
            int tamanho = atividades.size();
            sb.append(String.format(" (%d atividade", tamanho));
            if (tamanho > 1) {
                sb.append("s");
            }
            sb.append(")");
        }
        return sb.toString();
    }

}
