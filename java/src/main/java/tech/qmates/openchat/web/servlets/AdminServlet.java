package tech.qmates.openchat.web.servlets;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tech.qmates.openchat.web.AppFactory;

public class AdminServlet {

    public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
        AppFactory.resetRepositories();
    }

}
