/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author nicho
 */
public class DateHelper {
    public static String dateToString(Date data,
            String format) {
        if (data != null) {
            SimpleDateFormat simple = new SimpleDateFormat(format);
            return simple.format(data);
        }
        return null;
    }
    
    public static Date stringToDate(String data, String format) throws ParseException {
        if (!"".equals(data) && !"  /  /    ".equals(data)) {
            SimpleDateFormat simple = new SimpleDateFormat(format);
            return simple.parse(data);
        }
        return null;
    }

    public static Calendar stringToCalendar(String data, String format) throws ParseException {
        if (!"".equals(data)) {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simple = new SimpleDateFormat(format);
            calendar.setTime(simple.parse(data));
            return calendar;
        }
        return null;
    }

    public static Calendar dateToCalendar(Date data) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        return calendar;
    }
}
