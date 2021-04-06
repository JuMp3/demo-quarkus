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
public class UserFe implements Serializable, Comparable<UserFe> {

    private static final long serialVersionUID = 2797899789073844329L;

    private String username;
    private String name;
    private String surname;
    private String email;
    private UserStatusEnum status;

    @JsonSerialize(using = JsonLocalDateTimeSerializer.class)
    @JsonDeserialize(using = JsonLocalDateTimeDeserializer.class)
    private LocalDateTime insertTime;

    @JsonSerialize(using = JsonLocalDateTimeSerializer.class)
    @JsonDeserialize(using = JsonLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    private Set<String> roles;

    @Override
    @JsonIgnore
    public int compareTo(UserFe u) {
        return getUsername().compareTo(u.getUsername());
    }
}
