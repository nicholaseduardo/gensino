/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.util;

import ensino.util.types.SituacaoEstudante;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author santos
 */
@Converter(autoApply = true)
public class SituacaoEstudanteConverter implements AttributeConverter<SituacaoEstudante, String> {

    @Override
    public String convertToDatabaseColumn(SituacaoEstudante situacaoEstudante) {
        if (situacaoEstudante == null) {
            return null;
        }
        return situacaoEstudante.getValue();
    }

    @Override
    public SituacaoEstudante convertToEntityAttribute(String situacaoEstudante) {
        if (situacaoEstudante == null)
            return null;
        return SituacaoEstudante.of(situacaoEstudante);
    }
    
}
