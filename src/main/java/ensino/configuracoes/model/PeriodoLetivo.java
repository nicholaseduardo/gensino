package ensino.configuracoes.model;

import ensino.util.types.Periodo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name = "PeriodoLetivo")
@Table(name = "periodoLetivo")
public class PeriodoLetivo implements Serializable {
    
    @EmbeddedId
    private PeriodoLetivoId id;

    @Column(name = "descricao", nullable = false)
    private String descricao;
    
    @Embedded
    private Periodo periodo;
    
    @OneToMany(mappedBy = "id.periodoLetivo", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<SemanaLetiva> semanasLetivas;
    /**
     * Atributo utilizado para marcar o objeto para remoção futura.
     * Valor padrão: false
     */
    @Transient
    private Boolean deleted;

    public PeriodoLetivo() {
        id = new PeriodoLetivoId();
        deleted = false;
        semanasLetivas = new ArrayList<>();
    }

    public PeriodoLetivoId getId() {
        return id;
    }

    public void setId(PeriodoLetivoId id) {
        this.id = id;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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

    public void addSemanaLetiva(SemanaLetiva semanaLetiva) {
        semanaLetiva.getId().setPeriodoLetivo(this);
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
   
    public void clearSemanasLetivas() {
        this.semanasLetivas.clear();
    }
    
    /**
     * Recupera a lista de atividades realizadas na semana
     * @param semana    Semana do ano a ser verificada
     * @return 
     */
    private List<Atividade> getAtividadesPorSemana(int semana) {
        List<Atividade> lista = new ArrayList();
        Calendar cal = Calendar.getInstance();
        
        Iterator<Atividade> it = id.getCalendario().getAtividades().iterator();
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
    
    public void delete() {
        deleted = true;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.id);
        hash = 61 * hash + Objects.hashCode(this.descricao);
        hash = 61 * hash + Objects.hashCode(this.periodo);
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
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.periodo, other.periodo)) {
            return false;
        }
        return true;
    }
    

    @Override
    public String toString() {
        return descricao;
    }
}
