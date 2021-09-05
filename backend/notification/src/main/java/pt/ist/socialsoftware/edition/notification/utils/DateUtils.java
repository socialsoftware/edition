package pt.ist.socialsoftware.edition.notification.utils;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static LocalDate convertDate(String dateString) {
        Date date = null;
        if (dateString.equals("")) {
            return null;
        } else if (dateString.length() == 4) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
            try {
                date = dateFormat.parse(dateString);
            } catch (ParseException e) {
                throw new LdoDLoadException(
                        "A data não está no formato correto: " + dateString);
            }
            return new LocalDate(new DateTime(date));
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                date = dateFormat.parse(dateString);
            } catch (ParseException e) {
                throw new LdoDLoadException(
                        "A data não está no formato correto: " + dateString);
            }
            return new LocalDate(new DateTime(date));
        }
    }
}
