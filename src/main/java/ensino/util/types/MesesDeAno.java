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
public enum MesesDeAno {
    JAN(1), FEV(2), MAR(3), ABR(4), MAI(5), JUN(6), JUL(7), AGO(8), SET(9), OUT(10), NOV(11), DEZ(12);
    
    private final int value;

    MesesDeAno(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    /**
     * Retorna uma instância do enum
     * @param value Zero (0) inicia em Janeiro.
     * @return 
     */
    public static MesesDeAno of(int value) {
        switch(value) {
            default:
            case 0: return JAN;
            case 1: return FEV;
            case 2: return MAR;
            case 3: return ABR;
            case 4: return MAI;
            case 5: return JUN;
            case 6: return JUL;
            case 7: return AGO;
            case 8: return SET;
            case 9: return OUT;
            case 10: return NOV;
            case 11: return DEZ;
        }
    }
    
    @Override
    public String toString() {
        switch(value) {
            case 1: return "Janeiro";
            case 2: return "Fevereiro";
            case 3: return "Março";
            case 4: return "Abril";
            case 5: return "Maio";
            case 6: return "Junho";
            case 7: return "Julho";
            case 8: return "Agosto";
            case 9: return "Setembro";
            case 10: return "Outubro";
            case 11: return "Novembro";
            case 12: return "Dezembro";
        }
        return null;
    }
}
