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
public enum AcoesBotoes {
    ADD(0), DELETE(1), EDIT(2), SAVE(3),
    CLOSE(4), DUPLICATE(5), GENERATE(6),
    IMPORT(7), NEW(8), PLAN(9);
    
    private final Integer value;
    
    AcoesBotoes(Integer value) {
        this.value = value;
    }
    
    public Integer getValue() {
        return value;
    }
    
    public static AcoesBotoes of(Integer value) {
        return Stream.of(AcoesBotoes.values()).filter(t -> t.getValue().equals(value))
                .findFirst().orElseThrow(IllegalArgumentException::new );
    }

    @Override
    public String toString() {
        switch(value) {
            default:
            case 0: return "Adicionar";
            case 1: return "Excluir";
            case 2: return "Alterar";
            case 3: return "Salvar";
            case 4: return "Fechar";
            case 5: return "Duplicar";
            case 6: return "Gerar";
            case 7: return "Importar";
            case 8: return "Novo";
            case 9: return "Plano";
        }
    }
}
