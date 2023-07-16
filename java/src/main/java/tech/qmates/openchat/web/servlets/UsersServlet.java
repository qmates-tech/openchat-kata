package tech.qmates.openchat.web.servlets;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tech.qmates.openchat.domain.entity.RegisteredUser;
import tech.qmates.openchat.domain.usecase.GetAllUserUseCase;
import tech.qmates.openchat.domain.usecase.RegisterUserUseCase;
import tech.qmates.openchat.web.AppFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static jakarta.servlet.http.HttpServletResponse.*;

public class UsersServlet extends BaseOpenChatServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        GetAllUserUseCase usecase = new GetAllUserUseCase(AppFactory.getUserRepository());
        List<RegisteredUser> users = usecase.run();
        List<HashMap<String, Object>> listOfMapUsers = users.stream()
            .map(user -> new HashMap<String, Object>() {{
                put("id", user.uuid().toString());
                put("username", user.username());
                put("about", user.about());
            }})
            .collect(Collectors.toList());
        jsonResponse(SC_OK, listOfMapUsers, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> requestBody = stringJsonToMap(request.getInputStream());
        String username = (String) requestBody.get("username");
        String password = (String) requestBody.get("password");
        String about = (String) requestBody.get("about");

        try {
            RegisterUserUseCase usecase = new RegisterUserUseCase(AppFactory.getUserRepository());
            UUID storedUserUUID = usecase.run(username, password, about);

            jsonResponse(SC_CREATED, new HashMap<>() {{
                put("id", storedUserUUID.toString());
                put("username", username);
                put("about", about);
            }}, response);
        } catch (RegisterUserUseCase.UsernameAlreadyInUseException e) {
            textResponse(SC_BAD_REQUEST, "Username already in use.", response);
        }
    }

}
