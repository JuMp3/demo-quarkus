package it.jump3.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class BitlyErrorResponse implements Serializable {

    private static final long serialVersionUID = -1845934983937417001L;

    private String message;
    private String description;
    private List<BitlyError> errors;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @AllArgsConstructor
    private static class BitlyError implements Serializable {

        private static final long serialVersionUID = 2756362279351325792L;

        private String field;

        @JsonProperty("error_code")
        private String errorCode;
    }
}
