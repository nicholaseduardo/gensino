/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.configuracoes.model.Estudante;
import ensino.util.types.Presenca;
import java.util.Objects;

/**
 *
 * @author nicho
 */
public class DiarioFrequencia {
    /**
     * Atributo utilizado para identificar uma frequência do diário
     */
    private Integer id;
    /**
     * Atributo utilizado para identificar de qual diário é o registro
     * desta frequência.
     */
    private Diario diario;
    /**
     * Atributo utilizado para registrar a presença ou a falta do estudante.
     *
     * A presença pode ser:<br/>
     * <ul>
     * <li>PRESENTE</li>
     * <li>FALTA</li>
     * </ul>
     */
    private Presenca presenca;
    /**
     * Atributo utilizado para identificar o estudante cuja frequência está
     * sendo registrada.
     */
    private Estudante estudante;
    
    public DiarioFrequencia() {
        presenca = Presenca.PONTO;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.id);
        hash = 37 * hash + Objects.hashCode(this.diario);
        hash = 37 * hash + Objects.hashCode(this.presenca);
        hash = 37 * hash + Objects.hashCode(this.estudante);
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
        final DiarioFrequencia other = (DiarioFrequencia) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.diario, other.diario)) {
            return false;
        }
        if (this.presenca != other.presenca) {
            return false;
        }
        if (!Objects.equals(this.estudante, other.estudante)) {
            return false;
        }
        return true;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Diario getDiario() {
        return diario;
    }

    public void setDiario(Diario diario) {
        this.diario = diario;
    }

    public Presenca getPresenca() {
        return presenca;
    }

    public void setPresenca(Presenca presenca) {
        this.presenca = presenca;
    }

    public Estudante getEstudante() {
        return estudante;
    }

    public void setEstudante(Estudante estudante) {
        this.estudante = estudante;
    }
    
    public String toString() {
        return presenca.getValue();
    }
}
