package it.jump3.i18n;

import io.quarkus.qute.i18n.Message;
import io.quarkus.qute.i18n.MessageBundle;

@MessageBundle(locale = "en")
public interface AppMessages {

    @Message("Hello!")
    String hello();

    @Message("Hello {name}!")
    String hello_name(String name);
}
