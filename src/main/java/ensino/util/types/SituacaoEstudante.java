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
public enum SituacaoEstudante {
    EM_CURSO("EC"), REPROVADO_POR_NOTA("RN"), REPROVADO_POR_FALTA("RF"),
    DESLIGADO("DE"), APROVADO("AP");
    
    private final String value;
    
    SituacaoEstudante(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    public static SituacaoEstudante of(String value) {
        return Stream.of(SituacaoEstudante.values()).filter(t -> t.getValue().equals(value))
                .findFirst().orElseThrow(IllegalArgumentException::new );
    }

    @Override
    public String toString() {
        switch (value) {
            default:
            case "EC":
                return "Em Curso";
            case "RN":
                return "Reprovado por Nota";
            case "RF":
                return "Reprovado por Falta";
            case "DE":
                return "Desligado";
            case "AP":
                return "Aprovado";
        }
    }
}
