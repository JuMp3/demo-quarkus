package it.jump3.security.context;

import com.auth0.jwt.interfaces.DecodedJWT;
import it.jump3.security.TokenProvider;
import it.jump3.security.profile.Role;
import it.jump3.user.UserInfo;
import it.jump3.util.EnvironmentConstants;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class DecodedJWTSecurityContext implements SecurityContext {

    private final DecodedJWT decodedJWT;
    private final TokenProvider tokenProvider;

    public DecodedJWTSecurityContext(DecodedJWT decodedJWT, TokenProvider tokenProvider) {
        this.decodedJWT = decodedJWT;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Principal getUserPrincipal() {
        return decodedJWT.getClaim(EnvironmentConstants.USER_INFO_KEY).as(UserInfo.class);
    }

    @Override
    public boolean isUserInRole(String role) {
        Role[] tokenRoles = tokenProvider.extractRoles(decodedJWT);
        for (Role tokenRole : tokenRoles) {
            if (role.equals(tokenRole.name())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isSecure() {
        return this.isSecure();
    }

    @Override
    public String getAuthenticationScheme() {
        return this.getAuthenticationScheme();
    }
}
