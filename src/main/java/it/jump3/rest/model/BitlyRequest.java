package it.jump3.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BitlyRequest implements Serializable {

    private static final long serialVersionUID = -2584517140887044250L;

    @JsonProperty("long_url")
    private String longUrl;
}
