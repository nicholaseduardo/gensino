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
 * @author santos
 */
@Entity
@Table(name = "conteudo")
public class Conteudo implements Serializable {

    /**
     * Atributo criado para identificar unicamente o conteudo
     */
    @EmbeddedId
    private ConteudoId id;

    /**
     * Atributo criado para indicar qual será a descrição do conteúdo limitado a
     * 255 caracteres
     */
    @Column(name = "descricao")
    private String descricao;

    /**
     * Atributo criado para determinar qual será a sequência do conteúdo visto
     * que esta estrutura será hierarquizada
     */
    @Column(name = "sequencia")
    private Integer sequencia;

    /**
     * Atributo criado para identificar o nível hierárquico do conteúdo
     */
    @Column(name = "nivel")
    private Integer nivel;

    /**
     * Atributo criado para estabelecer a hierarquia entre os conteúdos
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns(value = {
        @JoinColumn(name = "parent_unidadeCurricular_id"),
        @JoinColumn(name = "parent_curso_id"),
        @JoinColumn(name = "parent_campus_id"),
        @JoinColumn(name = "conteudo_id")
    })
    private Conteudo conteudoParent;

    public Conteudo() {
        id = new ConteudoId();
    }

    public ConteudoId getId() {
        return id;
    }

    public void setId(ConteudoId id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getSequencia() {
        return sequencia;
    }

    public void setSequencia(Integer sequencia) {
        this.sequencia = sequencia;
    }

    public Conteudo getConteudoParent() {
        return conteudoParent;
    }

    public void setConteudoParent(Conteudo conteudoParent) {
        this.conteudoParent = conteudoParent;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public Boolean hasParent() {
        return conteudoParent != null;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.id);
        hash = 17 * hash + Objects.hashCode(this.descricao);
        hash = 17 * hash + Objects.hashCode(this.sequencia);
        hash = 17 * hash + Objects.hashCode(this.nivel);
        hash = 17 * hash + Objects.hashCode(this.conteudoParent);
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
        if (!(obj instanceof Conteudo)) {
            return false;
        }
        final Conteudo other = (Conteudo) obj;
        if (!Objects.equals(this.descricao, other.getDescricao())) {
            return false;
        }
        if (!Objects.equals(this.id, other.getId())) {
            return false;
        }
        if (!Objects.equals(this.sequencia, other.getSequencia())) {
            return false;
        }
        if (!Objects.equals(this.nivel, other.getNivel())) {
            return false;
        }
        if (!Objects.equals(this.conteudoParent, other.getConteudoParent())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        int length = this.descricao.length();
        if (length > 70) {
            length = 70;
        }
        return String.format("[%d] %s", this.id.getId(), this.descricao.substring(0, length));
    }
}
