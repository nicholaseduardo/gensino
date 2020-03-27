/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import ensino.patterns.BaseObject;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author nicho
 */
@Entity
@Table(name = "instrumentoAvaliacao")
public class InstrumentoAvaliacao extends BaseObject {
    
    public InstrumentoAvaliacao() {
        super();
    }

}
