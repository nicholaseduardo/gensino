/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.util;

import ensino.util.types.Turno;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author santos
 */
@Converter(autoApply = true)
public class TurnoConverter implements AttributeConverter<Turno, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Turno turno) {
        if (turno == null) {
            return null;
        }
        return turno.getValue();
    }

    @Override
    public Turno convertToEntityAttribute(Integer turno) {
        if (turno == null)
            return null;
        return Turno.of(turno);
    }
    
}
