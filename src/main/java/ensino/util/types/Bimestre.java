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
public enum Bimestre {
    PRIMEIRO(0), SEGUNDO(1), TERCEIRO(2), QUARTO(3);

    private final int value;

    Bimestre(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
    public static Bimestre of(int value) {
        return Stream.of(Bimestre.values()).filter(t -> t.getValue() == value)
                .findFirst().orElseThrow(IllegalArgumentException::new );
    }

    @Override
    public String toString() {
        switch (value) {
            default:
            case 0:
                return "1.o Bimestre";
            case 1:
                return "2.o Bimestre";
            case 2:
                return "3.o Bimestre";
            case 3:
                return "4.o Bimestre";
        }
    }
}
