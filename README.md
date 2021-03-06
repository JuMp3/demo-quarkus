# demo-quarkus project 

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_** Environment variables have been configured in the application.yml file.
>In order to run the application, **you need to create the *.env* file** in the current root working directory ([doc](https://www.oreilly.com/library/view/quarkus-cookbook/9781492062646/ch04.html)),
>with the ENV var; example:
>
>URL_DB=hostname:port/postgres <br />
>USERNAME_DB=postgres <br />
>PASSWORD_DB=******** <br />
>SECRET_JWT=***** <br />
>ISSUER_JWT=quarkus-demo <br />
>SERVICE_TOKEN=<service_token> <br />
>BITLY_TOKEN=***** <br />

For tracing function, you have to run in local a Jaeger instance; you can use docker for this purpose:
```shell script
docker run --restart unless-stopped --name jaeger -d -p 5775:5775/udp -p 6831:6831/udp -p 6832:6832/udp -p 5778:5778 -p 16686:16686 -p 14268:14268 jaegertracing/all-in-one:latest
```

For set custom profile, use:
```shell script
./mvnw -Dquarkus.profile=staging compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

### Features implemented
For now, I have implemented the following features:
- Web (RESTful API with Resteasy)
- DB with Panache ORM (used PostgreSQL)
- Fallback (quarkus-smallrye-fault-tolerance)
- Health API
- External config from YML file
- OpenAPI 3.0 with Swagger-UI
- Request validation
- Use of Filters handle (equivalent Spring Aspect)
- Log and tracing with Jaeger
- JWY/custom authentication (with check roles)
- Test (WIP), also for secured API
- REST Client for call 3rd-party API
- Use resteasy-multipart for upload/download file
- Template with i18n messages (Qute)

Coming soon:
- i18n messages

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it???s not an _??ber-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

If you want to build an _??ber-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/demo-quarkus-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.html.

## Related guides

- YAML Configuration ([guide](https://quarkus.io/guides/config#yaml)): Use YAML to configure your Quarkus application

## Provided examples

### YAML Config example

This Supersonic example displays mach speed in your favourite unit, depending on the specified Quarkus configuration.

[Related guide section...](https://quarkus.io/guides/config-reference#configuration-examples)

The Quarkus configuration location is `src/main/resources/application.yml`.

### RESTEasy JSON serialisation using Jackson

This example demonstrate RESTEasy JSON serialisation by letting you list, add and remove quark types from a list. Quarked!

[Related guide section...](https://quarkus.io/guides/rest-json#creating-your-first-json-rest-service)

### Use Servlet

For user Servlet, you must be add the quarkus-undertow dependency

    <dependency>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-undertow</artifactId>
    </dependency>
            
 After, you can use inject servlet object with the @Context annotation, that allows you to inject instances of

    javax.ws.rs.core.HttpHeaders
    javax.ws.rs.core.UriInfo
    javax.ws.rs.core.Request
    javax.servlet.http.HttpServletRequest
    javax.servlet.http.HttpServletResponse
    javax.servlet.ServletConfig
    javax.servlet.ServletContext
    javax.ws.rs.core.SecurityContext