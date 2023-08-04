package acceptance.tech.qmates.openchat.web;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UsersRouteAcceptanceTest extends BaseOpenChatRouteAcceptanceTest {

    @Test
    void retrieveEmptyUserListWithNoRegisteredUser() throws IOException, InterruptedException {
        HttpRequest request = requestBuilderFor("/users").GET().build();

        HttpResponse<String> response = send(request);

        assertEquals(200, response.statusCode());
        String contentType = "application/json";
        assertContentType(contentType, response);
        assertEquals("[]", response.body());
    }

    @Test
    void registerSomeUsersAndRetrieveThem() throws IOException, InterruptedException {
        HttpRequest request = requestBuilderFor("/users")
            .POST(bodyFor(Map.of(
                "username", "alice90",
                "password", "pass1234",
                "about", "About alice user."
            )))
            .build();

        HttpResponse<String> response = send(request);

        assertEquals(201, response.statusCode());
        assertContentType("application/json", response);
        Map<String, Object> responseBody = stringJsonToMap(response.body());
        assertEquals("alice90", responseBody.get("username"));
        assertEquals("About alice user.", responseBody.get("about"));
        String aliceUUID = (String) responseBody.get("id");
        assertValidUUIDV4(aliceUUID);

        // ========================================= register some other users

        String johnUUID = registerUser("john91", "pass4321", "About john user.");
        String martinUUID = registerUser("martin85", "pass$$", "About martin user.");

        // ========================================= retrieve registered users

        HttpRequest retrieveUsersRequest = requestBuilderFor("/users").GET().build();
        HttpResponse<String> retrieveUsersResponse = send(retrieveUsersRequest);

        assertEquals(200, retrieveUsersResponse.statusCode());
        assertContentType("application/json", retrieveUsersResponse);
        List<Map<String, Object>> retrieveUsersResponseBody = stringJsonArrayToList(retrieveUsersResponse.body());
        assertEquals(3, retrieveUsersResponseBody.size());
        assertThat(retrieveUsersResponseBody).anySatisfy(userMap -> {
            assertEquals(aliceUUID, userMap.get("id"));
            assertEquals("alice90", userMap.get("username"));
            assertEquals("About alice user.", userMap.get("about"));
        });
        assertThat(retrieveUsersResponseBody).anySatisfy(userMap -> {
            assertEquals(johnUUID, userMap.get("id"));
            assertEquals("john91", userMap.get("username"));
            assertEquals("About john user.", userMap.get("about"));
        });
        assertThat(retrieveUsersResponseBody).anySatisfy(userMap -> {
            assertEquals(martinUUID, userMap.get("id"));
            assertEquals("martin85", userMap.get("username"));
            assertEquals("About martin user.", userMap.get("about"));
        });
    }

    @Test
    void usernameAlreadyInUse() throws IOException, InterruptedException {
        registerUser("bob89", "any", "any");

        HttpResponse<String> secondRegistrationResponse = send(requestBuilderFor("/users")
            .POST(bodyFor(Map.of(
                "username", "bob89",
                "password", "pass123",
                "about", "Another about."
            ))).build());
        assertEquals(400, secondRegistrationResponse.statusCode());
        assertContentType("text/plain;charset=utf-8", secondRegistrationResponse);
        assertEquals("Username already in use.", secondRegistrationResponse.body());
    }

}
