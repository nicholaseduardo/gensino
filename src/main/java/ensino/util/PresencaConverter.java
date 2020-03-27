/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.util;

import ensino.util.types.Presenca;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author santos
 */
@Converter(autoApply = true)
public class PresencaConverter implements AttributeConverter<Presenca, String> {

    @Override
    public String convertToDatabaseColumn(Presenca presenca) {
        if (presenca == null) {
            return null;
        }
        return presenca.getValue();
    }

    @Override
    public Presenca convertToEntityAttribute(String presenca) {
        if (presenca == null)
            return null;
        return Presenca.of(presenca);
    }
    
}
