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
import javax.persistence.Table;

/**
 *
 * @author santos
 */
@Entity
@Table(name = "objetivoUCConteudo")
public class ObjetivoUCConteudo implements Serializable {

    @EmbeddedId
    private ObjetivoUCConteudoId id;

    @Column(name = "ordem")
    private Integer ordem;

    public ObjetivoUCConteudo() {
        id = new ObjetivoUCConteudoId();
    }

    public ObjetivoUCConteudoId getId() {
        return id;
    }

    public void setId(ObjetivoUCConteudoId id) {
        this.id = id;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public ObjetivoUC getObjetivoUC() {
        if (id != null) {
            return id.getObjetivo();
        }
        return null;
    }

    public Conteudo getConteudo() {
        if (id != null) {
            return id.getConteudo();
        }
        return null;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.id);
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
        final ObjetivoUCConteudo other = (ObjetivoUCConteudo) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
