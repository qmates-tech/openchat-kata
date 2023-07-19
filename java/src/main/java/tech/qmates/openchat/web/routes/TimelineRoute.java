package tech.qmates.openchat.web.routes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

public class TimelineRoute extends BaseRoute {

    @Override
    public void handleGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userUUID = extractUserIdFromRequestURI(request);
        if (userUUID.equals("unexisting")) {
            textResponse(SC_NOT_FOUND, "User not found.", response);
            return;
        }

        jsonResponse(SC_OK, Collections.emptyList(), response);
    }

    private static String extractUserIdFromRequestURI(HttpServletRequest request) {
        Pattern pathRegex = Pattern.compile("^/users/(.+)/timeline");
        Matcher matcher = pathRegex.matcher(request.getRequestURI());
        if (!matcher.matches() || matcher.groupCount() < 1)
            throw new RuntimeException("Request URI " + request.getRequestURI() + " do not match expected format!");

        return matcher.group(1);
    }

}
