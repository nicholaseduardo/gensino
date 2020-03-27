/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.util;

import ensino.util.types.Bimestre;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author santos
 */
@Converter(autoApply = true)
public class BimestreConverter implements AttributeConverter<Bimestre, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Bimestre bimestre) {
        if (bimestre == null) {
            return null;
        }
        return bimestre.getValue();
    }

    @Override
    public Bimestre convertToEntityAttribute(Integer bimestre) {
        if (bimestre == null)
            return null;
        return Bimestre.of(bimestre);
    }
    
}
