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
public enum TipoMetodo {
    TECNICA(0), RECURSO(1), INSTRUMENTO(2);

    private final int value;

    TipoMetodo(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    public static TipoMetodo of(Integer value) {
        return Stream.of(TipoMetodo.values()).filter(t -> t.getValue() == value)
                .findFirst().orElseThrow(IllegalArgumentException::new );
    }

    @Override
    public String toString() {
        switch (value) {
            default:
            case 0:
                return "Técnica";
            case 1:
                return "Recurso";
            case 2:
                return "Instrumento avaliativo";
        }
    }
}
