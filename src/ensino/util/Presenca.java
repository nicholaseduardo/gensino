/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.util;

/**
 *
 * @author nicho
 */
public enum Presenca {
    PONTO("."), PRESENTE("P"), FALTA("F");

    private final String value;

    Presenca(String value) {
        this.value = value;
    }
    
    public static Presenca of(String value) {
        switch(value) {
            default: return PONTO;
            case "P": return PRESENTE;
            case "F": return FALTA;
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        switch (value) {
            default:
                return ".";
            case "P":
                return "Presente";
            case "F":
                return "Falta";
        }
    }
}
