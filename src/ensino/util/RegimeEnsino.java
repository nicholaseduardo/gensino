/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.util;

/**
 *
 * @author nicho
 */
public enum RegimeEnsino {
    ANUAL(1), SEMESTRAL(2);

    private final int tipo;

    RegimeEnsino(int tipo) {
        this.tipo = tipo;
    }
    
    public int getTipo() {
        return tipo;
    }
    
    @Override
    public String toString() {
        switch(tipo) {
            case 1: return "ANUAL";
            case 2: return "SEMESTRAL";
        }
        return null;
    }
}
