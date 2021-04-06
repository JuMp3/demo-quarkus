package it.jump3;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import it.jump3.dao.model.User;
import it.jump3.dao.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QuarkusDemoTest {

    @InjectMock
    UserRepository mockUserRepository;

    @Test
    @Order(1)
    public void testUsers() {
        given()
                .when().get("/users/0/10")
                .then()
                .statusCode(Response.Status.UNAUTHORIZED.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body("errorDescription", is("Unauthorized"));
    }

    @Test
    @Order(2)
    public void testDb() {

        User user = new User();
        user.setUsername("test");

        Mockito.when(mockUserRepository.findByUsername(any())).thenReturn(user);
        Assertions.assertEquals("test", mockUserRepository.findByUsername("pippo").getUsername());
    }
}