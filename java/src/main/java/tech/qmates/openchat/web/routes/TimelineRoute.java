package tech.qmates.openchat.web.routes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tech.qmates.openchat.domain.usecase.RegisterUserUseCase;
import tech.qmates.openchat.web.AppFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static jakarta.servlet.http.HttpServletResponse.*;

public class TimelineRoute extends BaseRoute {

    @Override
    public void handleGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // TODO complete
        textResponse(SC_NOT_FOUND, "User not found.", response);
    }

}
