package it.jump3.config;

import lombok.Getter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.util.Optional;

@ApplicationScoped
@Getter
public class ConfigService {

    @ConfigProperty(name = "jwt.service-token")
    String serviceToken;

    @ConfigProperty(name = "quarkus.log.level")
    String logLevel;

    @ConfigProperty(name = "constant.speed-of-sound-in-meter-per-second", defaultValue = "343")
    int speedOfSound;

    @ConfigProperty(name = "display.mach")
    Optional<Integer> displayMach;

    @ConfigProperty(name = "display.unit.name")
    String displayUnitName;

    @ConfigProperty(name = "display.unit.factor")
    BigDecimal displayUnitFactor;

    @ConfigProperty(name = "jwt.issuer")
    String issuer;

    @ConfigProperty(name = "jwt.secret")
    String secret;

    @ConfigProperty(name = "jwt.expiration.time.minutes")
    Integer expirationTimeInMinutes;

    @ConfigProperty(name = "bitly-token")
    String bitlyToken;
}
