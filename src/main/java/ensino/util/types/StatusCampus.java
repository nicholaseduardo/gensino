/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.util.types;

import java.util.stream.Stream;

/**
 *
 * @author santos
 */
public enum StatusCampus {
    VIGENTE("V"), ANTERIOR("A");
    
    private final String value;
    
    StatusCampus(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    public static StatusCampus of(String value) {
        return Stream.of(StatusCampus.values()).filter(t -> t.getValue().equals(value))
                .findFirst().orElseThrow(IllegalArgumentException::new );
    }

    @Override
    public String toString() {
        switch (value) {
            default:
            case "V":
                return "Vigente";
            case "A":
                return "Anterior";
        }
    }
}
