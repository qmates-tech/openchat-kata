package tech.qmates.openchat.web.servlets;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.MimeTypes;
import tech.qmates.openchat.domain.usecase.RegisterUserUsecase;
import tech.qmates.openchat.web.AppFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static jakarta.servlet.http.HttpServletResponse.*;

public class UsersServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ArrayList<Object> users = new ArrayList<>();
        jsonResponse(SC_OK, users, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> requestBody = stringJsonToMap(request.getInputStream());

        try {
            String username = (String) requestBody.get("username");
            String about = (String) requestBody.get("about");
            RegisterUserUsecase usecase = new RegisterUserUsecase(AppFactory.getUserRepository());
            usecase.run(username);

            jsonResponse(SC_CREATED, new HashMap<>() {{
                put("id", UUID.randomUUID().toString());
                put("username", username);
                put("about", about);
            }}, response);
        } catch (RegisterUserUsecase.UsernameAlreadyInUseException e) {
            textResponse(SC_BAD_REQUEST, "Username already in use.", response);
        }
    }

    private Map<String, Object> stringJsonToMap(ServletInputStream inputStream) throws IOException {
        //@formatter:off
        TypeReference<HashMap<String, Object>> targetType = new TypeReference<>() { };
        return objectMapper.readValue(inputStream, targetType);
        //@formatter:on
    }

    private void jsonResponse(
        int statusCode,
        Object responseBody,
        HttpServletResponse response
    ) throws IOException {
        String jsonResponseBody = objectMapper.writeValueAsString(responseBody);
        response.setContentType(MimeTypes.Type.APPLICATION_JSON.toString());
        response.setStatus(statusCode);
        response.getWriter().print(jsonResponseBody);
    }

    private void textResponse(int statusCode, String text, HttpServletResponse response) throws IOException {
        response.setContentType(MimeTypes.Type.TEXT_PLAIN_UTF_8.toString());
        response.setStatus(statusCode);
        response.getWriter().print(text);
    }
}
