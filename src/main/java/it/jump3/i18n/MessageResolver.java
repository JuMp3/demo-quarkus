package it.jump3.i18n;

import io.quarkus.qute.i18n.Localized;

import javax.inject.Singleton;
import java.util.Locale;

@Singleton
public class MessageResolver {

    @Localized("it")
    AppMessages itAppMessages;

    @Localized("es")
    AppMessages esAppMessages;

    @Localized("en")
    AppMessages appMessages;

    public String hello(Locale locale) {
        if (locale.getLanguage().startsWith(Locale.ITALY.getLanguage())) {
            return itAppMessages.hello();
        } else if (locale.getLanguage().startsWith("es")) {
            return esAppMessages.hello();
        }
        return appMessages.hello();
    }

    public String helloName(String name, Locale locale) {
        if (locale.getLanguage().startsWith(Locale.ITALY.getLanguage())) {
            return itAppMessages.hello_name(name);
        } else if (locale.getLanguage().startsWith("es")) {
            return esAppMessages.hello_name(name);
        }
        return appMessages.hello_name(name);
    }
}
