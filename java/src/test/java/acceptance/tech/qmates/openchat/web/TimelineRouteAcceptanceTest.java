package acceptance.tech.qmates.openchat.web;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimelineRouteAcceptanceTest extends BaseOpenChatRouteAcceptanceTest {

    @Test
    void emptyUserTimeline() throws IOException, InterruptedException {
        String existingUserId = registerAUser();
        HttpRequest request = requestBuilderFor("/users/" + existingUserId + "/timeline").GET().build();

        HttpResponse<String> response = send(request);

        assertEquals(200, response.statusCode());
        assertContentType("application/json", response);
        assertEquals("[]", response.body());
    }

    @Test
    void userPublishAPost() throws IOException, InterruptedException {
        String existingUserId = registerAUser();
        HttpRequest request = requestBuilderFor("/users/" + existingUserId + "/timeline")
            .POST(bodyFor(new HashMap<>() {{
                put("text", "The post's text.");
            }}))
            .build();

        HttpResponse<String> response = send(request);

        assertEquals(201, response.statusCode());
        assertContentType("application/json", response);
        Map<String, Object> responseBody = stringJsonToMap(response.body());
        assertDoesNotThrow(() -> {
            String postId = (String) responseBody.get("postId");
            assertEquals(4, UUID.fromString(postId).version());
        });
        assertEquals(existingUserId, responseBody.get("userId"));
        assertEquals("The post's text.", responseBody.get("text"));
        assertExpectedUTCDateTimeFormat(responseBody.get("dateTime"));
    }

    @Test
    void unexistingUserTimeline() throws IOException, InterruptedException {
        UUID unexistingUUID = UUID.randomUUID();
        HttpRequest request = requestBuilderFor("/users/" + unexistingUUID + "/timeline").GET().build();

        HttpResponse<String> response = send(request);

        assertEquals(404, response.statusCode());
        assertContentType("text/plain;charset=utf-8", response);
        assertEquals("User not found.", response.body());
    }

    @Test
    void invalidUserIdTimeline() throws IOException, InterruptedException {
        HttpRequest request = requestBuilderFor("/users/invalid/timeline").GET().build();

        HttpResponse<String> response = send(request);

        assertEquals(404, response.statusCode());
        assertContentType("text/plain;charset=utf-8", response);
        assertEquals("User not found.", response.body());
    }

}
