package it.jump3.enumz;

import java.util.Arrays;
import java.util.List;

public class EnvironmentConstants {

    public static class Headers {
        public static final List<String> IP_HEADERS = Arrays.asList("X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR");
        public static final String HEADER_USERNAME = "X-USERNAME";
        public static final String SOURCE_SYSTEM_HEADER = "X-SOURCESYSTEM";
    }
}