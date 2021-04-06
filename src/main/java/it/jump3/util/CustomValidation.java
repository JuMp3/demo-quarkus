package it.jump3.util;

import it.jump3.controller.model.UserDto;
import it.jump3.enumz.BusinessError;
import it.jump3.exception.CommonBusinessException;
import it.jump3.security.profile.Role;
import org.apache.commons.collections.CollectionUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class CustomValidation {

    public void customCheck(UserDto userDto) {

        if (CollectionUtils.isEmpty(userDto.getRoles())) {
            throw new CommonBusinessException(Integer.toString(BusinessError.IB_400_ROLE.code()),
                    "Roles may not be empty", Response.Status.BAD_REQUEST);
        }

        for (String role : userDto.getRoles()) {
            try {
                Role.valueOf(role);
            } catch (IllegalArgumentException e) {
                throw new CommonBusinessException(Integer.toString(BusinessError.IB_400_ROLE.code()),
                        String.format("Wrong role %s", role), Response.Status.BAD_REQUEST);
            }
        }
    }
}
