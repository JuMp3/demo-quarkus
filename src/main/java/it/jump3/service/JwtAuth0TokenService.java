//package it.jump3.service;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import it.jump3.user.UserInfo;
//import it.jump3.util.DateUtil;
//import it.jump3.util.EnvironmentConstants;
//import org.eclipse.microprofile.config.inject.ConfigProperty;
//
//import javax.enterprise.context.ApplicationScoped;
//import javax.inject.Inject;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//@ApplicationScoped
//public class JwtAuth0TokenService {
//
//    @ConfigProperty(name = "jwt.issuer")
//    String issuer;
//
//    @ConfigProperty(name = "jwt.secret")
//    String secret;
//
//    @ConfigProperty(name = "jwt.expiration.time.minutes")
//    Integer expirationTimeInMinutes;
//
//    @Inject
//    ObjectMapper objectMapper;
//
//    public String generateToken(UserInfo userInfo) {
//
//        Map<String, Object> headerClaims = new HashMap<>();
//        headerClaims.put("typ", "JWT");
//
//        String jti = UUID.randomUUID().toString();
//        Date now = DateUtil.currentTimeInDate();
//
//        Map<String, Object> userInfoDTOMap = objectMapper.convertValue(userInfo, new TypeReference<>() {
//        });
//
//        return JWT.create()
//                .withHeader(headerClaims)
//                .withSubject(userInfo.getUsername())
//                .withIssuedAt(now)
//                .withExpiresAt(DateUtil.plusMinutes(now, expirationTimeInMinutes))
//                .withIssuer(issuer)
//                .withJWTId(jti)
//                .withClaim(EnvironmentConstants.USER_INFO_KEY, userInfoDTOMap)
//                .sign(Algorithm.HMAC512(secret));
//    }
//
//    private DecodedJWT getDecodedJWTFromToken(String jwt) {
//        //return JWT.require(Algorithm.HMAC512(Base64.getDecoder().decode(secret)))
//        return JWT.require(Algorithm.HMAC512(secret))
//                .build()
//                .verify(jwt);
//    }
//
//    public UserInfo getUserInfoFromToken(String jwt) {
//        return getDecodedJWTFromToken(jwt).getClaim(EnvironmentConstants.USER_INFO_KEY).as(UserInfo.class);
//    }
//
//    public Date getExpirationDateFromToken(String jwt) {
//        return getDecodedJWTFromToken(jwt).getExpiresAt();
//    }
//
//    public Boolean validateToken(String jwt) {
//        return !isTokenExpiredFromJwt(jwt);
//    }
//
//    private Boolean isTokenExpiredFromJwt(String jwt) {
//        final Date expiration = getExpirationDateFromToken(jwt);
//        return expiration != null && expiration.before(new Date());
//    }
//}
