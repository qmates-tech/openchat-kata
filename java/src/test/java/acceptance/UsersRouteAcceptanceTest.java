package acceptance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UsersRouteAcceptanceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(5))
        .build();

    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        resetApplication();
    }

    @Test
    void retrieveEmptyUserListWithNoRegisteredUser() throws IOException, InterruptedException {
        HttpRequest request = requestBuilderFor("/users").GET().build();

        HttpResponse<String> response = send(request);

        assertEquals(200, response.statusCode());
        assertEquals("application/json", response.headers().firstValue("Content-Type").get());
        assertEquals("[]", response.body());
    }

    @Test
    void registerSomeUsersAndRetrieveThem() throws IOException, InterruptedException {
        HttpRequest request = requestBuilderFor("/users")
            .POST(bodyFor(new HashMap<>() {{
                put("username", "alice90");
                put("password", "pass1234");
                put("about", "About alice user.");
            }}))
            .build();

        HttpResponse<String> response = send(request);

        assertEquals(201, response.statusCode());
        assertEquals("application/json", response.headers().firstValue("Content-Type").get());
        Map<String, Object> responseBody = stringJsonToMap(response.body());
        assertEquals("alice90", responseBody.get("username"));
        assertEquals("About alice user.", responseBody.get("about"));
        String aliceUUID = (String) responseBody.get("id");
        assertDoesNotThrow(() ->
            assertEquals(4, UUID.fromString(aliceUUID).version())
        );

        // ========================================= register some other users

        String johnUUID = registerUser("john91", "pass4321", "About john user.");
        String martinUUID = registerUser("martin85", "pass$$", "About martin user.");

        // ========================================= retrieve registered users

        HttpRequest retrieveUsersRequest = requestBuilderFor("/users").GET().build();
        HttpResponse<String> retrieveUsersResponse = send(retrieveUsersRequest);

        assertEquals(200, retrieveUsersResponse.statusCode());
        assertEquals("application/json", retrieveUsersResponse.headers().firstValue("Content-Type").get());
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
            .POST(bodyFor(new HashMap<>() {{
                put("username", "bob89");
                put("password", "pass123");
                put("about", "Another about.");
            }})).build());
        assertEquals(400, secondRegistrationResponse.statusCode());
        assertEquals("text/plain;charset=utf-8", secondRegistrationResponse.headers().firstValue("Content-Type").get());
        assertEquals("Username already in use.", secondRegistrationResponse.body());
    }

    // ***** TODO move function below in some Acceptance Test super class ******

    private String registerUser(String username, String password, String userAbout) throws IOException, InterruptedException {
        HttpResponse<String> response = send(requestBuilderFor("/users")
            .POST(bodyFor(new HashMap<>() {{
                put("username", username);
                put("password", password);
                put("about", userAbout);
            }})).build()
        );
        assertEquals(201, response.statusCode());
        Map<String, Object> responseBody = stringJsonToMap(response.body());
        return (String) responseBody.get("id");
    }

    private void resetApplication() throws IOException, InterruptedException {
        this.httpClient.send(requestBuilderFor("/admin").DELETE().build(), HttpResponse.BodyHandlers.discarding());
    }

    private HttpRequest.BodyPublisher bodyFor(Object requestBody) throws JsonProcessingException {
        return HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(requestBody));
    }

    private static HttpRequest.Builder requestBuilderFor(String route) {
        return HttpRequest.newBuilder().uri(URI.create("http://localhost:8000" + route));
    }

    private HttpResponse<String> send(HttpRequest request) throws IOException, InterruptedException {
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    //@formatter:off
    private Map<String, Object> stringJsonToMap(String body) throws IOException {
        TypeReference<Map<String, Object>> targetType = new TypeReference<>() { };
        return objectMapper.readValue(body, targetType);
    }

    private List<Map<String, Object>> stringJsonArrayToList(String body) throws JsonProcessingException {
        TypeReference<List<Map<String, Object>>> targetType = new TypeReference<>() { };
        return objectMapper.readValue(body, targetType);
    }
    //@formatter:on

}
