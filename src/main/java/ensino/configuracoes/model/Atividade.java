package ensino.configuracoes.model;

import ensino.util.types.Periodo;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name = "Atividade")
@Table(name = "atividade")
public class Atividade implements Serializable {

    @EmbeddedId
    private AtividadeId id;

    @Embedded
    private Periodo periodo;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "legenda_id")
    private Legenda legenda;
    /**
     * Atributo utilizado para marcar o objeto para remoção futura. Valor
     * padrão: false
     */
    @Transient
    private Boolean deleted;

    public Atividade() {
        id = new AtividadeId();
        deleted = false;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public AtividadeId getId() {
        return id;
    }

    public void setId(AtividadeId id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Legenda getLegenda() {
        return legenda;
    }

    public void setLegenda(Legenda legenda) {
        this.legenda = legenda;
    }

    public void delete() {
        deleted = true;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Objects.hashCode(this.id);
        hash = 73 * hash + Objects.hashCode(this.periodo);
        hash = 73 * hash + Objects.hashCode(this.descricao);
        hash = 73 * hash + Objects.hashCode(this.legenda);
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
        if (!Objects.equals(this.legenda, other.legenda)) {
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
