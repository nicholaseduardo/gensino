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
        return Stream.of(Turno.values()).filter(t -> t.getValue() == value)
                .findFirst().orElseThrow(IllegalArgumentException::new );
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
