package it.jump3.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GlobalPojo implements Serializable {

    private static final long serialVersionUID = -4693238633108749767L;

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
}
