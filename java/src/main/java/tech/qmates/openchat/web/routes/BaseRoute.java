package tech.qmates.openchat.web.routes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.MimeTypes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseRoute {

    private final ObjectMapper objectMapper = new ObjectMapper();

    protected void jsonResponse(
        int statusCode,
        Object responseBody,
        HttpServletResponse response
    ) throws IOException {
        String jsonResponseBody = objectMapper.writeValueAsString(responseBody);
        response.setContentType(MimeTypes.Type.APPLICATION_JSON.toString());
        response.setStatus(statusCode);
        response.getWriter().print(jsonResponseBody);
    }

    protected void textResponse(int statusCode, String text, HttpServletResponse response) throws IOException {
        response.setContentType(MimeTypes.Type.TEXT_PLAIN_UTF_8.toString());
        response.setStatus(statusCode);
        response.getWriter().print(text);
    }

    protected void emptyResponse(int statusCode, HttpServletResponse response) {
        response.setContentType(MimeTypes.Type.TEXT_PLAIN_UTF_8.toString());
        response.setStatus(statusCode);
    }

    protected Map<String, Object> stringJsonToMap(ServletInputStream inputStream) throws IOException {
        //@formatter:off
        TypeReference<HashMap<String, Object>> targetType = new TypeReference<>() { };
        return objectMapper.readValue(inputStream, targetType);
        //@formatter:on
    }

}
