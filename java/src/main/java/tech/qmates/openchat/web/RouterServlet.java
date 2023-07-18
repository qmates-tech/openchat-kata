package tech.qmates.openchat.web;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.MimeTypes;
import tech.qmates.openchat.web.routes.AdminRoute;
import tech.qmates.openchat.web.routes.TimelineRoute;
import tech.qmates.openchat.web.routes.UsersRoute;

import java.io.IOException;

public class RouterServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();

        if (requestURI.equals("/users")) {
            UsersRoute route = new UsersRoute();
            //@formatter:off
            switch (httpMethod) {
                case "GET": route.handleGet(request, response); return;
                case "POST": route.handlePost(request, response); return;
                default: httpMethodNotAllowedResponse(request, response); return;
            }
            //@formatter:on
        }

        if (requestURI.equals("/users/unexisting/timeline")) {
            TimelineRoute route = new TimelineRoute();
            //@formatter:off
            switch (httpMethod) {
                case "GET": route.handleGet(request, response); return;
                default: httpMethodNotAllowedResponse(request, response); return;
            }
            //@formatter:on
        }

        if (requestURI.equals("/admin")) {
            AdminRoute route = new AdminRoute();
            //@formatter:off
            switch (httpMethod) {
                case "DELETE": route.handleDelete(request, response); return;
                default: httpMethodNotAllowedResponse(request, response); return;
            }
            //@formatter:on
        }

        textResponse(404, "Route not found!", response);
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
