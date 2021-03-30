package it.jump3.controller.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class UserResponse extends PaginatedResponse implements Serializable {

    private static final long serialVersionUID = -6189389384500272650L;

    private Set<UserDto> users;
}
