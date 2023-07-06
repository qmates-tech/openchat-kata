package tech.qmates.openchat.web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import tech.qmates.openchat.web.servlets.UsersServlet;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("Starting the server at localhost:8000 ...");
        var handler = new ServletHandler();
        handler.addServletWithMapping(UsersServlet.class, "/users");

        Server server = new Server(8000);
        server.setHandler(handler);
        server.start();
        server.join();
    }
}
