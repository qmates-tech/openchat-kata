package tech.qmates.openchat.web.routes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tech.qmates.openchat.domain.UserNotFoundException;
import tech.qmates.openchat.domain.entity.Post;
import tech.qmates.openchat.domain.usecase.GetTimelineUseCase;
import tech.qmates.openchat.domain.usecase.SubmitPostUseCase;
import tech.qmates.openchat.web.AppFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static jakarta.servlet.http.HttpServletResponse.*;

public class TimelineRoute extends BaseRoute {

    @Override
    public void handleGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            UUID userUUID = extractUserIdFromRequestURI(request);
            GetTimelineUseCase usecase = new GetTimelineUseCase(AppFactory.getUserRepository());
            List<Post> posts = usecase.run(userUUID);
            jsonResponse(SC_OK, posts, response);
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

            SubmitPostUseCase usecase = new SubmitPostUseCase(
                AppFactory.getPostRepository(),
                AppFactory.getUserRepository(),
                AppFactory.getRealClock());
            Post storedPost = usecase.run(authorUserId, postText);

            jsonResponse(SC_CREATED, Map.of(
                "postId", storedPost.id(),
                "userId", storedPost.userId(),
                "text", storedPost.text(),
                "dateTime", "2023-09-19T19:30:00Z"
            ), response);

        } catch (UserNotFoundException | IllegalArgumentException ex) {
            textResponse(SC_NOT_FOUND, "User not found.", response);
        }
    }

    private static UUID extractUserIdFromRequestURI(HttpServletRequest request) {
        Pattern pathRegex = Pattern.compile("^/users/(.+)/timeline");
        Matcher matcher = pathRegex.matcher(request.getRequestURI());
        if (!matcher.matches() || matcher.groupCount() < 1)
            throw new RuntimeException("Request URI " + request.getRequestURI() + " do not match expected format!");

        return UUID.fromString(matcher.group(1));
    }

}
