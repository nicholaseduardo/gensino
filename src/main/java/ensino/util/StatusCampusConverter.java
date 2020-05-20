/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.util;

import ensino.util.types.StatusCampus;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author santos
 */
@Converter(autoApply = true)
public class StatusCampusConverter implements AttributeConverter<StatusCampus, String> {

    @Override
    public String convertToDatabaseColumn(StatusCampus statusCampus) {
        if (statusCampus == null) {
            return null;
        }
        return statusCampus.getValue();
    }

    @Override
    public StatusCampus convertToEntityAttribute(String statusCampus) {
        if (statusCampus == null)
            return null;
        return StatusCampus.of(statusCampus);
    }
    
}
