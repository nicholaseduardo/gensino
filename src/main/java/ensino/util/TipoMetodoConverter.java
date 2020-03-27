/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.util;

import ensino.util.types.TipoMetodo;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author santos
 */
@Converter(autoApply = true)
public class TipoMetodoConverter implements AttributeConverter<TipoMetodo, Integer> {

    @Override
    public Integer convertToDatabaseColumn(TipoMetodo tipoMetodo) {
        if (tipoMetodo == null) {
            return null;
        }
        return tipoMetodo.getValue();
    }

    @Override
    public TipoMetodo convertToEntityAttribute(Integer tipoMetodo) {
        if (tipoMetodo == null)
            return null;
        return TipoMetodo.of(tipoMetodo);
    }
    
}
