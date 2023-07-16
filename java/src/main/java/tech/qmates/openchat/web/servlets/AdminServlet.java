package tech.qmates.openchat.web.servlets;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tech.qmates.openchat.web.AppFactory;

public class AdminServlet extends BaseOpenChatServlet {

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("!! Resetting repositories !!");
        AppFactory.resetRepositories();
    }

}
