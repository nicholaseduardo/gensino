/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.util.types;

import java.util.stream.Stream;

/**
 *
 * @author nicho
 */
public enum TipoAula {
    ANTECIPACAO("A"), REPOSICAO("R"), NORMAL("N");

    private final String value;

    TipoAula(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
    public static TipoAula of(String value) {
        return Stream.of(TipoAula.values()).filter(t -> t.getValue().equals(value))
                .findFirst().orElseThrow(IllegalArgumentException::new );
    }

    @Override
    public String toString() {
        switch (value) {
            case "A":
                return "Antecipação";
            case "R":
                return "Reposição";
            default:
            case "N":
                return "Normal";
        }
    }
}
