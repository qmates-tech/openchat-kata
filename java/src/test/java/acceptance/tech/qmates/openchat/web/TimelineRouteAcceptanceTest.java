package acceptance.tech.qmates.openchat.web;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TimelineRouteAcceptanceTest extends BaseOpenChatRouteAcceptanceTest {

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
    void registeredUserSubmitAPost() throws IOException, InterruptedException {
        String existingUserId = registerAUser();
        HttpRequest request = requestBuilderFor("/users/" + existingUserId + "/timeline")
            .POST(bodyFor(Map.of(
                "text", "The post's text."
            )))
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
    void submitSomePostsAndGetTimelinePostsInDescendingOrder() throws IOException, InterruptedException {
        String aliceId = registerUser("alice90");
        String bobId = registerUser("bob89");

        // ========================================= submit some posts

        List<String> alicePostsIds = List.of(
            submitPost(aliceId, "Alice user, first post."),
            submitPost(aliceId, "Alice user, second post."),
            submitPost(aliceId, "Alice user, third post.")
        );
        List<String> bobPostsIds = List.of(
            submitPost(bobId, "Bob user, first post."),
            submitPost(bobId, "Bob user, second post.")
        );

        // ========================================= check alice's timeline

        HttpResponse<String> aliceTimeline = send(requestBuilderFor("/users/" + aliceId + "/timeline").GET().build());

        assertEquals(200, aliceTimeline.statusCode());
        assertContentType("application/json", aliceTimeline);
        List<Map<String, Object>> alicePosts = stringJsonArrayToList(aliceTimeline.body());
        assertThat(alicePosts).hasSize(3);
        assertEquals(alicePostsIds.get(2), alicePosts.get(0).get("postId"));
        assertEquals(alicePostsIds.get(1), alicePosts.get(1).get("postId"));
        assertEquals(alicePostsIds.get(0), alicePosts.get(2).get("postId"));
        assertThat(alicePosts).allSatisfy((post) -> assertEquals(aliceId, post.get("userId")));
        assertEquals("Alice user, third post.", alicePosts.get(0).get("text"));
        assertEquals("Alice user, second post.", alicePosts.get(1).get("text"));
        assertEquals("Alice user, first post.", alicePosts.get(2).get("text"));
        assertThat(alicePosts).allSatisfy((post) -> assertExpectedUTCDateTimeFormat(post.get("dateTime")));

        // ========================================= check bob's timeline

        HttpResponse<String> bobTimeline = send(requestBuilderFor("/users/" + bobId + "/timeline").GET().build());

        List<Map<String, Object>> bobPosts = stringJsonArrayToList(bobTimeline.body());
        assertThat(bobPosts).hasSize(2);
        assertEquals(bobPostsIds.get(1), bobPosts.get(0).get("postId"));
        assertEquals(bobPostsIds.get(0), bobPosts.get(1).get("postId"));
        assertThat(bobPosts).allSatisfy((post) -> assertEquals(bobId, post.get("userId")));
        assertEquals("Bob user, second post.", bobPosts.get(0).get("text"));
    }

    @Test
    void cannotSubmitPostWithInappropriateLanguage() throws IOException, InterruptedException {
        String existingUserId = registerAUser();
        HttpRequest request = requestBuilderFor("/users/" + existingUserId + "/timeline")
            .POST(bodyFor(Map.of(
                "text", "The word 'orange' is a forbidden word !"
            )))
            .build();

        HttpResponse<String> response = send(request);

        assertEquals(400, response.statusCode());
        assertContentType("text/plain;charset=utf-8", response);
        assertEquals("Post contains inappropriate language.", response.body());
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
