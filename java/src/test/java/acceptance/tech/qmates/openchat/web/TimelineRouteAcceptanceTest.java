package acceptance.tech.qmates.openchat.web;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimelineRouteAcceptanceTest extends BaseOpenChatRouteAcceptanceTest {

    @Test
    void unexistingUserTimeline() throws IOException, InterruptedException {
        HttpRequest request = requestBuilderFor("/users/unexisting/timeline").GET().build();

        HttpResponse<String> response = send(request);

        assertEquals(404, response.statusCode());
        assertEquals("text/plain;charset=utf-8", response.headers().firstValue("Content-Type").get());
        assertEquals("User not found.", response.body());
    }

}
