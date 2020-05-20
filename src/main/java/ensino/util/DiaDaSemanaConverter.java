/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.util;

import ensino.util.types.DiaDaSemana;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author santos
 */
@Converter(autoApply = true)
public class DiaDaSemanaConverter implements AttributeConverter<DiaDaSemana, Integer> {

    @Override
    public Integer convertToDatabaseColumn(DiaDaSemana diaDaSemana) {
        if (diaDaSemana == null) {
            return null;
        }
        return diaDaSemana.getValue();
    }

    @Override
    public DiaDaSemana convertToEntityAttribute(Integer diaDaSemana) {
        if (diaDaSemana == null)
            return null;
        return DiaDaSemana.of(diaDaSemana);
    }
    
}
