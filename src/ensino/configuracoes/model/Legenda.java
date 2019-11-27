package ensino.configuracoes.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Legenda {

    private Integer id;
    private String nome;
    private Boolean letivo;
    private Boolean informativo;
    private Color cor;
    private List<Atividade> atividades;

    public Legenda() {
        this.atividades = new ArrayList<>();
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

    public List<Atividade> getAtividades() {
        return atividades;
    }

    public void setAtividades(List<Atividade> atividades) {
        this.atividades = atividades;
    }

    public void addAtividade(Atividade at) {
        atividades.add(at);
    }

    public void removeAtividade(Atividade at) {
        atividades.remove(at);
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
