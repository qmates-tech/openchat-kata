package acceptance.tech.qmates.openchat.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class BaseOpenChatRouteAcceptanceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(5))
        .build();

    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        resetApplication();
    }

    protected String registerAUser() throws IOException, InterruptedException {
        return registerUser("any");
    }

    protected String registerUser(String username) throws IOException, InterruptedException {
        return registerUser(username, "any", "any");
    }

    protected String registerUser(String username, String password, String userAbout) throws IOException, InterruptedException {
        HttpResponse<String> response = send(requestBuilderFor("/users")
            .POST(bodyFor(Map.of(
                "username", username,
                "password", password,
                "about", userAbout
            ))).build()
        );
        assertEquals(201, response.statusCode());
        Map<String, Object> responseBody = stringJsonToMap(response.body());
        return (String) responseBody.get("id");
    }

    protected String submitPost(String authorUserId, String postText) throws IOException, InterruptedException {
        HttpResponse<String> response = send(
            requestBuilderFor("/users/" + authorUserId + "/timeline")
                .POST(bodyFor(Map.of(
                    "text", postText
                ))).build()
        );

        assertEquals(201, response.statusCode());
        Map<String, Object> responseBody = stringJsonToMap(response.body());
        return (String) responseBody.get("postId");
    }

    protected HttpRequest.BodyPublisher bodyFor(Object requestBody) throws JsonProcessingException {
        return HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(requestBody));
    }

    protected static HttpRequest.Builder requestBuilderFor(String route) {
        return HttpRequest.newBuilder().uri(URI.create("http://localhost:8000" + route));
    }

    protected HttpResponse<String> send(HttpRequest request) throws IOException, InterruptedException {
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    //@formatter:off
    protected Map<String, Object> stringJsonToMap(String body) throws IOException {
        TypeReference<Map<String, Object>> targetType = new TypeReference<>() { };
        return objectMapper.readValue(body, targetType);
    }

    protected List<Map<String, Object>> stringJsonArrayToList(String body) throws JsonProcessingException {
        TypeReference<List<Map<String, Object>>> targetType = new TypeReference<>() { };
        return objectMapper.readValue(body, targetType);
    }
    //@formatter:on

    void assertContentType(String expected, HttpResponse<String> response) {
        assertEquals(expected, response.headers().firstValue("Content-Type").get());
    }

    void assertValidUUIDV4(Object value) {
        assertDoesNotThrow(
            () -> assertEquals(4, UUID.fromString(value.toString()).version()),
            "Provided string is not a valid v4 uuid ! -> " + value
        );
    }

    void assertExpectedUTCDateTimeFormat(Object dateTimeString) {
        // 2018-01-10T11:30:00Z (iso format but without milliseconds)
        assertThat(dateTimeString.toString()).matches(
            "^((19|20)[0-9][0-9])[-]" +
                "(0[1-9]|1[012])[-]" +
                "(0[1-9]|[12][0-9]|3[01])" +
                "[T]" +
                "([01][0-9]|[2][0-3])[:]" +
                "([0-5][0-9])[:]" +
                "([0-5][0-9])" +
                "Z$"
        );
    }

    private void resetApplication() throws IOException, InterruptedException {
        this.httpClient.send(requestBuilderFor("/admin").DELETE().build(), HttpResponse.BodyHandlers.discarding());
    }
}
