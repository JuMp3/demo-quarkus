package it.jump3.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import it.jump3.security.profile.Role;
import it.jump3.user.UserInfo;

public interface TokenProvider {

    String generateToken(UserInfo userInfo);

    DecodedJWT verify(String token);

    Role[] extractRoles(DecodedJWT decodedJWT);

    UserInfo getUserInfoFromToken(String jwt);
}
