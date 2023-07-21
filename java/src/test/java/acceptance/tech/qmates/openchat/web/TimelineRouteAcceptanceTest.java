package acceptance.tech.qmates.openchat.web;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
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
    @Disabled("WIP")
    void submitSomePostsAndGetTimelinePostsInDescendingOrder() throws IOException, InterruptedException {
        String existingUserId = registerAUser();
        String firstPostId = submitPost(existingUserId, "The first post.");
        String secondPostId = submitPost(existingUserId, "The second post.");
        String thirdPostId = submitPost(existingUserId, "The third post.");

        HttpRequest request = requestBuilderFor("/users/" + existingUserId + "/timeline").GET().build();
        HttpResponse<String> response = send(request);

        assertEquals(200, response.statusCode());
        assertContentType("application/json", response);
        List<Map<String, Object>> posts = stringJsonArrayToList(response.body());
        assertThat(posts).hasSize(3);
        assertEquals(thirdPostId, posts.get(0).get("postId"));
        assertEquals(secondPostId, posts.get(1).get("postId"));
        assertEquals(firstPostId, posts.get(2).get("postId"));
        assertThat(posts).allSatisfy((post) -> assertEquals(existingUserId, post.get("userId")));
        assertEquals("The third post.", posts.get(0).get("text"));
        assertEquals("The second post.", posts.get(1).get("text"));
        assertEquals("The first post.", posts.get(2).get("text"));
        assertThat(posts).allSatisfy((post) -> assertExpectedUTCDateTimeFormat(post.get("dateTime")));
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
