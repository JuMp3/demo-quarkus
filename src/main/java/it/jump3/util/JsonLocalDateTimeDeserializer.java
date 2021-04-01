package it.jump3.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class JsonLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser jsonparser, DeserializationContext context) throws IOException {
        LocalDateTime localDateTime;
        try {
            localDateTime = DateUtil.getDateTimeFromStringFe(jsonparser.getText());
        } catch (DateTimeParseException | ParseException e) {
            throw new IllegalArgumentException("invalid date format");
        }
        return localDateTime;
    }
}
