package tech.qmates.openchat.web.servlets;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UsersServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ArrayList<Object> users = new ArrayList<>();
        jsonResponse(HttpServletResponse.SC_OK, users, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> requestBody = stringJsonToMap(request.getInputStream());
        String username = (String) requestBody.get("username");
        String about = (String) requestBody.get("about");


        jsonResponse(HttpServletResponse.SC_CREATED, new HashMap<>() {{
            put("id", UUID.randomUUID().toString());
            put("username", username);
            put("about", about);
        }}, response);
    }

    private Map<String, Object> stringJsonToMap(ServletInputStream inputStream) throws IOException {
        TypeReference<HashMap<String, Object>> targetType = new TypeReference<>() { };
        return objectMapper.readValue(inputStream, targetType);
    }

    private void jsonResponse(
        int statusCode,
        Object responseBody,
        HttpServletResponse response
    ) throws IOException {
        String jsonResponseBody = objectMapper.writeValueAsString(responseBody);
        response.setContentType("application/json");
        response.setStatus(statusCode);
        response.getWriter().print(jsonResponseBody);
    }
}
