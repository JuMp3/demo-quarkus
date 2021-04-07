package it.jump3.controller;

import io.vertx.core.http.HttpServerRequest;
import it.jump3.annotation.Trace;
import it.jump3.controller.model.GenericResponse;
import it.jump3.controller.model.LoginRequest;
import it.jump3.security.annotation.Secured;
import it.jump3.security.profile.Role;
import it.jump3.service.LoginService;
import it.jump3.user.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.jboss.resteasy.spi.HttpRequest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.lang.reflect.InvocationTargetException;

@Path("/login")
@RequestScoped
@Slf4j
public class LoginController {

    @Inject
    LoginService loginService;

    /*@GET
    @Path("hi")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public String hello(@Context SecurityContext ctx) {
        return getResponseString(ctx);
    }

    @GET
    @Path("jwt")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public String jwt(@CookieParam("jwt") String jwtCookie) {

        String jwt = jwtCookie;
        if (StringUtils.isEmpty(jwt)) {
            jwt = jwtTokenService.generateToken(initUserInfo());
        } else {
            UserInfo userInfo = this.jwt.getClaim(EnvironmentConstants.USER_INFO_KEY);
            log.info(userInfo.toString());
        }

        return jwt;
    }*/

    @Trace
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(@RequestBody @Valid LoginRequest loginRequest) throws InvocationTargetException, IllegalAccessException {

        log.info("**** START -> login ****");
        String token = loginService.login(loginRequest);
        log.info("**** END -> login ****");

        return Response.ok(new GenericResponse(token)).build();
    }

    @Trace
    @GET
    @Path("user-info")
    @Secured({Role.EVERYONE})
    @Produces(MediaType.APPLICATION_JSON)
    public UserInfo getUser(@HeaderParam(HttpHeaders.AUTHORIZATION) String token,
                            @Context SecurityContext securityContext,
                            @Context HttpRequest httpRequest,
                            @Context HttpServerRequest httpServerRequest) {
        return (UserInfo) securityContext.getUserPrincipal();
        //return loginService.getUserInfoFromToken(token);
    }

    @GET
    @Path("user")
    @Secured({Role.USER})
    @Produces(MediaType.APPLICATION_JSON)
    public UserInfo getUser(@Context SecurityContext securityContext) {
        return (UserInfo) securityContext.getUserPrincipal();
    }

    @GET
    @Path("admin")
    @Secured({Role.ADMIN})
    @Produces(MediaType.APPLICATION_JSON)
    public UserInfo getAdmin(@Context SecurityContext securityContext) {
        return (UserInfo) securityContext.getUserPrincipal();
    }

    /*@GET
    @Path("user-info")
    @RolesAllowed({Role.ADMIN, Role.USER})
    @Produces(MediaType.APPLICATION_JSON)
    public UserInfo helloRolesAllowed(@Context SecurityContext ctx) {
        log.info(getResponseString(ctx));
        return jwt.getClaim(EnvironmentConstants.USER_INFO_KEY);
    }

    private String getResponseString(SecurityContext ctx) {
        String name;
        if (ctx.getUserPrincipal() == null) {
            name = "anonymous";
        } else if (!ctx.getUserPrincipal().getName().equals(jwt.getName())) {
            throw new CommonBusinessException(Integer.toString(BusinessError.IB_500_JWT.code()),
                    "Principal and JsonWebToken names do not match", Response.Status.INTERNAL_SERVER_ERROR);
        } else {
            name = ctx.getUserPrincipal().getName();
        }
        return String.format("hello_ %s,"
                        + " isHttps: %s,"
                        + " authScheme: %s,"
                        + " hasJWT: %s",
                name, ctx.isSecure(), ctx.getAuthenticationScheme(), hasJwt());
    }

    private boolean hasJwt() {
        return jwt.getClaimNames() != null;
    }*/
}
