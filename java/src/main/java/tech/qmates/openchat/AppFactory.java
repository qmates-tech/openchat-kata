package tech.qmates.openchat;

import tech.qmates.openchat.database.SQLitePostRepository;
import tech.qmates.openchat.database.SQLiteUserRepository;
import tech.qmates.openchat.domain.UTCClock;
import tech.qmates.openchat.domain.repository.PostRepository;
import tech.qmates.openchat.domain.repository.UserRepository;
import tech.qmates.openchat.domain.usecase.GetAllUserUseCase;
import tech.qmates.openchat.domain.usecase.GetTimelineUseCase;
import tech.qmates.openchat.domain.usecase.RegisterUserUseCase;
import tech.qmates.openchat.domain.usecase.SubmitPostUseCase;

import java.net.URISyntaxException;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class AppFactory {

    private static UserRepository userRepositoryInstance;
    private static PostRepository postRepositoryInstance;

    public static GetTimelineUseCase buildGetTimelineUseCase() {
        return new GetTimelineUseCase(getPostRepository(), getUserRepository());
    }

    public static SubmitPostUseCase buildSubmitPostUseCase() {
        return new SubmitPostUseCase(getPostRepository(), getUserRepository(), getRealClock());
    }

    public static GetAllUserUseCase buildGetAllUserUseCase () {
        return new GetAllUserUseCase(AppFactory.getUserRepository());
    }

    public static RegisterUserUseCase buildRegisterUserUseCase() {
        return new RegisterUserUseCase(AppFactory.getUserRepository());
    }

    public static UserRepository getUserRepository() {
        if (userRepositoryInstance == null)
            userRepositoryInstance = new SQLiteUserRepository(getSqliteFilePath());

        return userRepositoryInstance;
    }

    public static PostRepository getPostRepository() {
        if (postRepositoryInstance == null)
            postRepositoryInstance = new SQLitePostRepository(getSqliteFilePath());

        return postRepositoryInstance;
    }

    public static void resetRepositories() {
        if (userRepositoryInstance != null)
            userRepositoryInstance.reset();
        if (postRepositoryInstance != null)
            postRepositoryInstance.reset();
    }

    public static UTCClock getRealClock() {
        return () -> ZonedDateTime.now(ZoneId.of("UTC"));
    }

    private static String getSqliteFilePath() {
        URL res = AppFactory.class.getClassLoader().getResource("production.db");
        if (res == null)
            throw new RuntimeException("Cannot find sqlite database file!");

        try {
            return res.toURI().getPath();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Cannot load sqlite database file!", e);
        }
    }
}
