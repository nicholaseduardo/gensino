/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author santos
 */
@Entity
@Table(name = "permanenciaEstudantil")
public class PermanenciaEstudantil implements Serializable {

    @EmbeddedId
    private PermanenciaEstudantilId id;
    /**
     * Atributo utilizado para registrar a data na qual os estudantes foram
     * atendidos
     */
    @Column(name = "dataAtendimento", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date dataAtendimento;
    /**
     * Atributo utilizado para registrar a data e hora na qual os estudantes
     * foram atendidos
     */
    @Column(name = "horaAtendimento", nullable = true)
    @Temporal(TemporalType.TIME)
    private Date horaAtendimento;

    /**
     * Descrição do conteúdo discutido com o estudante
     */
    @Lob
    @Column(name = "descricao", columnDefinition = "CLOB")
    private String descricao;

    /**
     * Atributo utilizado para registrar os estudantes que participaram da aula
     * no dia e horário definido.
     */
    @OneToMany(mappedBy = "id.permanenciaEstudantil", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy(value = "id.sequencia")
    private List<AtendimentoEstudante> atendimentos;

    public PermanenciaEstudantil() {
        id = new PermanenciaEstudantilId();
        atendimentos = new ArrayList();
    }

    public PermanenciaEstudantilId getId() {
        return id;
    }

    public void setId(PermanenciaEstudantilId id) {
        this.id = id;
    }

    public Date getDataAtendimento() {
        return dataAtendimento;
    }

    public void setDataAtendimento(Date atendimento) {
        this.dataAtendimento = atendimento;
    }

    public Date getHoraAtendimento() {
        return horaAtendimento;
    }

    public void setHoraAtendimento(Date horaAtendimento) {
        this.horaAtendimento = horaAtendimento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<AtendimentoEstudante> getAtendimentos() {
        return atendimentos;
    }

    public void setAtendimentos(List<AtendimentoEstudante> atendimentos) {
        if (!atendimentos.isEmpty() && atendimentos.get(0).getId().getPermanenciaEstudantil() == null) {
            atendimentos.forEach((at) -> {
                addAtendimento(at);
            });
        } else {
            this.atendimentos = atendimentos;
        }
    }

    public void addAtendimento(AtendimentoEstudante o) {
        o.getId().setPermanenciaEstudantil(this);
        atendimentos.add(o);
    }

    public void removeAtendimento(AtendimentoEstudante o) {
        atendimentos.remove(o);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.id);
        hash = 13 * hash + Objects.hashCode(this.dataAtendimento);
        hash = 13 * hash + Objects.hashCode(this.horaAtendimento);
        hash = 13 * hash + Objects.hashCode(this.descricao);
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
        final PermanenciaEstudantil other = (PermanenciaEstudantil) obj;
        if (!Objects.equals(this.descricao, other.descricao)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.dataAtendimento, other.dataAtendimento)) {
            return false;
        }
        if (!Objects.equals(this.horaAtendimento, other.horaAtendimento)) {
            return false;
        }
        return true;
    }

}
