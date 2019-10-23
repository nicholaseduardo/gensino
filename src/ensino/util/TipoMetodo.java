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
public enum TipoMetodo {
    TECNICA(0), RECURSO(1), INSTRUMENTO(2);

    private final int tipo;

    TipoMetodo(int tipo) {
        this.tipo = tipo;
    }

    public int getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        switch (tipo) {
            default:
            case 0:
                return "Técnica";
            case 1:
                return "Recurso";
            case 2:
                return "Instrumento avaliativo";
        }
    }
}
