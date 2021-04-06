package it.jump3.controller;

import io.quarkus.panache.common.Page;
import it.jump3.annotation.Trace;
import it.jump3.controller.model.UserDto;
import it.jump3.controller.model.UserFe;
import it.jump3.controller.model.UserResponse;
import it.jump3.dao.model.User;
import it.jump3.security.annotation.CheckServiceToken;
import it.jump3.security.annotation.Secured;
import it.jump3.security.profile.Role;
import it.jump3.service.UserService;
import it.jump3.util.CustomValidation;
import it.jump3.util.Utility;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.lang.reflect.InvocationTargetException;

//@ApplicationPath("/api")
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "user")
@Slf4j
public class UserController {

    @Inject
    UserService userService;

    @Inject
    CustomValidation customValidation;

    //@Inject
    //Validator validator;

    @GET
    @Secured({Role.EVERYONE})
    @Path("/{username}")
    public Response user(@PathParam("username") @Valid @NotBlank(message = "Username may not be blank") String username)
            throws InvocationTargetException, IllegalAccessException {

        log.info("**** START -> getUser ****");
        UserFe user = userService.getUser(username);
        log.info("**** END -> getUser ****");

        return Response.ok(user).build();
    }

    @GET
    @Secured({Role.ADMIN})
    @Path("/{page}/{size}")
    public Response list(@PathParam("page") @DefaultValue("0") Integer page,
                         @PathParam("size") @DefaultValue("20") Integer size,
                         @QueryParam("sort") String sortQuery) {

        log.info("**** START -> list ****");
        UserResponse users = userService.findUsers(Page.of(page, size), Utility.getSortFromQuery(sortQuery));
        log.info("**** END -> list ****");

        return CollectionUtils.isEmpty(users.getUsers()) ? Response.noContent().build() : Response.ok(users).build();
    }

    @Trace
    @CheckServiceToken
    @POST
    public Response create(@RequestBody @Valid UserDto userDto,
                           @Context UriInfo uriInfo) throws InvocationTargetException, IllegalAccessException {

        /*Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        if (!violations.isEmpty()) {
            throw new CommonBusinessException(Integer.toString(BusinessError.IB_400_USER.code()),
                    violations, Response.Status.BAD_REQUEST);
        }*/

        log.info("**** START -> create ****");
        customValidation.customCheck(userDto);
        User user = userService.newUser(userDto);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(user.getUsername());
        log.info("**** END -> create ****");

        return Response.created(builder.build()).build();
    }

    @Trace
    @DELETE
    @Secured({Role.ADMIN})
    @Path("/{username}")
    public Response delete(@PathParam("username") String username) {

        log.info("**** START -> delete ****");
        userService.deleteUser(username);
        log.info("**** END -> delete ****");

        return Response.noContent().build();
    }
}
