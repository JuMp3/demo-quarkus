package it.jump3.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.jump3.util.JsonLocalDateTimeDeserializer;
import it.jump3.util.JsonLocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CovidResponse implements Serializable {

    private static final long serialVersionUID = -1068614460108006110L;

    @JsonProperty("Global")
    private GlobalPojo global;

    @JsonProperty("Countries")
    private List<CountryPojo> countries;

    @JsonSerialize(using = JsonLocalDateTimeSerializer.class)
    @JsonDeserialize(using = JsonLocalDateTimeDeserializer.class)
    @JsonProperty("Date")
    private LocalDateTime date;
}
