package ensino.configuracoes.model;

import ensino.util.ColorConverter;
import java.awt.Color;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "legenda")
public class Legenda implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    
    @Column(name="nome", nullable = false, length = 255)
    private String nome;
    
    @Column(name = "letivo", nullable = false)
    private Boolean letivo;
    
    @Column(name = "informativo", nullable = false)
    private Boolean informativo;
    
    @Column(name = "cor", nullable = false, columnDefinition = "VARCHAR(255)")
    @Convert(converter = ColorConverter.class)
    private Color cor;

    public Legenda() {
        letivo = false;
        informativo = false;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean isLetivo() {
        return letivo;
    }

    public void setLetivo(Boolean letivo) {
        this.letivo = letivo;
    }

    public Boolean isInformativo() {
        return informativo;
    }

    public void setInformativo(Boolean informativo) {
        this.informativo = informativo;
    }

    public Color getCor() {
        return cor;
    }

    public void setCor(Color cor) {
        this.cor = cor;
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final Legenda other = (Legenda) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.letivo, other.letivo)) {
            return false;
        }
        if (!Objects.equals(this.informativo, other.informativo)) {
            return false;
        }
        if (!Objects.equals(this.cor, other.cor)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nome;
    }
}
