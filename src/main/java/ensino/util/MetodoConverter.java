/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.util;

import ensino.patterns.BaseObject;
import javax.persistence.AttributeConverter;

/**
 *
 * @author santos
 */
public class MetodoConverter implements AttributeConverter<BaseObject, String> {

    @Override
    public String convertToDatabaseColumn(BaseObject x) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BaseObject convertToEntityAttribute(String y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
