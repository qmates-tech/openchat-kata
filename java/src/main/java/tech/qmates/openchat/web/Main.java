package tech.qmates.openchat.web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("Starting the server at localhost:8000 ...");

        ServletHandler routerServletHandler = new ServletHandler();
        routerServletHandler.addServletWithMapping(RouterServlet.class, "/*");

        Server server = new Server(8000);
        server.setHandler(routerServletHandler);
        server.setErrorHandler(new OpenChatServerErrorHandler());
        server.start();
        server.join();
    }

}
