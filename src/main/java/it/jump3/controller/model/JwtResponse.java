package it.jump3.controller.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import it.jump3.user.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RegisterForReflection
public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -7819251097115316340L;

    private UserInfo userInfo;
    private String jwt;
}
