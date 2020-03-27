/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.util;

import ensino.util.types.TipoAula;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author santos
 */
@Converter(autoApply = true)
public class TipoAulaConverter implements AttributeConverter<TipoAula, String> {

    @Override
    public String convertToDatabaseColumn(TipoAula tipoAula) {
        if (tipoAula == null) {
            return null;
        }
        return tipoAula.getValue();
    }

    @Override
    public TipoAula convertToEntityAttribute(String tipoAula) {
        if (tipoAula == null)
            return null;
        return TipoAula.of(tipoAula);
    }
    
}
