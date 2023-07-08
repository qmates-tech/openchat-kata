package tech.qmates.openchat.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import tech.qmates.openchat.web.servlets.AdminServlet;
import tech.qmates.openchat.web.servlets.UsersServlet;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("Starting the server at localhost:8000 ...");

        ServletHandler servletHandler = new ServletHandler();
        servletHandler.addServletWithMapping(UsersServlet.class, "/users");
        // TODO add this only for tests
        servletHandler.addServletWithMapping(AdminServlet.class, "/admin");

        Server server = new Server(8000);
        server.setHandler(servletHandler);
        server.setErrorHandler(new OpenChatErrorHandler());
        server.start();
        server.join();
    }

    private static class OpenChatErrorHandler extends ErrorHandler {
        @Override
        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
            Throwable thrownException = (Throwable) request.getAttribute("jakarta.servlet.error.thrownException");
            System.out.println("Unhandled thrownException occurred!");
            thrownException.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
