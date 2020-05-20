/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.util.types;

/**
 *
 * @author santos
 */
public enum TabsPlano {
    IDEN(0), EMEN(1), REF(2), REC(3), OBJ(4), ESP(5), DET(6), PAVA(7), HOR(8),
    CON(9), FREQ(10), AVA(11), VIEW_PLAN(12), NOTAS(13), CONTROLE(14), PE(15);

    private final int index;

    TabsPlano(int index) {
        this.index = index;
    }

    public int toInt() {
        return index;
    }
    
    public static TabsPlano of(int value) {
        switch(value) {
            default:
            case 0:
                return IDEN;
            case 1:
                return EMEN;
            case 2:
                return REF;
            case 3:
                return REC;
            case 4:
                return OBJ;
            case 5:
                return ESP;
            case 6:
                return DET;
            case 7:
                return PAVA;
            case 8:
                return HOR;
            case 9:
                return CON;
            case 10:
                return FREQ;
            case 11:
                return AVA;
            case 12:
                return VIEW_PLAN;
            case 13:
                return NOTAS;
            case 14:
                return CONTROLE;
            case 15:
                return PE;
        }
    }

    @Override
    public String toString() {
        switch (index) {
            case 0:
                return "Identificação";
            case 1:
                return "Ementa";
            case 2:
                return "Referências bibliográficas";
            case 3:
                return "Recup. da aprendizagem";
            case 4:
                return "Objetivos";
            case 5:
                return "Obj. Específicos";
            case 6:
                return "Detalhamento";
            case 7:
                return "Plano de Avaliações";
            case 8:
                return "Horários das aulas";
            case 9:
                return "Conteúdo";
            case 10:
                return "Frequência";
            case 11:
                return "Avaliações";
            case 12:
                return "Plano de Ensino";
            case 13:
                return "Notas do Diário";
            case 14:
                return "Painel de Controle";
            case 15:
                return "Permanência Estudantil";
        }
        return null;
    }
}
