//package it.jump3.service;
//
//import io.smallrye.jwt.build.Jwt;
//import it.jump3.user.UserInfo;
//import it.jump3.util.DateUtil;
//import it.jump3.util.EnvironmentConstants;
//import org.eclipse.microprofile.config.inject.ConfigProperty;
//
//import javax.enterprise.context.ApplicationScoped;
//import java.util.Date;
//
//@ApplicationScoped
//public class JwtTokenService {
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
//    public String generateToken(UserInfo userInfo) {
//
//        Date now = DateUtil.currentTimeInDate();
//
//        return Jwt.issuer(issuer)
//                .upn(userInfo.getUsername())
//                .subject(userInfo.getUsername())
//                .groups(userInfo.getRoles())
//                .claim(EnvironmentConstants.USER_INFO_KEY, userInfo)
//                .issuedAt(now.toInstant())
//                .expiresAt(DateUtil.plusMinutes(now, expirationTimeInMinutes).toInstant())
//                .sign();
//
//        /*
//        String privateKeyLocation = "/privatekey.pem";
//        PrivateKey privateKey = TokenUtils.readPrivateKey(privateKeyLocation);
//
//        JwtClaimsBuilder claimsBuilder = Jwt.claims();
//        Date now = DateUtil.currentTimeInDate();
//
//        claimsBuilder.issuer(issuer);
//        claimsBuilder.subject(userInfo.getUsername());
//        claimsBuilder.upn(userInfo.getUsername());
//        claimsBuilder.issuedAt(now.toInstant());
//        claimsBuilder.expiresAt(DateUtil.plusMinutes(now, expirationTimeInMinutes).toInstant());
//        claimsBuilder.groups(userInfo.getRoles());
//        claimsBuilder.claim(EnvironmentConstants.USER_INFO_KEY, userInfo);
//
//        return claimsBuilder.sign(privateKey);
//         */
//    }
//}
