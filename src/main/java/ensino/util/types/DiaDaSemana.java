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
public enum DiaDaSemana {
    SEGUNDA(1), TERCA(2), QUARTA(3), QUINTA(4),
    SEXTA(5), SABADO(6), DOMINGO(7);
    
    private final Integer value;
    
    DiaDaSemana(Integer value) {
        this.value = value;
    }
    
    public Integer getValue() {
        return value;
    }
    
    public static DiaDaSemana of(Integer value) {
        return Stream.of(DiaDaSemana.values()).filter(t -> t.getValue().equals(value))
                .findFirst().orElseThrow(IllegalArgumentException::new );
    }
    
    @Override
    public String toString() {
        switch(value) {
            default:
            case 1:
                return "Segunda-Feira";
            case 2:
                return "Terça-Feira";
            case 3:
                return "Quarta-Feira";
            case 4:
                return "Quinta-Feira";
            case 5:
                return "Sexta-Feira";
            case 6:
                return "Sábado";
            case 7:
                return "Doming";
        }
    }
}
