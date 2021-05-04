package it.jump3.util.qute;

import io.quarkus.qute.TemplateExtension;

@TemplateExtension(namespace = "str")
public class StringExtensions {

    static String format(String fmt, Object... args) {
        return String.format(fmt, args);
    }

    static String reverse(String val) {
        return new StringBuilder(val).reverse().toString();
    }
}
