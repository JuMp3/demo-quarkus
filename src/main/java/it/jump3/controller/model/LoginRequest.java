package it.jump3.controller.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequest implements Serializable {

    private static final long serialVersionUID = 7825534622852606744L;

    @Schema(description = "Username", example = "mrossi", required = true)
    @NotBlank(message = "Username may not be blank")
    private String username;

    @Schema(description = "Plaintext password", required = true)
    @NotBlank(message = "Password may not be blank")
    private String password;
}
