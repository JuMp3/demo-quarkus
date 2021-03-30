package it.jump3.controller.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.jump3.enumz.UserStatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserDto implements Serializable, Comparable<UserDto> {

    private static final long serialVersionUID = -3736108121454130512L;

    @NotBlank(message = "Username may not be blank")
    private String username;

    @NotBlank(message = "Name may not be blank")
    private String name;

    @NotBlank(message = "Surname may not be blank")
    private String surname;

    private UserStatusEnum status;
    private LocalDateTime insertTime;

    @Override
    @JsonIgnore
    public int compareTo(UserDto u) {
        return getUsername().compareTo(u.getSurname());
    }
}
