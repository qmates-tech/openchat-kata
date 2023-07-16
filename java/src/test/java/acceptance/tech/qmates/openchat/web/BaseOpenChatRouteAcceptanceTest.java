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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    protected String registerUser(String username, String password, String userAbout) throws IOException, InterruptedException {
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

    private void resetApplication() throws IOException, InterruptedException {
        this.httpClient.send(requestBuilderFor("/admin").DELETE().build(), HttpResponse.BodyHandlers.discarding());
    }
}
