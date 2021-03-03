/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.util.types;

import java.util.Arrays;
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
    BACKWARD(34), FORWARD(35), DELETE(36), UP(37), DOWN(38);
    
    private final Integer value;
    private static final String[] sData = {"Adicionar", "Excluir", "Alterar", "Salvar",
        "Fechar", "Duplicar", "Gerar", "Importar", "Novo", "Plano de Ensino",
        "Identificação", "Obj. Específicos", "Detalhamento", "Plano de Avaliações",
        "Horários das aulas", "Conteúdo Programático", "Frequência", "Avaliações",
        "Plano de Ensino", "Notas do Diário", "Painel de Controle", "Permanência Estudantil",
        "Referências bibliográficas", "Estudantes", "Conteudo da U.C.", "Cancelar",
        "Buscar", "Limpar", "Diário", "Relatórios", "Estrutura", "Seleção",
        "Unidade Curricular", "Turma", "Para trás", "Para frente", "Delete",
        "Para cima", "Para baixo"};
    
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
    
    public static AcoesBotoes of(String value) {
        int index = Arrays.binarySearch(sData, value);
        return of(index);
    }

    @Override
    public String toString() {
        return sData[this.value];
    }
}
