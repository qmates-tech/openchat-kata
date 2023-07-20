package acceptance.tech.qmates.openchat.web;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimelineRouteAcceptanceTest extends BaseOpenChatRouteAcceptanceTest {

    @Test
    void unexistingUserTimeline() throws IOException, InterruptedException {
        UUID unexistingUUID = UUID.randomUUID();
        HttpRequest request = requestBuilderFor("/users/" + unexistingUUID + "/timeline").GET().build();

        HttpResponse<String> response = send(request);

        assertEquals(404, response.statusCode());
        assertEquals("text/plain;charset=utf-8", response.headers().firstValue("Content-Type").get());
        assertEquals("User not found.", response.body());
    }

    @Test
    void emptyUserTimeline() throws IOException, InterruptedException {
        String aliceUUID = registerUser("alice90", "pass1234", "About alice user.");
        HttpRequest request = requestBuilderFor("/users/" + aliceUUID + "/timeline").GET().build();

        HttpResponse<String> response = send(request);

        assertEquals(200, response.statusCode());
        assertEquals("application/json", response.headers().firstValue("Content-Type").get());
        assertEquals("[]", response.body());
    }

    @Test
    void timelineForInvalidUserId() throws IOException, InterruptedException {
        HttpRequest request = requestBuilderFor("/users/invalid/timeline").GET().build();

        HttpResponse<String> response = send(request);

        assertEquals(404, response.statusCode());
        assertEquals("text/plain;charset=utf-8", response.headers().firstValue("Content-Type").get());
        assertEquals("User not found.", response.body());
    }

}
