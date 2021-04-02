package it.jump3.controller;

import io.vertx.core.http.HttpServerRequest;
import it.jump3.annotation.Trace;
import it.jump3.security.TokenProvider;
import it.jump3.security.annotation.Secured;
import it.jump3.security.profile.Role;
import it.jump3.user.UserInfo;
import it.jump3.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.spi.HttpRequest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

@Path("/login")
@RequestScoped
@Slf4j
public class LoginController {

    @Inject
    TokenProvider tokenProvider;

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

    @GET
    @Path("jwt")
    @Produces(MediaType.TEXT_PLAIN)
    public String jwt() {
        return tokenProvider.generateToken(initUserInfo());
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
        return tokenProvider.getUserInfoFromToken(TokenUtils.getToken(token));
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

    private UserInfo initUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setName("Test");
        userInfo.setSurname("Quarkus");
        userInfo.setEmail("test.quarkus@eu.it");
        userInfo.setUsername("tquarkus");
        userInfo.getRoles().add(Role.ADMIN.name());
        return userInfo;
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
