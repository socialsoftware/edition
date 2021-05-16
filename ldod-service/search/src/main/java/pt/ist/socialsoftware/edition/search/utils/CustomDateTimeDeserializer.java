package pt.ist.socialsoftware.edition.search.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;

public class CustomDateTimeDeserializer extends StdDeserializer<LocalDate> {

    private static final long serialVersionUID = 1L;
    private static DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");

    public CustomDateTimeDeserializer() {
        this(null);
    }

    public CustomDateTimeDeserializer(Class<LocalDate> t) {
        super(t);
    }

    @Override
    public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {

        String date = parser.getText();
        System.out.println(date);
        System.out.println(format);

        return format.parseLocalDate(date);

    }

}
