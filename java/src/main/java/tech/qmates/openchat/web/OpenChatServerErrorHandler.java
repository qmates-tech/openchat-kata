package tech.qmates.openchat.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ErrorHandler;

import java.util.Arrays;
import java.util.Enumeration;

class OpenChatServerErrorHandler extends ErrorHandler {

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Unhandled thrownException occurred!");
        Throwable thrownException = findThrownException(request);
        if (thrownException != null) {
            thrownException.printStackTrace();
        } else {
            printAllAttributes(request);
        }

        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    private Throwable findThrownException(HttpServletRequest request) {
        for (String attribute : Arrays.asList(
            "jakarta.servlet.error.thrownException",
            "jakarta.servlet.error.exception"
        )) {
            Throwable thrownException = (Throwable) request.getAttribute(attribute);
            if (thrownException != null)
                return thrownException;
        }

        return null;
    }

    private void printAllAttributes(HttpServletRequest request) {
        Enumeration<String> attributeNames = request.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            System.out.println(attributeName + " = " + request.getAttribute(attributeName));
        }
    }

}
