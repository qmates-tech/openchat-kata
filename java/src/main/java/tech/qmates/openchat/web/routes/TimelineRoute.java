package tech.qmates.openchat.web.routes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tech.qmates.openchat.domain.UserNotFoundException;
import tech.qmates.openchat.domain.entity.Post;
import tech.qmates.openchat.domain.usecase.GetTimelineUseCase;
import tech.qmates.openchat.domain.usecase.SubmitPostUseCase;
import tech.qmates.openchat.AppFactory;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static jakarta.servlet.http.HttpServletResponse.*;

public class TimelineRoute extends BaseRoute {

    @Override
    public void handleGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            UUID userUUID = extractUserIdFromRequestURI(request);

            GetTimelineUseCase usecase = AppFactory.buildGetTimelineUseCase();
            List<Post> userPosts = usecase.run(userUUID);

            List<Map<String, String>> serializedPosts = userPosts
                .stream()
                .map(TimelineRoute::serializePost)
                .collect(Collectors.toList());
            jsonResponse(SC_OK, serializedPosts, response);
        } catch (UserNotFoundException | IllegalArgumentException ex) {
            textResponse(SC_NOT_FOUND, "User not found.", response);
        }
    }

    @Override
    public void handlePost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            UUID authorUserId = extractUserIdFromRequestURI(request);
            Map<String, Object> requestBody = stringJsonToMap(request.getInputStream());
            String postText = (String) requestBody.get("text");

            SubmitPostUseCase usecase = AppFactory.buildSubmitPostUseCase();
            Post storedPost = usecase.run(authorUserId, postText);

            jsonResponse(SC_CREATED, serializePost(storedPost), response);
        } catch (UserNotFoundException | IllegalArgumentException ex) {
            textResponse(SC_NOT_FOUND, "User not found.", response);
        }
    }

    private static Map<String, String> serializePost(Post p) {
        return Map.of(
            "postId", p.id().toString(),
            "userId", p.userId().toString(),
            "text", p.text(),
            "dateTime", serializeDateTime(p.dateTime())
        );
    }

    private static String serializeDateTime(ZonedDateTime zonedDateTime) {
        ZonedDateTime utcDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        return utcDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX"));
    }

    private static UUID extractUserIdFromRequestURI(HttpServletRequest request) {
        Pattern pathRegex = Pattern.compile("^/users/(.+)/timeline");
        Matcher matcher = pathRegex.matcher(request.getRequestURI());
        if (!matcher.matches() || matcher.groupCount() < 1)
            throw new RuntimeException("Request URI " + request.getRequestURI() + " do not match expected format!");

        return UUID.fromString(matcher.group(1));
    }

}
