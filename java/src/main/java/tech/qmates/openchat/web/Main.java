package tech.qmates.openchat.web;

import org.eclipse.jetty.server.Server;
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
        server.setErrorHandler(new OpenChatServerErrorHandler());
        server.start();
        server.join();
    }

}
