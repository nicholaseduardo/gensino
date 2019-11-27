/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.util.types;

/**
 *
 * @author nicho
 */
public enum Turno {
    MATUTINO(0), VESPERTINO(1), NOTURNO(2);
    
    private final int value;

    Turno(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    public static Turno of(int value) {
        switch(value) {
            default:
            case 0: return MATUTINO;
            case 1: return VESPERTINO;
            case 2: return NOTURNO;
        }
    }
    
    @Override
    public String toString() {
        switch(value) {
            case 0: return "Matutino";
            case 1: return "Vespertino";
            case 2: return "Noturno";
        }
        return null;
    }
}
