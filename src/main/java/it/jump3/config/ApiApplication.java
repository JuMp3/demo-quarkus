package it.jump3.config;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.core.Application;

@OpenAPIDefinition(
        tags = {
                @Tag(name = "user", description = "Users API"),
                @Tag(name = "config", description = "Configs API")
        },
        info = @Info(
                title = "Demo Quarkus API",
                version = "1.0.0",
                contact = @Contact(
                        name = "Giampiero Poggi",
                        url = "https://www.linkedin.com/in/giampieropoggi/",
                        email = "jmusic@hotmail.it"),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"))
)
public class ApiApplication extends Application {
}
