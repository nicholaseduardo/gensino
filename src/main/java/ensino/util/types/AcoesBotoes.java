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
    IDEN(10), ESP(11), DET(12), PAVA(13), HOR(14), PE(21), CON(15), FREQ(16), 
    AVA(17), VIEW_PLAN(18), 
    NOTAS(19), CONTROLE(20),
    REFBIB(22), ESTUD(23), CONT_EMENTA(24), PLAN(9), DIARY(28), REPORT(29),

    ADD(0), NEW(8), DEL(1), EDIT(2), SAVE(3), DUPLICATE(5), CLOSE(4), GENERATE(6),
    IMPORT(7), CANCEL(25), SEARCH(26), CLEAR(27), STRUCTURE(30),
    
    SELECTION(31), UC(32), TURMA(33),
    BACKWARD(34), FORWARD(35), DELETE(36);
    
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
            case 9: return "Plano de Ensino";
            case 10: return "Identificação";
            case 11: return "Obj. Específicos";
            case 12: return "Detalhamento";
            case 13: return "Plano de Avaliações";
            case 14: return "Horários das aulas";
            case 15: return "Conteúdo Programático";
            case 16: return "Frequência";
            case 17: return "Avaliações";
            case 18: return "Plano de Ensino";
            case 19: return "Notas do Diário";
            case 20: return "Painel de Controle";
            case 21: return "Permanência Estudantil";
            case 22: return "Referências bibliográficas";
            case 23: return "Estudantes";
            case 24: return "Conteudo da U.C.";
            case 25: return "Cancelar";
            case 26: return "Buscar";
            case 27: return "Limpar";
            case 28: return "Diário";
            case 29: return "Relatórios";
            case 30: return "Estrutura";
            case 31: return "Seleção";
            case 32: return "Unidade Curricular";
            case 33: return "Turma";
            case 34: return "Para trás";
            case 35: return "Para frente";
            case 36: return "Delete";
        }
    }
}
