/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.configuracoes.model.Estudante;
import ensino.helpers.DateHelper;
import ensino.util.TipoAulaConverter;
import ensino.util.types.Presenca;
import ensino.util.types.TipoAula;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author nicho
 */
@Entity
@Table(name = "diario")
public class Diario implements Serializable {
    
    @EmbeddedId
    private DiarioId id;
    
    /**
     * Atributo utilizado para registrar a data da aula.
     */
    @Column(name = "data_diario", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date data;
    
    /**
     * Atributo utilizado para registrar o horário de início da aula.
     */
    @Column
    private String horario;
    /**
     * Atributo utilizado para registrar o conteúdo programático passado em sala
     * de aula.
     */
    @Lob
    @Column(name = "conteudo", columnDefinition = "CLOB")
    private String conteudo;
    
    /**
     * Atributo utilizado para registrar alguma observação referente a aula.
     */
    @Lob
    @Column(name = "observacoes", columnDefinition = "CLOB")
    private String observacoes;
    
    /**
     * Atributo utilizado para registrar o tipo de aula. O tipo de aula pode
     * ser:<br/>
     * <ul>
     * <li>ANTECIPACAO</li>
     * <li>REPOSICAO</li>
     * <li>NORMAL</li>
     * </ul>
     */
    @Column(name = "tipoAula", nullable = false)
    @Convert(converter = TipoAulaConverter.class)
    private TipoAula tipoAula;
    
    /**
     * Atributo utilizado para registrar os estudantes que participaram da aula
     * no dia e horário definido.
     */
    @OneToMany(mappedBy = "id.diario", 
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH},
            fetch = FetchType.LAZY, orphanRemoval = true)
    private List<DiarioFrequencia> frequencias;

    public Diario() {
        id = new DiarioId();
        frequencias = new ArrayList<>();
    }

    public DiarioId getId() {
        return id;
    }

    public void setId(DiarioId id) {
        this.id = id;
    }

    public PlanoDeEnsino getPlanoDeEnsino() {
        return id.getPlanoDeEnsino();
    }
    
    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public TipoAula getTipoAula() {
        return tipoAula;
    }

    public void setTipoAula(TipoAula tipoAula) {
        this.tipoAula = tipoAula;
    }

    public void addFrequencia(DiarioFrequencia frequencia) {
        frequencia.getId().setDiario(this);
        this.frequencias.add(frequencia);
    }

    public void removeFrequencia(DiarioFrequencia frequencia) {
        this.frequencias.remove(frequencia);
    }

    public List<DiarioFrequencia> getFrequencias() {
        return frequencias;
    }

    public void setFrequencias(List<DiarioFrequencia> frequencias) {
        this.frequencias = frequencias;
    }

    public boolean hasFrequencias() {
        return !frequencias.isEmpty();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.id);
        hash = 17 * hash + Objects.hashCode(this.data);
        hash = 17 * hash + Objects.hashCode(this.horario);
        hash = 17 * hash + Objects.hashCode(this.conteudo);
        hash = 17 * hash + Objects.hashCode(this.observacoes);
        hash = 17 * hash + Objects.hashCode(this.tipoAula);
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
        final Diario other = (Diario) obj;
        if (!Objects.equals(this.horario, other.horario)) {
            return false;
        }
        if (!Objects.equals(this.conteudo, other.conteudo)) {
            return false;
        }
        if (!Objects.equals(this.observacoes, other.observacoes)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.data, other.data)) {
            return false;
        }
        if (this.tipoAula != other.tipoAula) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return DateHelper.dateToString(data, "dd/MM/yyyy");
    }

    public void criarFrequencia(List<Estudante> estudantes) {
        int seqId = 1;
        for (int i = 0; i < estudantes.size(); i++) {
            Estudante estudante = estudantes.get(i);
            
            DiarioFrequencia df = DiarioFrequenciaFactory.getInstance()
                    .createObject(new DiarioFrequenciaId(seqId++, this, estudante),
                            Presenca.PONTO);
            addFrequencia(df);
        }
    }

}
