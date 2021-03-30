package it.jump3.controller;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import it.jump3.controller.model.UserDto;
import it.jump3.controller.model.UserResponse;
import it.jump3.dao.repository.UserRepository;
import it.jump3.enumz.BusinessError;
import it.jump3.exception.CommonBusinessException;
import it.jump3.util.Utility;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

//@ApplicationPath("/api")
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "user")
@Slf4j
public class UserController {

    @Inject
    UserRepository userRepository;

    @Inject
    Validator validator;

    @GET
    @Path("/{page}/{size}")
    public UserResponse list(@PathParam("page") @DefaultValue("0") Integer page,
                             @PathParam("size") @DefaultValue("20") Integer size,
                             @QueryParam("sort") String sortQuery) {

        log.info("**** START -> getUser ****");
        UserResponse users = userRepository.findUsers(Page.of(page, size), Utility.getSortFromQuery(sortQuery));
        log.info("**** END -> getUser ****");

        return users;
    }

    @POST
    public void create(@RequestBody UserDto userDto) throws InvocationTargetException, IllegalAccessException {

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        if (!violations.isEmpty()) {
            throw new CommonBusinessException(Integer.toString(BusinessError.IB_400_USER.code()),
                    violations, Response.Status.BAD_REQUEST);
        }

        log.info("**** START -> create ****");
        userRepository.newUser(userDto);
        log.info("**** END -> create ****");
    }

    @DELETE
    @Path("/{username}")
    public void delete(@PathParam("username") String username) {

        log.info("**** START -> delete ****");
        userRepository.deleteUser(username);
        log.info("**** END -> delete ****");
    }
}
