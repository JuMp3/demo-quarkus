package it.jump3;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import it.jump3.controller.model.UserDto;
import it.jump3.controller.model.UserResponse;
import it.jump3.dao.repository.UserRepository;
import it.jump3.security.TokenProvider;
import it.jump3.security.profile.Role;
import it.jump3.user.UserInfo;
import it.jump3.util.EnvironmentConstants;
import it.jump3.util.Utility;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
public class QuarkusDemoNoSecTest {

    @InjectMock
    UserRepository mockUserRepository;

    @Inject
    TokenProvider tokenProvider;


    @Test
    public void testUsers403() {

        Mockito.when(mockUserRepository.findUsers(any(), any())).thenReturn(getResponse());

        given()
                .headers(HttpHeaders.AUTHORIZATION, generateUserToken())
                .when()
                .get("/users/0/10")
                .then()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }

    @Test
    public void testUsersOk() {

        Mockito.when(mockUserRepository.findUsers(any(), any())).thenReturn(getResponse());

        given()
                .headers(HttpHeaders.AUTHORIZATION, generateAdminToken())
                .when()
                .get("/users/0/10")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body("users.size()", is(1));
    }

    private UserResponse getResponse() {

        Set<UserDto> users = new HashSet<>();
        users.add(new UserDto());

        UserResponse userResponse = new UserResponse();
        userResponse.setUsers(users);
        Utility.setPaginatedResponse(userResponse, 1L, 10);

        return userResponse;
    }

    private String generateAdminToken() {

        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("junit5.admin");
        userInfo.setName("Junit");
        userInfo.setSurname("Admin");
        userInfo.setEmail("junit.admin@gmail.com");
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ADMIN);
        userInfo.setRoles(roles);

        return EnvironmentConstants.AUTHORIZATION_HEADER_PREFIX + tokenProvider.generateToken(userInfo);
    }

    private String generateUserToken() {

        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("junit5.user");
        userInfo.setName("Junit");
        userInfo.setSurname("User");
        userInfo.setEmail("junit.user@gmail.com");
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        userInfo.setRoles(roles);

        return EnvironmentConstants.AUTHORIZATION_HEADER_PREFIX + tokenProvider.generateToken(userInfo);
    }
}