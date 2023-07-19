package tech.qmates.openchat.web.routes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tech.qmates.openchat.domain.usecase.GetTimelineUseCase;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

public class TimelineRoute extends BaseRoute {

    @Override
    public void handleGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            UUID userUUID = extractUserIdFromRequestURI(request);
            GetTimelineUseCase usecase = new GetTimelineUseCase();
            List<Object> posts = usecase.run(userUUID);
            jsonResponse(SC_OK, posts, response);
        } catch (IllegalArgumentException ex) {
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
