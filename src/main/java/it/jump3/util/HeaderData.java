package it.jump3.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HeaderData {

    private String ipClient;
    private int port;
    private String username;

    @Override
    public String toString() {
        return "{"
                + "\"port\":\"" + port + "\""
                + ",\"ipClient\":\"" + ipClient + "\""
                + ",\"username\":\"" + username + "\""
                + "}";
    }
}
