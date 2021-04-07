package it.jump3.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.jump3.util.JsonLocalDateTimeDeserializer;
import it.jump3.util.JsonLocalDateTimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryPojo implements Serializable {

    private static final long serialVersionUID = -483488733429017861L;

    @JsonProperty("Country")
    private String country;

    @JsonProperty("CountryCode")
    private String countryCode;

    @JsonProperty("Slug")
    private String slug;

    @JsonProperty("NewConfirmed")
    private Integer newConfirmed;

    @JsonProperty("TotalConfirmed")
    private Integer totalConfirmed;

    @JsonProperty("NewDeaths")
    private Integer newDeaths;

    @JsonProperty("TotalDeaths")
    private Integer totalDeaths;

    @JsonProperty("NewRecovered")
    private Integer newRecovered;

    @JsonProperty("TotalRecovered")
    private Integer totalRecovered;

    @JsonSerialize(using = JsonLocalDateTimeSerializer.class)
    @JsonDeserialize(using = JsonLocalDateTimeDeserializer.class)
    @JsonProperty("Date")
    private LocalDateTime date;
}
