package tech.qmates.openchat.web.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class UsersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print("[]");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_CREATED);
        String jsonResponseBody = "{" +
            "\"id\":\"17c6b0f9-9b86-4ac1-b705-5b6d7cc60180\"," +
            "\"username\":\"pippo\"," +
            "\"about\":\"intro a pippo\"" +
            "}";
        response.getWriter().print(jsonResponseBody);
    }
}
