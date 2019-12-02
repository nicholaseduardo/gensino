/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import ensino.patterns.BaseObject;

/**
 *
 * @author nicho
 */
public class InstrumentoAvaliacao extends BaseObject {
    
    public InstrumentoAvaliacao() {
        super();
    }

    @Override
    public String toString() {
        return nome;
    }
}
