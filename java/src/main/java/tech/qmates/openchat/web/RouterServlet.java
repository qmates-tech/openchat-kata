package tech.qmates.openchat.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.MimeTypes;
import tech.qmates.openchat.web.routes.AdminRoute;
import tech.qmates.openchat.web.routes.UsersRoute;

import java.io.IOException;

public class RouterServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getRequestURI().equals("/users")) {
            UsersRoute usersServlet = new UsersRoute();
            if (request.getMethod().equals("GET")) {
                usersServlet.handleGet(request, response);
                return;
            }
            if (request.getMethod().equals("POST")) {
                usersServlet.handlePost(request, response);
                return;
            }
            textResponse(405, "Method " + request.getMethod() + " not allowed!", response);
            return;
        }
        if (request.getRequestURI().equals("/admin")) {
            AdminRoute adminServlet = new AdminRoute();
            if (request.getMethod().equals("DELETE")) {
                adminServlet.handleDelete(request, response);
                return;
            }
            textResponse(405, "Method " + request.getMethod() + " not allowed!", response);
            return;
        }

        textResponse(404, "Route not found!", response);
        super.service(request, response);
    }

    protected void textResponse(int statusCode, String text, HttpServletResponse response) throws IOException {
        response.setContentType(MimeTypes.Type.TEXT_PLAIN_UTF_8.toString());
        response.setStatus(statusCode);
        response.getWriter().print(text);
    }
}
