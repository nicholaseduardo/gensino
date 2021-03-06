/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author nicho
 */
@Entity
@Table(name = "etapaEnsino")
public class EtapaEnsino implements Serializable {

    @EmbeddedId
    private EtapaEnsinoId id;
    
    @Column(name = "nome")
    private String nome;
    
    @Column(name = "recuperacao")
    private Boolean recuperacao;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns(value = {
        @JoinColumn(name = "parent_nivelEnsino_id"),
        @JoinColumn(name = "etapaEnsino_id")
    })
    private EtapaEnsino nivelDependente;

    public EtapaEnsino() {
        id = new EtapaEnsinoId();
    }

    public EtapaEnsinoId getId() {
        return id;
    }

    public void setId(EtapaEnsinoId id) {
        this.id = id;
    }
    
    public Boolean hasId() {
        return id.hasId();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public NivelEnsino getNivelEnsino() {
        return id.getNivelEnsino();
    }

    public Boolean getRecuperacao() {
        return recuperacao;
    }
    
    public Boolean isRecuperacao() {
        return recuperacao == Boolean.TRUE;
    }

    public void setRecuperacao(Boolean recuperacao) {
        this.recuperacao = recuperacao;
    }

    public EtapaEnsino getNivelDependente() {
        return nivelDependente;
    }

    public void setNivelDependente(EtapaEnsino nivelDependente) {
        this.nivelDependente = nivelDependente;
    }
    
    public Boolean hasParent() {
        return nivelDependente != null;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.id);
        hash = 37 * hash + Objects.hashCode(this.nome);
        hash = 37 * hash + Objects.hashCode(this.recuperacao);
        hash = 37 * hash + Objects.hashCode(this.nivelDependente);
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
        final EtapaEnsino other = (EtapaEnsino) obj;
        if (!Objects.equals(this.nome, other.getNome())) {
            return false;
        }
        if (!Objects.equals(this.id, other.getId())) {
            return false;
        }
        if (!Objects.equals(this.recuperacao, other.getRecuperacao())) {
            return false;
        }
        if (!Objects.equals(this.nivelDependente, other.getNivelDependente())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nome;
    }

}
