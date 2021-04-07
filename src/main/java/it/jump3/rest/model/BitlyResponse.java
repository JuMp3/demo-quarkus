package it.jump3.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BitlyResponse implements Serializable {

    private static final long serialVersionUID = 2219212173579751419L;

    private String id;
    private String link;

    @JsonProperty("long_url")
    private String longUrl;
}
