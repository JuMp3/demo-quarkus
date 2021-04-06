package it.jump3.util;

import it.jump3.enumz.BusinessError;
import it.jump3.exception.CommonBusinessException;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class TokenUtils {

    /**
     * Read a PEM encoded private key from the classpath
     *
     * @param pemResName
     * @return
     * @throws IOException
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static PrivateKey readPrivateKey(final String pemResName) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        try (InputStream contentIS = TokenUtils.class.getResourceAsStream(pemResName)) {
            byte[] tmp = new byte[4096];
            int length = contentIS.read(tmp);
            return decodePrivateKey(new String(tmp, 0, length, StandardCharsets.UTF_8));
        }
    }

    /**
     * Decode a PEM encoded private key string to an RSA PrivateKey
     *
     * @param pemEncoded
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PrivateKey decodePrivateKey(final String pemEncoded) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] encodedBytes = toEncodedBytes(pemEncoded);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(keySpec);
    }

    public static byte[] toEncodedBytes(final String pemEncoded) {
        final String normalizedPem = removeBeginEnd(pemEncoded);
        return Base64.getDecoder().decode(normalizedPem);
    }

    public static String removeBeginEnd(String pem) {
        pem = pem.replaceAll("-----BEGIN (.*)-----", "");
        pem = pem.replaceAll("-----END (.*)----", "");
        pem = pem.replaceAll("\r\n", "");
        pem = pem.replaceAll("\n", "");
        return pem.trim();
    }

    public static String getToken(String token) {
        if (StringUtils.isEmpty(token)) {
            throw new CommonBusinessException(Integer.toString(BusinessError.IB_400_EMPTY_TOKEN.code()),
                    "Missing token", Response.Status.BAD_REQUEST);
        }
        return token.replace(EnvironmentConstants.AUTHORIZATION_HEADER_PREFIX, "");
    }
}
