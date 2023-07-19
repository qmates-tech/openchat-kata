package tech.qmates.openchat.web;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.MimeTypes;
import tech.qmates.openchat.web.routes.AdminRoute;
import tech.qmates.openchat.web.routes.BaseRoute;
import tech.qmates.openchat.web.routes.TimelineRoute;
import tech.qmates.openchat.web.routes.UsersRoute;

import java.io.IOException;

public class RouterServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestURI = request.getRequestURI();
        BaseRoute route = chooseRoute(requestURI);

        if(route == null) {
            textResponse(404, "Route not found!", response);
            return;
        }

        //@formatter:off
        switch (request.getMethod()) {
            case "GET": route.handleGet(request, response); return;
            case "POST": route.handlePost(request, response); return;
            case "DELETE": route.handleDelete(request, response); return;
            default: httpMethodNotAllowedResponse(request, response);
        }
        //@formatter:on
    }

    private static BaseRoute chooseRoute(String requestURI) {
        if (requestURI.equals("/users"))
            return new UsersRoute();

        if (requestURI.matches("^/users/.+/timeline"))
            return new TimelineRoute();

        if (requestURI.equals("/admin"))
            return new AdminRoute();

        return null;
    }

    private void httpMethodNotAllowedResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        textResponse(405, "Method " + request.getMethod() + " not allowed!", response);
    }

    protected void textResponse(int statusCode, String text, HttpServletResponse response) throws IOException {
        response.setContentType(MimeTypes.Type.TEXT_PLAIN_UTF_8.toString());
        response.setStatus(statusCode);
        response.getWriter().print(text);
    }
}
