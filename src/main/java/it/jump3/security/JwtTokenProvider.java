package it.jump3.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.jump3.security.profile.Role;
import it.jump3.user.UserInfo;
import it.jump3.util.DateUtil;
import it.jump3.util.EnvironmentConstants;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

@ApplicationScoped
public class JwtTokenProvider implements TokenProvider {

    @Inject
    ObjectMapper objectMapper;

    private final String CLAIM_ROLES = "ROLES";
    private final String issuer;
    private final Integer expirationTimeInMinutes;
    private final Algorithm algorithm;
    private final JWTVerifier jwtVerifier;

    public JwtTokenProvider(@ConfigProperty(name = "jwt.issuer") String issuer,
                            @ConfigProperty(name = "jwt.secret") String secret,
                            @ConfigProperty(name = "jwt.expiration.time.minutes") Integer expirationTimeInMinutes) {

        this.issuer = issuer;
        this.expirationTimeInMinutes = expirationTimeInMinutes;
        this.algorithm = Algorithm.HMAC512(secret);
        this.jwtVerifier = JWT.require(algorithm).withIssuer(issuer).build();
    }

    @Override
    public String generateToken(UserInfo userInfo) {

        Map<String, Object> headerClaims = new HashMap<>();
        headerClaims.put("typ", "JWT");

        String jti = UUID.randomUUID().toString();
        Date now = DateUtil.currentTimeInDate();

        Map<String, Object> userInfoDTOMap = objectMapper.convertValue(userInfo, new TypeReference<>() {
        });

        return JWT.create()
                .withHeader(headerClaims)
                .withSubject(userInfo.getUsername())
                .withIssuedAt(now)
                .withExpiresAt(DateUtil.plusMinutes(now, expirationTimeInMinutes))
                .withIssuer(issuer)
                .withJWTId(jti)
                .withClaim(EnvironmentConstants.USER_INFO_KEY, userInfoDTOMap)
                .withArrayClaim(CLAIM_ROLES, toArrayNames(userInfo.getRoles()))
                .sign(algorithm);
    }

    @Override
    public DecodedJWT verify(String token) {
        return jwtVerifier.verify(token);
    }

    @Override
    public Role[] extractRoles(DecodedJWT decodedJWT) {
        Claim claim = decodedJWT.getClaim(CLAIM_ROLES);
        return claim.asArray(Role.class);
    }

    private static String[] toArrayNames(Set<String> roles) {
        String[] rolesArray = new String[roles.size()];
        roles.toArray(rolesArray);
        return rolesArray;
    }

    @Override
    public UserInfo getUserInfoFromToken(String jwt) {
        return verify(jwt).getClaim(EnvironmentConstants.USER_INFO_KEY).as(UserInfo.class);
    }
}
