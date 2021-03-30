package it.jump3.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;

public class JsonLocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

    @Override
    public void serialize(LocalDateTime localDateTime, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(DateUtil.getFromLocalDateTimeDefault(localDateTime));
    }
}
