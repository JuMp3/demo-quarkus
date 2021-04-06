package it.jump3.controller.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.jump3.enumz.UserStatusEnum;
import it.jump3.security.profile.Role;
import it.jump3.util.DateUtil;
import it.jump3.util.JsonLocalDateTimeDeserializer;
import it.jump3.util.JsonLocalDateTimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
public class UserDto implements Serializable, Comparable<UserDto> {

    private static final long serialVersionUID = 8973007905143119433L;

    @Schema(description = "Username", example = "mrossi", required = true)
    @NotBlank(message = "Username may not be blank")
    private String username;

    @Schema(description = "Plaintext password", required = true)
    @NotBlank(message = "Password may not be blank")
    private String password;

    @Schema(description = "Name user", example = "Mario", required = true)
    @NotBlank(message = "Name may not be blank")
    private String name;

    @Schema(description = "Surname user", example = "Rossi", required = true)
    @NotBlank(message = "Surname may not be blank")
    private String surname;

    @Schema(description = "Email user", example = "mario.rossi@gmail.com")
    private String email;

    @Schema(description = "Status user", example = "A", required = true, implementation = UserStatusEnum.class)
    private UserStatusEnum status;

    @JsonSerialize(using = JsonLocalDateTimeSerializer.class)
    @JsonDeserialize(using = JsonLocalDateTimeDeserializer.class)
    @Schema(description = "Insert time user", type = SchemaType.STRING, format = "date-time", pattern = DateUtil.DATE_PATTERN)
    private LocalDateTime insertTime;

    @Schema(description = "Roles user", example = "USER,ADMIN", required = true, implementation = Role.class, type = SchemaType.ARRAY)
    @NotNull(message = "Roles may not be null")
    private Set<String> roles;

    @Override
    @JsonIgnore
    public int compareTo(UserDto u) {
        return getUsername().compareTo(u.getUsername());
    }
}
