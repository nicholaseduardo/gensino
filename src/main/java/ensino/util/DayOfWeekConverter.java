/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.util;

import java.time.DayOfWeek;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author santos
 */
@Converter(autoApply = true)
public class DayOfWeekConverter implements AttributeConverter<DayOfWeek, Integer> {

    @Override
    public Integer convertToDatabaseColumn(DayOfWeek dayOfWeek) {
        if (dayOfWeek == null) {
            return null;
        }
        return dayOfWeek.getValue();
    }

    @Override
    public DayOfWeek convertToEntityAttribute(Integer dayOfWeek) {
        if (dayOfWeek == null)
            return null;
        return DayOfWeek.of(dayOfWeek);
    }
    
}
