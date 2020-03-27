/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author santos
 */
@Embeddable
public class CalendarioId implements Serializable {

    @Column(name = "ano")
    private Integer ano;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campus_id")
    private Campus campus;
    
    public CalendarioId() {
        
    }

    public CalendarioId(Integer ano, Campus campus) {
        this.ano = ano;
        this.campus = campus;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.ano);
        hash = 13 * hash + Objects.hashCode(this.campus);
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
        final CalendarioId other = (CalendarioId) obj;
        if (!Objects.equals(this.ano, other.ano)) {
            return false;
        }
        if (!Objects.equals(this.campus, other.campus)) {
            return false;
        }
        return true;
    }

}
