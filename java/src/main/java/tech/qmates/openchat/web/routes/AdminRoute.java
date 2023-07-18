package tech.qmates.openchat.web.routes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tech.qmates.openchat.web.AppFactory;

public class AdminRoute extends BaseRoute {

    @Override
    public void handleDelete(HttpServletRequest request, HttpServletResponse response) {
        AppFactory.resetRepositories();
        emptyResponse(200, response);
    }

}
