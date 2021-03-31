package it.jump3.service;

import io.smallrye.jwt.build.Jwt;
import it.jump3.user.UserInfo;
import it.jump3.util.EnvironmentConstants;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class JwtTokenService {

    @ConfigProperty(name = "jwt.issuer")
    String issuer;

    @ConfigProperty(name = "jwt.secret")
    String secret;

    public String generateToken(UserInfo userInfo) {

        Map<String, Object> headerClaims = new HashMap<>();
        headerClaims.put("typ", "JWT");

        //String jti = UUID.randomUUID().toString();
        Date now = new Date(System.currentTimeMillis());
        Date expirationDate = new Date(now.getTime() + 10000);

        return Jwt.issuer(issuer)
                .upn(userInfo.getUsername())
                .subject(userInfo.getUsername())
                .groups(userInfo.getRoles())
                .claim(EnvironmentConstants.USER_INFO_KEY, userInfo)
                .issuedAt(now.getTime())
                .expiresAt(expirationDate.getTime())
                .signWithSecret(secret);
    }
}
