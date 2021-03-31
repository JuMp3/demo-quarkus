package it.jump3.controller;

import it.jump3.enumz.BusinessError;
import it.jump3.exception.CommonBusinessException;
import it.jump3.service.JwtAuth0TokenService;
import it.jump3.service.JwtTokenService;
import it.jump3.user.Role;
import it.jump3.user.UserInfo;
import it.jump3.util.EnvironmentConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/login")
@RequestScoped
@Slf4j
public class LoginController {

    @Inject
    JsonWebToken jwt;

    @Inject
    JwtTokenService jwtTokenService;

    @Inject
    JwtAuth0TokenService jwtAuth0TokenService;

    @GET
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
    }

    @GET
    @Path("jwt-auth0")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public String jwtAuth0(@CookieParam("jwt") String jwtCookie) {

        String jwt = jwtCookie;
        if (StringUtils.isEmpty(jwt)) {
            jwt = jwtAuth0TokenService.generateToken(initUserInfo());
        } else {
            UserInfo userInfo = jwtAuth0TokenService.getUserInfoFromToken(jwt);
            log.info(userInfo.toString());
        }

        return jwt;
    }

    private UserInfo initUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setName("Test");
        userInfo.setSurname("Quarkus");
        userInfo.setEmail("test.quarkus@eu.it");
        userInfo.setUsername("tquarkus");
        userInfo.getRoles().add(Role.ADMIN);
        return userInfo;
    }

    @GET
    @Path("user-info")
    @RolesAllowed({Role.ADMIN, Role.USER})
    @Produces(MediaType.TEXT_PLAIN)
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
        return String.format("hello + %s,"
                        + " isHttps: %s,"
                        + " authScheme: %s,"
                        + " hasJWT: %s",
                name, ctx.isSecure(), ctx.getAuthenticationScheme(), hasJwt());
    }

    private boolean hasJwt() {
        return jwt.getClaimNames() != null;
    }
}
